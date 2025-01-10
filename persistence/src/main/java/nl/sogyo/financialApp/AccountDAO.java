package nl.sogyo.financialApp;

import java.sql.*;

import java.sql.SQLException;
import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public class AccountDAO implements IAccountDAO{

    private final String saveAccountQuery = """
    INSERT INTO accounts (user_id, account_type, account_name, balance)
    VALUES (?, ?, ?, ?)";
    """;

    private final String findAccountById = """
    SELECT * FROM accounts WHERE id = ?;
    """;

    private final String findAccountsByUserId = """
    SELECT * FROM accounts WHERE user_id = ?;
    """;

    private final String deleteAccountWithId = """
    DELETE FROM accounts WHERE id = ?;
    """;

    private Connection getConnection(){
        try (Connection connection = DatabaseConnection.getConnection()) {
            return connection;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

	@Override
	public void save(Account account) {
        Connection conn = getConnection();
        // TODO: Implement this
	}

	@Override
	public void delete(Account account) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'delete'");
	}

	@Override
	public void update(Account account) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'update'");
	}

	@Override
	public List<Account> getAllAccountWithUserId(int id) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'getAllAccountWithUserId'");
	}
}
