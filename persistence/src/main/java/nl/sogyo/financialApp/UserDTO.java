package nl.sogyo.financialApp;

public class UserDTO {

    private String firstName;
    private String lastName;
    private String email;
    private String streetName;
    private String zipCode;
    private String houseNumber;
    private String city;
    private String country;
    private String password;

	public String getFirstName() {
        return firstName;
	}

	public String getLastName() {
        return lastName;
	}

	public String getEmailUser() {
        return email;
	}

	
	public String getStreetName() {
        return streetName;
	}

	
	public String getZipCode() {
        return zipCode;
	}

	
	public String getHouseNumber() {
        return houseNumber;
	}


	public String getCity() {
        return city;
	}

	
	public String getCountry() {
        return country;
	}

	public String getPassword() {
        return password;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setStreetName(String streetName) {
		this.streetName = streetName;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public void setHouseNumber(String houseNumber) {
		this.houseNumber = houseNumber;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
