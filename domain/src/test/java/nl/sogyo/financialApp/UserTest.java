package nl.sogyo.financialApp;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
/**
 * Unit test for simple App.
 */
public class UserTest {

    private User user;
    
    @BeforeEach
    public void setUp(){
        user = new User("Jelle", "Jacobs", "jelle.jacobs99@gmail.com", "Derkinderenstraat",
                "1061 vx", "196");
        System.out.println("creation of user complete");
    }

    @Test
    public void testingProjectSetup(){
        System.out.println("testingProjectSetup");
        assertEquals("Jelle", user.getUserName());
    }



} 
