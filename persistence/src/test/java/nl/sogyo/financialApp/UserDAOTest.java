package nl.sogyo.financialApp;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class UserDAOTest {
    
    @Test
    public void testPasswordHash(){
        UserDao user = new UserDao();
        String password = "hello";
        String hashedPassword = user.hashPassword(password);

        assertNotEquals(password, hashedPassword);
    }
    
    @Test
    public void testPasswordCheck(){
        UserDao user = new UserDao();
        String password = "hello";
        String hashedPassword = user.hashPassword(password);
        
        assertTrue(user.isPasswordCorrect(hashedPassword, password));
    }

}
