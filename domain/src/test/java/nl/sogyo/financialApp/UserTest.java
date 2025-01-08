package nl.sogyo.financialApp;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
/**
 * Unit test for simple App.
 */
public class UserTest {

    private User user;
    
    @BeforeEach
    public void setUp(){
        user = User.createUser("Jelle", "Jacobs", "jelle.jacobs99@gmail.com", "Derkinderenstraat",
                "1061 vx", "196", "Amsterdam", "Netherlands");
    }

    @Test
    public void testingProjectSetup(){
        System.out.println("testingProjectSetup");
        assertEquals("jelle", user.getFirstName());
    }

    @Test
    public void testIsStringEmpty() {
        assertTrue(user.isStringEmpty(" "));
        assertTrue(user.isStringEmpty(""));
        assertFalse(user.isStringEmpty("Not Empty"));
    }

    @Test
    public void testValidUserCreation() {
        assertNotNull(user);
        assertEquals("jelle", user.getFirstName());
        assertEquals("jacobs", user.getLastName());
        assertEquals("derkinderenstraat", user.getStreetName());
        assertEquals("amsterdam", user.getCity());
        assertEquals("netherlands", user.getCountry());
    }

    @Test
    public void testStripAndDecapitalize() {
        user = User.createUser("  Jelle  ", "  Jacobs  ", " jelle.jacobs99@gmail.com ",
                " Derkinderenstraat ", " 1061 VX ", " 196 ", " Amsterdam ", " Netherlands ");
        assertNotNull(user);
        assertEquals("jelle", user.getFirstName());
        assertEquals("jacobs", user.getLastName());
        assertEquals("derkinderenstraat", user.getStreetName());
        assertEquals("amsterdam", user.getCity());
        assertEquals("netherlands", user.getCountry());
    }

    @Test
    public void testNullInput() {
        user = User.createUser(null, "Jacobs", "jelle.jacobs99@gmail.com", "Derkinderenstraat",
                "1061 VX", "196", "Amsterdam", "Netherlands");
        assertNull(user);

        user = User.createUser("Jelle", null, "jelle.jacobs99@gmail.com", "Derkinderenstraat",
                "1061 VX", "196", "Amsterdam", "Netherlands");
        assertNull(user);
    }

    @Test
    public void testInvalidName() {
        user = User.createUser("Jelle!", "Jacobs", "jelle.jacobs99@gmail.com", "Derkinderenstraat",
                "1061 VX", "196", "Amsterdam", "Netherlands");
        assertNull(user);
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
        user = User.createUser("Jelle", "Jacobs", "jelle.jacobs99@gmail.com", "Derkinderenstraat",
                "1061", "196", "Amsterdam", "Netherlands");
        assertNull(user);
    }

     @Test
    public void testCitySanitization() {
        user = User.createUser("Jelle", "Jacobs", "jelle.jacobs99@gmail.com", "Derkinderenstraat",
                "1061 VX", "196", "    AmsterDam   ", " Netherlands ");
        assertNotNull(user);
        assertEquals("amsterdam", user.getCity());
    }

    
    @Test
    public void testInvalidEmailNoAtSymbol() {
        user = User.createUser("Jelle", "Jacobs", "jelle.jacobs99gmail.com", "Derkinderenstraat",
                "1061 VX", "196", "Amsterdam", "Netherlands");
        assertNull(user);
    }

    @Test
    public void testInvalidHouseNumber() {
        user = User.createUser("Jelle", "Jacobs", "jelle.jacobs99@gmail.com", "Derkinderenstraat",
                "1061 VX", "", "Amsterdam", "Netherlands");
        assertNull(user);
    }

    
    @Test
    public void testEmailNotDecapitalized() {
        user = User.createUser("Jelle", "Jacobs", "JELLE.JACOBS99@GMAIL.COM", "Derkinderenstraat",
                "1061 VX", "196", "Amsterdam", "Netherlands");
        assertNotNull(user);
        assertEquals("JELLE.JACOBS99@GMAIL.COM", user.getEmail());
    }
    
    @Test
    public void testCountrySanitization() {
        user = User.createUser("Jelle", "Jacobs", "jelle.jacobs99@gmail.com", "Derkinderenstraat",
                "1061 VX", "196", "Amsterdam", "    NetherLands   ");
        assertNotNull(user);
        assertEquals("netherlands", user.getCountry());
    }

    @Test
    public void testChangeFirstName(){
        user.changeFirstName("Hennie");
        assertEquals("hennie", user.firstName);
    }


} 
