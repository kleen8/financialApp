package nl.sogyo.financialApp;

public class User {

    protected String name;
    protected String familyName;
    protected String email;
    protected String address;
    protected String zipCode;
    protected String houseNumber;

    public User(String name, String familyName,
            String email, String address, String zipCode, String houseNumber){
        this.name = name;
        this.familyName = familyName;
        this.email = email;
        this.address = address;
        this.zipCode = zipCode;
        this.houseNumber = houseNumber;
        
    }

    protected String getUserName(){
        return name;
    }



}
