package nl.sogyo.financialApp;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
/**
 * Unit test for simple App.
 */
public class AppTest 
{
    @Test
    public void testingProjectSetup(){
        domainTest newApp = new domainTest();

        assertEquals("hello", newApp.printInput("hello"));
    }
} 
