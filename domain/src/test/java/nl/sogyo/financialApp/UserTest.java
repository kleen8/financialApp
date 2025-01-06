package nl.sogyo.financialApp;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.BeforeEach;
/**
 * Unit test for simple App.
 */
public class UserTest {

    private User user;
    
    @BeforeEach
    public void setUp(){
        System.out.println("Setting up user");
        user = User.createUser("Jelle", "Jacobs", "jelle.jacobs99@gmail.com", "Derkinderenstraat",
                "1061 vx", "196", "Amsterdam", "Netherlands");
    }

    @Test
    public void testingProjectSetup(){
        System.out.println("testingProjectSetup");
        assertEquals("Jelle", user.getFirstName());
    }


    @Test
    public void testIsStringEmpty(){
        assertEquals(true, user.isStringEmpty(" "));
    }

    @Test
    public void testWrongInput(){
        user = User.createUser("", "Jacobs", "jelle.jacobs99@gmail.com", "Derkinderenstraat",
                "1061 vx", "196", "Amsterdam", "Netherlands");
        assertNull(user);
    }

    @Test
    public void testWrongEmail(){
        user = User.createUser("Jelle", "Jacobs", "jelle.jacobs99", "Derkinderenstraat",
                "1061 vx", "196", "Amsterdam", "Netherlands");
        assertNull(user);
    }


    @Test
    public void testWrongEmailDot(){
        user = User.createUser("Jelle", "Jacobs", "jelle.jacobs99@gmail", "Derkinderenstraat",
                "1061 vx", "196", "Amsterdam", "Netherlands");
        assertNull(user);
    }

    @Test
    public void testWrongZipCode(){
        user = User.createUser("Jelle", "Jacobs", "jelle.jacobs99@gmail", "Derkinderenstraat",
                "1061", "196", "Amsterdam", "Netherlands");
        assertNull(user);
    }

    @Test
    public void testChangeFirstName(){
        user.changeFirstName("Hennie");
        assertEquals("Hennie", user.firstName);
    }

} 
