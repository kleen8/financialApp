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
import nl.sogyo.financialApp.controller.DTO.UserDTO;

@RestController
@RequestMapping("/api")
public class FinancialAppController{

    private final IUserDAO userDAO;
    private final HttpSession session;
    private final IAccountDAO accountDAO; 

    @Autowired
    public FinancialAppController(IUserDAO userDAO,IAccountDAO accountDAO ,HttpSession session){
        this.userDAO = userDAO;
        this.accountDAO = accountDAO;
        this.session = session;
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
            System.out.println("first name: "+ userDTO.getFirstName());
            System.out.println("Last name: " + userDTO.getLastName());
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
        if (userEmail != null){
            return ResponseEntity.ok("User is logged in");
        } else {
            return ResponseEntity.status(401).body("Unauthorized");
        }
    }

    @PostMapping("/create-account")
    public ResponseEntity<String> addAccount(@RequestBody String jsonString, HttpSession session) {
        String userId = (String) session.getAttribute("userId");
        int userIdInt = Integer.parseInt(userId);
        User user = userDAO.getUserWithId(userIdInt);
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            String account_type = jsonObject.optString("account_type").trim();
            String account_name = jsonObject.optString("account_name").trim();
            double balance = Double.parseDouble(jsonObject.optString("balance"));
            Account account = null;
            switch (account_type) {
                case "General":
                    account = new GeneralAccount(account_name, user, balance);
                    break;
                case "Savings":
                    account = new SavingsAccount(account_name, user, balance);
                    break;
                case "Investing":
                    account = new InvestmentsAccount(account_name, user, balance);
                    break;
                default:
                    break;
            }
            if (account != null){
                accountDAO.save(account, userIdInt);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("An error occured in /create-account");
        }
        return ResponseEntity.ok().body("everything Okey");
    }
        
    @GetMapping("get-accounts")
    public ResponseEntity<String> getAccounts(){
        String userId = (String) session.getAttribute("userId");
        int userIdInt = Integer.parseInt(userId);
        List<Account> accounts = accountDAO.getAllAccountWithUserId(userIdInt);
        return ResponseEntity.ok().body("Accounts are called");        
    }
}
