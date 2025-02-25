package nl.sogyo.financialApp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Repository;

@Repository
public class TransactionDAO implements ITransactionDAO{

    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionDAO.class);

    /* Database table: 
     * id | type | amount | category | recurrent | time_interval | timestamp | account_id
     */

    private static final String saveTransactionQry = """
    INSERT INTO transactions (type, amount, balance_before, balance_after, category, recurrent, time_interval, timestamp, account_id) 
    VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);
    """;

    private static final String getAllTransactionsWithAccIdQry = """
    SELECT * FROM transactions WHERE account_id = ?;
    """;

    private static final String getAllTransactionsWithAccIdDescQry = """
    SELECT * FROM transactions WHERE account_id = ?
    ORDER BY timestamp DESC;
    """;


    private static final String updateTransactionQry = """
    UPDATE transactions
    SET type = ?,
    amount = ?,
    category = ?,
    recurrent = ?,
    time_interval = ?,
    timestamp = ?,
    account_id = ?,
    Where id = ?;
    """;

    private static final String deleteTransactionQry = """
    DELETE FROM transactions WHERE id = ?;
    """;

    private static final String getTransactionWithId = """
    SELECT * FROM transactions WHERE id = ?;
    """;
    private static final String getAllTransactionsQry = """
    SELECT * FROM transactions;
    """;

    private static final String getTransactionAmount = """
    SELECT amount FROM transactions
    WHERE id = ?;
    """;

    private static final String getTransactionTypeQry = """
    SELECT type FROM transactions
    WHERE id = ?;
    """;

    private static final String getAllRecurrentTransactionsQry = """
    SELECT * FROM transactions WHERE recurrent = true;
    """;

    @Override
    public String getTransactionType(int transactionId){
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(getTransactionTypeQry);
            stmt.setInt(1, transactionId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()){
                    return rs.getString("type");
                }
                throw new RuntimeException("Database error");
            } 
        } catch (SQLException e) {
            throw new RuntimeException("Database error");
        }
    }

    @Override
    public List<TransactionDTO> getTransactionByIdDescOrd(int accountId){
        List<TransactionDTO> transactionDTOs = new ArrayList<TransactionDTO>();
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(getAllTransactionsWithAccIdDescQry);
            stmt.setInt(1, accountId);
            try(ResultSet rs = stmt.executeQuery()){
                while (rs.next()) {
                    TransactionDTO transaction = mapToTransactionDTO(rs);
                    transactionDTOs.add(transaction);
                }
            }
           return transactionDTOs; 
        } catch (Exception e) {
            e.printStackTrace();
            return transactionDTOs;
        }
    }

    @Override
    public Double getTransactionAmount(int transactionId){
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(getTransactionAmount);
            stmt.setInt(1, transactionId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()){
                    return rs.getDouble("amount");
                }
                throw new RuntimeException("Database error");
            } 
        } catch (SQLException e) {
            throw new RuntimeException("Database error");
        }
    }

    @Override
    public List<TransactionDTO> getAllRecuTransactionDTO() {
        try (Connection connection = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement(getAllRecurrentTransactionsQry);
            try (ResultSet resultSet = stmt.executeQuery()){
                List<TransactionDTO> transactionDTOs = new ArrayList<TransactionDTO>();
                while (resultSet.next()){
                    TransactionDTO transactionDTO = mapToTransactionDTO(resultSet);
                    transactionDTOs.add(transactionDTO);
                }
                return transactionDTOs;
            } 
        } catch (SQLException e) {
            LOGGER.error("SQLException occured at {}: {}" , java.time.LocalDateTime.now(), e.getMessage());
            throw new RuntimeException("Database error occured");
        }
    }
    
    @Override
    public List<Transaction> getAllRecurrentTransactions() {
        List<Transaction> transactionsList = new ArrayList<Transaction>();
        try (Connection connection = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement(getAllRecurrentTransactionsQry);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Transaction transaction = mapToTransaction(rs);
                    transactionsList.add(transaction);
                }
            }
            return transactionsList;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Transaction getTransaction(int transactionId){
        try (Connection conn = DatabaseConnection.getConnection()){
            PreparedStatement stmt = conn.prepareStatement(getTransactionWithId);
            stmt.setInt(1, transactionId);
            try (ResultSet rs = stmt.executeQuery()){
                if (rs.next()){
                    return mapToTransaction(rs);
                }
            }
            throw new RuntimeException("No transaction found");
        } catch (SQLException e){
            e.printStackTrace();
            throw new RuntimeException("Error in getting transactions");
        }
    }

    public List<Transaction> getAllTransactions() {
        List<Transaction> transactionsList = new ArrayList<Transaction>();
        try (Connection connection = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement(getAllTransactionsQry);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Transaction transaction = mapToTransaction(rs);
                    transactionsList.add(transaction);
                }
            }
            return transactionsList;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void save(TransactionDTO transactionDTO, int accountId) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            connection.setAutoCommit(false);
            AccountDAO accountDAO = new AccountDAO();
            PreparedStatement stmt = connection.prepareStatement(saveTransactionQry);
            Double currentBalance = accountDAO.getAccountBalance(accountId);
            double delta = transactionDTO.getType().equalsIgnoreCase("income")
            ? Double.parseDouble(transactionDTO.getAmount())
            : -Double.parseDouble(transactionDTO.getAmount());
            Double newBalance = currentBalance + delta;
            stmt.setString(1, transactionDTO.getType());
            stmt.setDouble(2, Double.parseDouble(transactionDTO.getAmount()));
            stmt.setDouble(3, currentBalance);
            stmt.setDouble(4, newBalance);
            stmt.setString(5, transactionDTO.getCategory());
            stmt.setBoolean(6, transactionDTO.getRecurrent());
            stmt.setString(7, transactionDTO.getTimeInterval());
            stmt.setTimestamp(8, Timestamp.valueOf(transactionDTO.getLocaldatetime()));
            stmt.setInt(9, transactionDTO.getAccountId());
            stmt.executeUpdate();
            accountDAO.updateBalance(connection, accountId, delta);
            connection.commit();
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            LOGGER.error("SQLException occured at {}: {}" , java.time.LocalDateTime.now(), e.getMessage());
            throw new RuntimeException("Database error occured");
        }
    }

    @Override
    public List<TransactionDTO> getAllTransactionDTOWitAccId(int accountId) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement(getAllTransactionsWithAccIdQry);
            stmt.setInt(1, accountId);
            try (ResultSet resultSet = stmt.executeQuery()){
                List<TransactionDTO> transactionDTOs = new ArrayList<TransactionDTO>();
                while (resultSet.next()){
                    TransactionDTO transactionDTO = mapToTransactionDTO(resultSet);
                    transactionDTOs.add(transactionDTO);
                }
                return transactionDTOs;
            } 
        } catch (SQLException e) {
            LOGGER.error("SQLException occured at {}: {}" , java.time.LocalDateTime.now(), e.getMessage());
            throw new RuntimeException("Database error occured");
        }
    }

    @Override
    public void updateTransaction(int transactionId, TransactionDTO transactionDTO) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement(updateTransactionQry);
            stmt.setString(1, transactionDTO.getType());
            stmt.setDouble(2, Double.parseDouble(transactionDTO.getAmount()));
            stmt.setString(3, transactionDTO.getCategory());
            stmt.setBoolean(4, transactionDTO.getRecurrent());
            stmt.setString(5, transactionDTO.getTimeInterval());
            stmt.setTimestamp(6, transformISOtoLocalDateTime(transactionDTO.getTimestamp()));
            stmt.setInt(7, transactionDTO.getAccountId());
            stmt.setInt(8, transactionId);
            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                return;
            } 
        } catch (SQLException e) {
            LOGGER.error("SQLException occured at {}: {}" , java.time.LocalDateTime.now(), e.getMessage());
            throw new RuntimeException("Database error occured");
        }
    }

    @Override
    public void deleteTransaction(int transactionId) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement(deleteTransactionQry);
            stmt.setInt(1, transactionId);
            int rowsDeleted = stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("SQLException occured at {}: {}" , java.time.LocalDateTime.now(), e.getMessage());
            throw new RuntimeException("Database error occured");
        }
    }

    @Override
    public TransactionDTO getTransactionDTOWitId(int transactionId){
        try (Connection connection = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement(getTransactionWithId);
            stmt.setInt(1, transactionId);
            try (ResultSet resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {
                    return mapToTransactionDTO(resultSet);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            LOGGER.error("SQLException occured at {}: {}" , java.time.LocalDateTime.now(), e.getMessage());
            throw new RuntimeException("Database error occured");
        }
        return null;
    }


    @Override
    public List<TransactionDTO> getAllTransactionWitId(int accountId){
        return null;
    }

    private Timestamp transformISOtoLocalDateTime(String isoTimeString){
        // Parse ISO 8601 to LocalDateTime
        DateTimeFormatter isoFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX");
        LocalDateTime localDateTime = LocalDateTime.parse(isoTimeString, isoFormatter);
        // Convert LocalDateTime to Timestamp
        Timestamp sqlTimestamp = Timestamp.valueOf(localDateTime);
        return sqlTimestamp;
    }

    private TransactionDTO mapToTransactionDTO(ResultSet resultSet) {
        try {
            TransactionDTO transactionDTO = new TransactionDTO();
            int id = resultSet.getInt("id");
            String type = resultSet.getString("type");
            double amount = resultSet.getDouble("amount");
            double currentBalance = resultSet.getDouble("balance_before");
            double balance_after = resultSet.getDouble("balance_after");
            String category = resultSet.getString("category");
            Boolean recurrent = resultSet.getBoolean("recurrent");
            String timeInterval = resultSet.getString("time_interval");
            String timestamp = resultSet.getString("timestamp");
            int accountId = resultSet.getInt("account_id");
            LocalDateTime newTime = resultSet.getTimestamp("timestamp").toLocalDateTime();
            transactionDTO.setAccountId(accountId);
            transactionDTO.setTransactionId(id);
            transactionDTO.setType(type);
            transactionDTO.setAmount(Double.toString(amount));
            transactionDTO.setCategory(category);
            transactionDTO.setRecurrent(recurrent);
            transactionDTO.setTimeInterval(timeInterval);
            transactionDTO.setTimestamp(timestamp);
            transactionDTO.setLocaldatetime(newTime);
            transactionDTO.setBalance_after(String.valueOf(balance_after));
            transactionDTO.setBalance_before(String.valueOf(currentBalance));
            return transactionDTO;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    private Transaction mapToTransaction(ResultSet resultSet) {
        try {
            String type = resultSet.getString("type").toLowerCase(); 
            double amount = resultSet.getDouble("amount");
            String category = resultSet.getString("category");
            Boolean recurrent = resultSet.getBoolean("recurrent");
            String timeInterval = resultSet.getString("time_interval");
            String timestamp = resultSet.getString("timestamp");
            LocalDateTime localDateTime = stringToLDTtransformer(timestamp) ;
            ChronoUnit chronoUnit = stringToChronoUnit(timeInterval);
            switch (type) {
                case "income":
                return new Income(amount, localDateTime, category, chronoUnit);
                case "expense":
                return new Expense(amount, localDateTime, category, chronoUnit);
                default:
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }



    private LocalDateTime stringToLDTtransformer(String timestamp){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ssX");
        OffsetDateTime offsetDateTime = OffsetDateTime.parse(timestamp, formatter);
        LocalDateTime dateTime = offsetDateTime.toLocalDateTime();
        return dateTime;
    }

    private ChronoUnit stringToChronoUnit(String timeInterval){
        if (timeInterval != null){
            switch(timeInterval.toLowerCase()){
                case "daily":
                return ChronoUnit.DAYS;
                case "weekly":
                return ChronoUnit.WEEKS;
                case "monthly":
                return ChronoUnit.MONTHS;
                case "yearly":
                return ChronoUnit.YEARS;
                default:
                throw new IllegalArgumentException("Invalid time interval");
            }
        } else {
            return null;
        }
    }

}
