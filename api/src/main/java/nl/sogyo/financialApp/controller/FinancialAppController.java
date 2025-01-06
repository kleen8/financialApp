package nl.sogyo.financialApp.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.sql.Connection;
import nl.sogyo.financialApp.*;

@CrossOrigin(origins = "*" )
@RestController
@RequestMapping("/api")
public class FinancialAppController{
    


    @GetMapping("/hello")
    public String sayHello(){
        return "Hello";
    }

    @PostMapping("/createUser")
    public String createUser(@RequestBody String jsonString){
        System.out.println(jsonString);
        Connection connection = DatabaseConnection.getConnection();  
        if (connection != null) {
            return "Account Created";
        }
        return "error";
    }
}
