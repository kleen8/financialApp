package nl.sogyo.financialApp.controller;

import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.servlet.http.HttpSession;

import nl.sogyo.financialApp.*;

@RestController
@RequestMapping("/api")
public class FinancialAppController{

    private final IUserDAO userDAO;
    private final HttpSession session;
    private final IAccountDAO accountDAO; 
    private final ITransactionDAO transactionDAO;

    @Autowired
    public FinancialAppController(IUserDAO userDAO, ITransactionDAO transactionDAO, IAccountDAO accountDAO, HttpSession session){
        this.userDAO = userDAO;
        this.accountDAO = accountDAO;
        this.session = session;
        this.transactionDAO = transactionDAO;
    }

     
    @GetMapping("/hello")
    public String sayHello(){
        Object userEmail = session.getAttribute("userEmail");
        System.out.println(userDAO.doesUserExist((String) userEmail));
        return "Hello";
    }

    @PostMapping("/create-user")
    public ResponseEntity<String> createUser(@RequestBody UserDTO userDTO){
        try {
            User user = User.createUser(
                userDTO.getFirstName(),
                userDTO.getLastName(),
                userDTO.getEmailUser(),
                userDTO.getStreetName(),
                userDTO.getZipCode(),
                userDTO.getHouseNumber(),
                userDTO.getCity(),
                userDTO.getCountry()
            );
            if (userDAO.doesUserExist(userDTO.getEmailUser())){
                return ResponseEntity.status(HttpStatus.CONFLICT).body("User already exists");
            } else if (user != null) {
                userDAO.save(user, userDTO.getPassword()); 
                return ResponseEntity.ok("User created succesfully");
            } else {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("User is not created");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
        }
    }

    @PostMapping("/login-user")
    public ResponseEntity<String> loginUser(@RequestBody String jsonString, HttpSession session){
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            String email = jsonObject.optString("email", "").trim();
            String password = jsonObject.optString("password", "").trim();
            HashMap<String, String> sessionCredentials = userDAO.loginUser(email, password);
            if (sessionCredentials != null){
                session.setAttribute("userId", sessionCredentials.get("userId"));
                session.setAttribute("userEmail", sessionCredentials.get("userEmail"));
                return ResponseEntity.ok("User excists");
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Email or password incorrect");
        } catch (JSONException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
        }
    }

    @GetMapping("/check-login")
    public ResponseEntity<String> checkLogin() {
        String userEmail = (String) session.getAttribute("userEmail");
        if (userEmail != null && !userEmail.isBlank() && !userEmail.isEmpty()){
            return ResponseEntity.ok("User is logged in");
        } else {
            return ResponseEntity.status(401).body("Unauthorized");
        }
    }

    @PostMapping("/create-account")
    public ResponseEntity<AccountDTO> addAccount(@RequestBody AccountDTO accountDTO) {
        try {
            System.out.println("In create account");
            String userId = (String) session.getAttribute("userId");
            int userIdInt = Integer.parseInt(userId);
            User user = userDAO.getUserWithId(userIdInt);
            String account_type = accountDTO.getAccountType(); 
            String account_name = accountDTO.getAccountName(); 
            double balance = accountDTO.getBalance();
            Account account = null;
            switch (account_type) {
                case "General":
                account = new GeneralAccount(account_name, user, balance);
                break;
                case "Saving":
                account = new SavingsAccount(account_name, user, balance);
                break;
                case "Investing":
                account = new InvestmentsAccount(account_name, user, balance);
                break;
                default:
                break;
            }
            if (account != null){
                accountDTO.setAccountId(accountDAO.saveAndReturnId(account, userIdInt));
                System.out.println("Account balance is: " + accountDTO.getBalance());
                System.out.println("Account id is: " + accountDTO.getAccountId());
                return ResponseEntity.ok(accountDTO);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/get-accounts")
    public ResponseEntity<List<AccountDTO>> getAccounts(HttpSession session){
        String userId = (String) session.getAttribute("userId");
        int userIdInt = Integer.parseInt(userId);
        List<AccountDTO> accountsDTO = accountDAO.getAccountDTOsWithUserId(userIdInt);
        if (accountsDTO == null || accountsDTO.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(accountsDTO);        
    }

    @PostMapping("/post-account-credentials")
    public ResponseEntity<String> postAccountCred(@RequestBody AccountDTO accountDTO, HttpSession session){
        session.setAttribute("accountId", accountDTO.getAccountId());
        session.setAttribute("accountName", accountDTO.getAccountName());
        session.setAttribute("accountType", accountDTO.getAccountType());
        return ResponseEntity.ok("ok");
    }

    @PostMapping("/post-transaction")
    public ResponseEntity<TransactionDTO> postTransaction(@RequestBody TransactionDTO transactionDTO, HttpSession session){
        Integer accountId = (Integer) session.getAttribute("accountId");
        transactionDTO.setAccountId(accountId);
        transactionDAO.save(transactionDTO);
        return ResponseEntity.ok(transactionDTO);
    }

    @GetMapping("/get-all-transactions")
    public ResponseEntity<List<TransactionDTO>> getAllTransaction(HttpSession session){
        Integer accountId = (Integer) session.getAttribute("accountId");
        return ResponseEntity.ok(transactionDAO.getAllTransactionDTOWitAccId(accountId));
    }

}
