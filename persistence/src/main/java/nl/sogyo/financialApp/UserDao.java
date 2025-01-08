package nl.sogyo.financialApp;

import java.sql.*;
import java.util.List;

import com.password4j.Hash;
import com.password4j.Password;

import nl.sogyo.financialApp.exception.UserNotFoundException;

public class UserDao implements IUserDAO{

    private final String saveUserQuery = """
    INSERT INTO users (email, first_name, last_name,
    street_name, zip_code, house_number, city, country, password_hash) VALUES 
    (?, ?, ?, ?, ?, ?, ?, ?, ?);
    """;

    private final String findUserByEmail = """
    SELECT * FROM users WHERE email = ?;
    """;

    private final String findAllUsers = """
    SELECT * FROM users;
    """;

	@Override
	public void save(User user, String password) {
        String passwordHash = hashPassword(password);
        try {
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement stmt = connection.prepareStatement(saveUserQuery);
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getFirstName());
            stmt.setString(3, user.getLastName());
            stmt.setString(4, user.getStreetName());
            stmt.setString(5, user.getZipCode());
            stmt.setString(6, user.getHouseNumber());
            stmt.setString(7, user.getCity());
            stmt.setString(8, user.getCountry());
            stmt.setString(9, passwordHash);
            stmt.executeUpdate();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
	}

    // because then the frontend needs to get notified
    @Override
    public User findByEmail(String email){
        try {
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement stmt = connection.prepareStatement(findUserByEmail);
            stmt.setString(1, email);
            try (ResultSet resultSet = stmt.executeQuery()){
                if (resultSet.next()) {
                    return mapToUser(resultSet);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        return null;
    }


    public boolean doesUserExist(String email){
        try {
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement stmt = connection.prepareStatement(findUserByEmail);
            stmt.setString(1, email);
            try (ResultSet resultSet = stmt.executeQuery()){
                    return resultSet.next();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
    }

    public boolean isLoginCorrect(String email, String password){
        try {
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement stmt = connection.prepareStatement(findUserByEmail);
            stmt.setString(1, email);
            try (ResultSet resultSet = stmt.executeQuery()){
                if (resultSet.next()){
                    String hashedPassword = resultSet.getString("password_hash");
                    if (isPasswordCorrect(hashedPassword, password)){
                        return true;
                    }
                }
              }
            } catch (SQLException e){
                e.printStackTrace();
            }
            return false;
    }
	
    @Override
	public List<User> findAll() {
		// TODO Auto-generated method stub
        List<User> users = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement(findUserByEmail);
            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next()){
                User user = mapToUser(resultSet);
                users.add(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return users;
	}

	@Override
	public void update(User user) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'update'");
	}

	@Override
	public void delete(int id) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'delete'");
	}

    protected String hashPassword(String password){
        Hash hash = Password.hash(password)
                            .withArgon2();
        return hash.getResult();
    }

    protected boolean isPasswordCorrect(String hashedPassword, String password){
        return Password.check(password, hashedPassword).withArgon2();
    }
    
    protected User mapToUser(ResultSet resultSet){
        try {
            String first_name = resultSet.getString("first_name");
            String last_name = resultSet.getString("last_name");
            String email = resultSet.getString("email");
            String city = resultSet.getString("city");
            String country = resultSet.getString("country");
            String zip_code = resultSet.getString("zip_code");
            String street_name = resultSet.getString("street_name");
            String house_number = resultSet.getString("house_number");

            User user = User.createUser(first_name, last_name, email, street_name, zip_code, house_number, city, country);

            return user;
        } catch (Exception e) {
            e.printStackTrace();

        }
        return null;
        
    }

}

