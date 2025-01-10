package nl.sogyo.financialApp.controller;

import java.util.HashMap;

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

    @Autowired
    private HttpSession session;
    
    @GetMapping("/hello")
    public String sayHello(){
        Object userEmail = session.getAttribute("userEmail");
        System.out.println(userEmail);
        return "Hello";
    }

    @PostMapping("/create-user")
    public ResponseEntity<String> createUser(@RequestBody String jsonString){
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            String firstName = jsonObject.optString("firstName", "").trim();
            String lastName = jsonObject.optString("familyName", "").trim();
            String email = jsonObject.optString("emailUser", "").trim();
            String streetName = jsonObject.optString("streetName", "").trim();
            String zipCode = jsonObject.optString("zipCode", "").trim();
            String houseNumber = jsonObject.optString("houseNumber", "").trim();
            String city = jsonObject.optString("city", "").trim();
            String country = jsonObject.optString("country", "").trim();
            String password = jsonObject.optString("password", "").trim();
            User newUser = User.createUser(firstName, lastName, email, streetName, zipCode, houseNumber,
            city, country);
            UserDao database = new UserDao();
            if (database.doesUserExist(email)){
                return ResponseEntity.status(HttpStatus.CONFLICT).body("User already exists");
            } else {
                database.save(newUser, password); 
                return ResponseEntity.ok("User created succesfully");
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Invalid JSON payload");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
        }
    }

    @PostMapping("/login-user")
    public ResponseEntity<String> loginUser(@RequestBody String jsonString, HttpSession session){
        UserDao database = new UserDao();
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            String email = jsonObject.optString("email", "").trim();
            String password = jsonObject.optString("password", "").trim();
            HashMap<String, String> sessionCredentials = database.loginUser(email, password);
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
        UserDao userData = new UserDao();
        String userId = (String) session.getAttribute("userId");
        int userIdInt = Integer.parseInt(userId);
        User user = userData.findById(userIdInt);
        System.out.println(user.toString());
        return ResponseEntity.ok().body("everything Okey");
    }
        
}
