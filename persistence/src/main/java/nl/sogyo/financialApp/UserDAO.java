package nl.sogyo.financialApp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.password4j.Hash;
import com.password4j.Password;

import nl.sogyo.financialApp.exception.UserNotFoundException;
import nl.sogyo.financialApp.exception.AuthenticationException;

import org.springframework.stereotype.Repository;

@Repository
public class UserDAO implements IUserDAO{

    private static final Logger LOGGER = LoggerFactory.getLogger(UserDAO.class);


    private final String saveUserQuery = """
    INSERT INTO users (email, first_name, last_name,
    street_name, zip_code, house_number, city, country, password_hash) VALUES 
    (?, ?, ?, ?, ?, ?, ?, ?, ?);
    """;

    private final String findUserById = """
    SELECT * FROM users WHERE id = ?;
    """;

    private final String findUserByEmail = """
    SELECT * FROM users WHERE email = ?;
    """;

    private final String findAllUsers = """
    SELECT * FROM users;
    """;

    private final String deleteUser = """
    DELETE FROM users WHERE email = ?;
    """;

    private final String updateUserEmailQry = """
    UPDATE users 
    SET first_name = COALESCE(?, first_name),
    last_name = COALESCE(?, last_name),
    street_name = COALESCE(?, street_name),
    zip_code = COALESCE(?, zip_code),
    house_number = COALESCE(?, house_number),
    city = COALESCE(?, city),
    country = COALESCE(?, country)
    WHERE email = ?;
    """;

    @Override
    public void save(User user, String password) {
        String passwordHash = hashPassword(password);
        try (Connection connection = DatabaseConnection.getConnection()){
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
        } catch (SQLException e) {
            LOGGER.error("SQLException occured at {}: {}" , java.time.LocalDateTime.now(), e.getMessage());
            throw new RuntimeException("Database error occured");
        }
    }

    // because then the frontend needs to get notified
	@Override
    public User getUserWithId(int id){
        try (Connection connection = DatabaseConnection.getConnection()){
            PreparedStatement stmt = connection.prepareStatement(findUserById);
            stmt.setInt(1, id);
            try (ResultSet resultSet = stmt.executeQuery()){
                if (resultSet.next()) {
                    return mapToUser(resultSet);
                } else {
                    throw new UserNotFoundException("User is not found");
                }
            }
        } catch (SQLException e) {
            LOGGER.error("SQLException occured at {}: {}" , java.time.LocalDateTime.now(), e.getMessage());
            throw new RuntimeException("Database error occured");
        }
    }


    // because then the frontend needs to get notified
    @Override
    public User getUserWithEmail(String email){
        try {
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement stmt = connection.prepareStatement(findUserByEmail);
            stmt.setString(1, email);
            try (ResultSet resultSet = stmt.executeQuery()){
                if (resultSet.next()) {
                    return mapToUser(resultSet);
                } else {
                    throw new UserNotFoundException("User is not found");
                }
            }
        } catch (SQLException e) {
            LOGGER.error("SQLException occured at {}: {}" , java.time.LocalDateTime.now(), e.getMessage());
            throw new RuntimeException("Database error occured");
        }
    }

    @Override
    public boolean doesUserExist(String email){
        try (Connection connection = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement(findUserByEmail);
            stmt.setString(1, email);
            try (ResultSet resultSet = stmt.executeQuery()){
                return resultSet.next();
            }
        } catch (SQLException e) {
            LOGGER.error("SQLException occured at {}: {}" , java.time.LocalDateTime.now(), e.getMessage());
            return false;
        }
    }
    
    @Override
    public HashMap<String, String> loginUser(String email, String password) {
        HashMap<String, String> sessionCredentials = new HashMap<String, String>(); 
        try (Connection connection = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement(findUserByEmail);
            stmt.setString(1, email);
            try (ResultSet resultSet = stmt.executeQuery()){
                if (resultSet.next()){
                    String hashedPassword = resultSet.getString("password_hash");
                    if (isPasswordCorrect(hashedPassword, password)){
                        Integer userId = resultSet.getInt("id");
                        sessionCredentials.put("userEmail", email);
                        sessionCredentials.put("userId", userId.toString());
                        LOGGER.info("User found for email: {}", email);
                        return sessionCredentials;
                    } else {
                        throw new AuthenticationException("Authentication incorrect");
                    }
                } else {
                    LOGGER.warn("User not found for email: {}", email);
                    throw new UserNotFoundException("User with email: " + email + " not found");
                }
            }
        } catch (SQLException e) {
            LOGGER.error("SQLException occured at {}: {}" , java.time.LocalDateTime.now(), e.getMessage());
            throw new RuntimeException("Database error occured");
        }
    }

    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement(findAllUsers);
            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next()){
                User user = mapToUser(resultSet);
                users.add(user);
            }
            return users;
        } catch (SQLException e) {
            LOGGER.error("SQLException occured at {}: {}" , java.time.LocalDateTime.now(), e.getMessage());
            throw new RuntimeException("Database error occured");
        }
    }

    @Override
    public void update(User user) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement(updateUserEmailQry);
            stmt.setString(1, user.getFirstName());
            stmt.setString(2, user.getLastName());
            stmt.setString(3, user.getStreetName());
            stmt.setString(4, user.getZipCode());
            stmt.setString(5, user.getHouseNumber());
            stmt.setString(6, user.getCity());
            stmt.setString(7, user.getCountry());
            stmt.setString(9, user.getEmail());
            stmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error("SQLException occured at {}: {}" , java.time.LocalDateTime.now(), e.getMessage());
            throw new RuntimeException("Database error occured");
        }
    }

    @Override
    public void delete(String email) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement(deleteUser);
            stmt.setString(1, email);
            stmt.executeUpdate();
        } catch (Exception e) {
            LOGGER.error("SQLException occured at {}: {}" , java.time.LocalDateTime.now(), e.getMessage());
            throw new RuntimeException("Database error occured");
        }
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
    
    @Override
    public int getUserIdByEmail(String email) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement(findUserByEmail);
            stmt.setString(1, email);
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()){
                return (int) resultSet.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            LOGGER.error("SQLException occured at {}: {}" , java.time.LocalDateTime.now(), e.getMessage());
            throw new RuntimeException("Database error occured");
        }
        throw new UserNotFoundException("User with email: " + email + " not found");
    }


}

