package nl.sogyo.financialApp.controller;

import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import nl.sogyo.financialApp.*;

@RestController
@RequestMapping("/api")
public class FinancialAppController{
    
    @GetMapping("/hello")
    public String sayHello(){
        return "Hello";
    }

    @PostMapping("/createUser")
    public ResponseEntity<String> createUser(@RequestBody String jsonString){
        System.out.println(jsonString);
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            String firstName = jsonObject.getString("firstName");
            String lastName = jsonObject.getString("familyName");
            String email = jsonObject.getString("emailUser");
            String streetName = jsonObject.getString("streetName");
            String zipCode = jsonObject.getString("zipCode");
            String houseNumber = jsonObject.getString("houseNumber");
            String city = jsonObject.getString("city");
            String country = jsonObject.getString("country");
            String password = jsonObject.getString("password");
            User newUser = User.createUser(firstName, lastName, email, streetName, zipCode, houseNumber,
            city, country);
            UserDao database = new UserDao();
            if (database.doesUserExist(email)){
                return null; 
            } else {
                database.save(newUser, password); 
                return ResponseEntity.ok("succes");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.CONFLICT).body("user not created");
        }
    }



}
