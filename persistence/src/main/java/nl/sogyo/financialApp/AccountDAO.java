package nl.sogyo.financialApp;

import java.sql.*;

import java.sql.SQLException;
import java.util.ArrayList;
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

    private final String updateAccount = """
    UPDATE accounts
    Set account_type = ?,
        account_name = ?,
        balance = ?
    WHERE account_id = ?;
    """;

	@Override
	public void save(Account account, int userId) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement(saveAccountQuery);
            stmt.setInt(1, userId);
            stmt.setString(2, account.getClass().getName());
            stmt.setString(3, account.getAccountName());
            stmt.setDouble(4, account.getBalance());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
	}

	@Override
	public void delete(Account account) {
        try (Connection connection = DatabaseConnection.getConnection()) {
           PreparedStatement stmt = connection.prepareStatement(deleteAccountWithId);
            

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
		throw new UnsupportedOperationException("Unimplemented method 'delete'");
	}

	@Override
	public void update(Account account, int accountId) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement(updateAccount);
            stmt.setString(1, account.getClass().getName());
            stmt.setString(2, account.getAccountName());
            stmt.setDouble(3, account.getBalance());
            stmt.setInt(4, accountId);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
		throw new UnsupportedOperationException("Unimplemented method 'update'");
	}

	@Override
	public List<Account> getAllAccountWithUserId(int id) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement(findAccountsByUserId);

            try (ResultSet resultSet = stmt.executeQuery()) {
                List<Account> accounts = new ArrayList<Account>();
                while (resultSet.next()){
                    Account account = mapToAccount(resultSet);
                    accounts.add(account);
                }
            return accounts;
            } catch (Exception e) {
                // TODO: handle exception
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
		throw new UnsupportedOperationException("Unimplemented method 'getAllAccountWithUserId'");
	}

	@Override
	public Account getAccountWithId(int id) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement(findAccountById);
            try (ResultSet resultSet = stmt.executeQuery()){
                if (resultSet.next()){
                    return mapToAccount(resultSet);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // TODO: Return exeptionm instead of null but for now it'll work
        return null;
	}

    private Account mapToAccount(ResultSet resultSet){
        UserDao userDao = new UserDao();
        try {
            int userId = resultSet.getInt("user_id");
            User user = userDao.getUserWithId(userId);
            String account_name = resultSet.getString("account_name");
            String account_type = resultSet.getString("account_type");
            double balance = resultSet.getDouble("balance");
            switch (account_type) {
                case "GeneralAccount":
                    return new GeneralAccount(account_name, user, balance);
                case "SavingsAccount":
                    return new SavingsAccount(account_name, user, balance);
                case "InvestimentsAccount":
                    return new InvestmentsAccount(account_name, user, balance);
                default:
                    break;
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        // TODO: Return exeptionm instead of null but for now it'll work
        return null;
    }


}
