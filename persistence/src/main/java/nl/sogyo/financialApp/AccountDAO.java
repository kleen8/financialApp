package nl.sogyo.financialApp;

import java.sql.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class AccountDAO implements IAccountDAO{

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountDAO.class);

    private final String saveAccountQuery = """
    INSERT INTO accounts (user_id, account_type, account_name, balance)
    VALUES (?, ?, ?, ?);
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

    private final String updateAccountBalanceQry = """
    UPDATE accounts
    SET balance = balance + ?, updated_at = CURRENT_TIMESTAMP
    WHERE id = ?;
    """;
    private final String setAccountBalanceQry = """
    UPDATE accounts
    SET balance = ?, updated_at = CURRENT_TIMESTAMP
    WHERE id = ?;
    """;

    private final String getAccountBalaceQry = """
    SELECT balance FROM accounts WHERE id = ?;
    """;

    private final String saveAccountQueryReturnId = """
    INSERT INTO accounts (user_id, account_type, account_name, balance)
    VALUES (?, ?, ?, ?)
    RETURNING id;
    """;

    @Override
    public int saveAndReturnId(Account account, int userId) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement(saveAccountQueryReturnId, Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, userId);
            stmt.setString(2, account.getAccountType().getTypeName());
            stmt.setString(3, account.getAccountName());
            stmt.setDouble(4, account.getBalance());
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0){
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()){
                    return rs.getInt(1);
                }
            }
            throw new RuntimeException("Databse error occured");
        } catch (SQLException e) {
            LOGGER.error("SQLException occured at {}: {}" , java.time.LocalDateTime.now(), e.getMessage());
            throw new RuntimeException("Databse error occured");
        }
    }

    @Override
    public void save(Account account, int userId) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement(saveAccountQuery);
            stmt.setInt(1, userId);
            stmt.setString(2, account.getAccountType().getTypeName());
            stmt.setString(3, account.getAccountName());
            stmt.setDouble(4, account.getBalance());
            stmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error("SQLException occured at {}: {}" , java.time.LocalDateTime.now(), e.getMessage());
            throw new RuntimeException("Databse error occured");
        }
    }

    @Override
    public AccountDTO getAccountDTOWithId(int accountId){
        try (Connection conn = DatabaseConnection.getConnection()) {
           PreparedStatement stmt = conn.prepareStatement(findAccountById);
            stmt.setInt(1 , accountId);
            try (ResultSet rs = stmt.executeQuery()){
                if(rs.next()){
                    return mapToAccountDto(rs);
                }
                else { return null; }
            }
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void delete(int accountId) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement(deleteAccountWithId);
            stmt.setInt(1, accountId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error("SQLException occured at {}: {}" , java.time.LocalDateTime.now(), e.getMessage());
        }
    }

    @Override
    public void update(Account account, int accountId) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement(updateAccount);
            stmt.setString(1, account.getAccountType().getTypeName());
            stmt.setString(2, account.getAccountName());
            stmt.setDouble(3, account.getBalance());
            stmt.setInt(4, accountId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error("SQLException occured at {}: {}" , java.time.LocalDateTime.now(), e.getMessage());
        }
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public List<Account> getAllAccountWithUserId(int id) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement(findAccountsByUserId);
            stmt.setInt(1, id);
            try (ResultSet resultSet = stmt.executeQuery()) {
                List<Account> accounts = new ArrayList<Account>();
                while (resultSet.next()){
                    Account account = mapToAccount(resultSet);
                    accounts.add(account);
                }
                return accounts;
            } catch (Exception e) {
                e.printStackTrace();
                LOGGER.error("Exception occured at {}: {}" , java.time.LocalDateTime.now(), e.getMessage());
            }
        } catch (SQLException e) {
            LOGGER.error("SQLException occured at {}: {}" , java.time.LocalDateTime.now(), e.getMessage());
            return null;
        }
        return null;
    }

    @Override
    public Account getAccountWithId(int id) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement(findAccountById);
            stmt.setInt(1, id);
            try (ResultSet resultSet = stmt.executeQuery()){
                if (resultSet.next()){
                    return mapToAccount(resultSet);
                }
            }
            return null;
        } catch (SQLException e) {
            LOGGER.error("Exception occured at {}: {}" , java.time.LocalDateTime.now(), e.getMessage());
            throw new RuntimeException("Database error occured");
        }
    }

    @Override
    public List<AccountDTO> getAccountDTOsWithUserId(int userId){
        try (Connection connection = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement(findAccountsByUserId);
            stmt.setInt(1, userId);
            try (ResultSet resultSet = stmt.executeQuery()) {
                List<AccountDTO> accountsDTOs = new ArrayList<AccountDTO>();
                while (resultSet.next()){
                    AccountDTO accountDto = mapToAccountDto(resultSet);
                    accountsDTOs.add(accountDto);
                }
                return accountsDTOs;
            } catch (Exception e) {
                LOGGER.error("Exception occured at {}: {}" , java.time.LocalDateTime.now(), e.getMessage());
                throw new RuntimeException("Server error occured");
            }
        } catch (SQLException e) {
            LOGGER.error("SQLException occured at {}: {}" , java.time.LocalDateTime.now(), e.getMessage());
            throw new RuntimeException("Database error occured");
        }
    }

    private Account mapToAccount(ResultSet resultSet){
        UserDAO userDao = new UserDAO();
        try {
            int userId = resultSet.getInt("user_id");
            User owner = userDao.getUserWithId(userId);
            String account_name = resultSet.getString("account_name");
            String account_typeStr = resultSet.getString("account_type");
            double balance = resultSet.getDouble("balance");
            AccountType accountType = AccountType.fromTypeName(account_typeStr);
            if (accountType == AccountType.GENERAL){
                return new GeneralAccount(account_name, owner, balance);
            } else if (accountType == AccountType.SAVINGS){
                return new SavingsAccount(account_name, owner, balance);
            } else if (accountType == AccountType.INVESTMENTS){
                return new InvestmentsAccount(account_name, owner, balance);
            }
            return null;
        } catch (Exception e){
            e.printStackTrace();
            LOGGER.error("SQLException occured at {}: {}" , java.time.LocalDateTime.now(), e.getMessage());
            return null;
        }
    }

    private AccountDTO mapToAccountDto(ResultSet resultSet){
        AccountDTO accountDTO = new AccountDTO();
        try {
            String account_name = resultSet.getString("account_name");
            String account_type = resultSet.getString("account_type");
            double balance = resultSet.getDouble("balance");
            int accountId = resultSet.getInt("id");
            int userId = resultSet.getInt("user_id");
            accountDTO.setUserId(userId);
            accountDTO.setBalance(balance);
            accountDTO.setAccountId(accountId);
            accountDTO.setAccountName(account_name);
            accountDTO.setAccountType(account_type);
            return accountDTO;
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("SQLException occured at {}: {}" , java.time.LocalDateTime.now(), e.getMessage());
            return null;
        }
    }

    @Override
    public void updateBalance(int accountId, double delta) {
        try (Connection conn = DatabaseConnection.getConnection()) {
        try (PreparedStatement stmt = conn.prepareStatement(setAccountBalanceQry)){
            stmt.setDouble(1, delta);
            stmt.setInt(2, accountId);
            stmt.executeUpdate();
            }
        } catch (Exception e) {
            LOGGER.error("SQLException occured at {}: {}" , java.time.LocalDateTime.now(), e.getMessage());
            throw new RuntimeException("Error updating account balance" + e.getMessage());
        }
    }

    @Override
    public void setAccountBalance(Connection connection, int accountId, double delta) {
        try (PreparedStatement stmt = connection.prepareStatement(setAccountBalanceQry)){
            stmt.setDouble(1, delta);
            stmt.setInt(2, accountId);
            stmt.executeUpdate();
        } catch (Exception e) {
            LOGGER.error("SQLException occured at {}: {}" , java.time.LocalDateTime.now(), e.getMessage());
            throw new RuntimeException("Error updating account balance" + e.getMessage());
        }
    }

    @Override
    public void updateBalance(Connection connection, int accountId, double delta) {
        try (PreparedStatement stmt = connection.prepareStatement(updateAccountBalanceQry)){
            stmt.setDouble(1, delta);
            stmt.setInt(2, accountId);
            stmt.executeUpdate();
        } catch (Exception e) {
            LOGGER.error("SQLException occured at {}: {}" , java.time.LocalDateTime.now(), e.getMessage());
            throw new RuntimeException("Error updating account balance" + e.getMessage());
        }
    }

	@Override
	public Double getAccountBalance(int accountId) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement(getAccountBalaceQry);
            stmt.setInt(1, accountId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("balance");
                }
                throw new RuntimeException("Result set contains no balance");
            } catch (Exception e) {

                LOGGER.error("Exception occured at {}: {}" , java.time.LocalDateTime.now(), e.getMessage());
                throw new RuntimeException("Can't find the balance" + e.getMessage());
            }
        } catch (SQLException e) {
            LOGGER.error("SQLException occured at {}: {}" , java.time.LocalDateTime.now(), e.getMessage());
            throw new RuntimeException("database error occured");
        }
	}

}
