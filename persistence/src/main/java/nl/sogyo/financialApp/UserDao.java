package nl.sogyo.financialApp;

import java.sql.*;
import java.util.List;

public class UserDao implements IUserDAO{

    private final String saveUserQuery = "INSERT INTO users (name, email) VALUES (?, ?)";

	@Override
	public void save(User user) {
        try {
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement stmt = connection.prepareStatement(saveUserQuery);
            stmt.setString(1, user.getFirstName());
            stmt.setString(2, user.getEmail());
            stmt.executeUpdate();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
	}

	@Override
	public User findById(int id) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'findById'");
	}

	@Override
	public List<User> findAll() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'findAll'");
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
}

