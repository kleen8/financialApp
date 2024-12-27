package nl.sogyo.financialApp;


// TODO: Strip the inputs so there are no leading or trailing whilespaces
// TODO: Write test cases for the strip part

public class User {

    protected String firstName;
    protected String lastName;
    protected String email;
    protected String streetName;
    protected String zipCode;
    protected String houseNumber;
    protected String city;
    protected String country;

    private User(String firstName, String lastName,
            String email, String streetName, String zipCode, String houseNumber,
            String city, String country){
     
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.streetName = streetName;
        this.zipCode = zipCode;
        this.houseNumber = houseNumber;
        this.city = city;
        this.country = country;
    }

     public static User createUser(String firstName, String lastName, String email, 
                                  String streetName, String zipCode, String houseNumber, 
                                  String city, String country) {
        // Validate inputs
        if (!isValidName(firstName) || !isValidName(lastName) || !isValidEmail(email) ||
            !isValidAddressField(streetName) || !isValidZipCode(zipCode) || 
            !isValidHouseNumber(houseNumber) || !isValidCity(city) || !isValidCountry(country)) {
            //System.out.println("Invalid input data; user not created.");
            return null;  // Return null to indicate failure
        }

        return new User(firstName, lastName, email, streetName, zipCode, houseNumber, city, country);
    }

    protected String getUserName(){
        return firstName;
    }

    protected Boolean isStringEmpty(String input){
        return input == null || input.isBlank();
    }

    private static boolean isValidName(String name){
        return name != null && !name.isBlank() && name.matches("[A-Za-z]+");
    }


    private static boolean isValidEmail(String email){
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"; 
        return email != null && email.matches(emailRegex);
    }

    private static boolean isValidAddressField(String field){
        return field != null && !field.isBlank();
    }


    private static boolean isValidZipCode(String zipCode){
        String zipCodeRegex = "^[0-9]{4}\\s?[A-Za-z]{2}$";  // Example: 1234 AB
        return zipCode != null && zipCode.matches(zipCodeRegex);
    }

     // Validate house number (can be numeric or alphanumeric in some cases)
    private static boolean isValidHouseNumber(String houseNumber) {
        return houseNumber != null && houseNumber.matches("[A-Za-z0-9]+");
    }

    // Validate city name
    private static boolean isValidCity(String city) {
        return city != null && !city.isBlank();
    }

    // Validate country name
    private static boolean isValidCountry(String country) {
        return country != null && !country.isBlank();
    }

    protected void changeFirstName(String newName){
        this.firstName = newName;
    }
}