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

import org.springframework.stereotype.Repository;

@Repository
public class RecurrentTransactionDAO implements IRecurrentTransactionDAO {

    private static final String saveRecurringTransactionQry = """
    INSERT INTO recurring_transactions
    (amount, balance_before, balance_after, type, transaction_id, next_execution_date, last_execution_date, category, account_id)
    VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);
    """;

    private static final String getRecurringTransactionQryId = """
    SELECT * FROM recurring_transactions
    WHERE transaction_id = ?;
    """;

    private static final String getRecurringTransactionQry = """
    SELECT * FROM recurring_transactions;
    """;

    private static final String checkRecurringTransactionDTOQry = """
    SELECT * FROM recurring_transactions
    WHERE transaction_id = ?;
    """;

    private static final String getRecTransForAccId = """
    SELECT * FROM recurring_transactions
    WHERE account_id = ? AND is_completed = true;
    """;

    private static final String updateEntryThatItIsDoneQry = """
    UPDATE recurring_transactions
    SET is_completed = true,
    updated_at = CURRENT_TIMESTAMP
    WHERE id = ?;
    """;

    
    @Override
    public void save(TransactionDTO transactionDTO){
        try (Connection conn = DatabaseConnection.getConnection()) {
            insertNewRecurringTransaction(conn, transactionDTO);
        } catch (Exception e) {
            e.printStackTrace();
        } 
    }

    @Override
    public void saveRecTrans(Connection conn, TransactionDTO transactionDTO){
        try {
            insertUpdatedRecurringTransaction(conn, transactionDTO);
        } catch (Exception e) {
           e.printStackTrace(); 
        }
    }

    @Override
    public void saveInDb(TransactionDTO transactionDTO){
        try (Connection conn = DatabaseConnection.getConnection()) {
            Boolean transactionExists = checkIfRecurringTransactionExistsDb(conn, transactionDTO.getTransactionId());
            if (transactionExists){
                LocalDateTime datePassed = checkIfRecurringTransactionExistsDateInDB(conn, transactionDTO.getTransactionId());
                if (datePassed != null) {
                    transactionDTO.setTimestamp(datePassed.toString());
                    LocalDateTime nextExecutionDate2 = LocalDateTime.parse(transactionDTO.getTimestamp());
                    transactionDTO.setTimestamp(nextExecutionDate2.format(
                        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));
                    insertRecurringTransaction(conn, transactionDTO);
                }
            } else {
                insertNewRecurringTransaction(conn, transactionDTO);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } 
    }
    
    @Override
    public List<RecurrentTransactionDTO> getAllRecurrentTransactionDTOsId(int transactionId){
        List<RecurrentTransactionDTO> transactionDTOs = new ArrayList<RecurrentTransactionDTO>();
        try (Connection conn = DatabaseConnection.getConnection()) {
            try(PreparedStatement stmt = conn.prepareStatement(getRecurringTransactionQryId)) {
                stmt.setInt(1, transactionId);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()){
                    RecurrentTransactionDTO trns = mapToTransactionDTO(rs);
                    transactionDTOs.add(trns);
                }
                if (transactionDTOs != null && transactionDTOs.size() > 0){
                    return transactionDTOs;
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<TransactionDTO> getRecTransForAccInTransDTO(int account_id){
            List<TransactionDTO> transactionDTOs = new ArrayList<TransactionDTO>();
        try (Connection conn = DatabaseConnection.getConnection()) {
            try(PreparedStatement stmt = conn.prepareStatement(getRecTransForAccId)){
                stmt.setInt(1, account_id);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()){
                    TransactionDTO trns = mapRecTransToTransDTO(rs);
                    transactionDTOs.add(trns);
                }
                return transactionDTOs;            
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return transactionDTOs;
        }
    }

    @Override
    public List<RecurrentTransactionDTO> getAllRecurrentTransactionDTOs(){
        List<RecurrentTransactionDTO> transactionDTOs = new ArrayList<RecurrentTransactionDTO>();
        try (Connection conn = DatabaseConnection.getConnection()) {
            try(PreparedStatement stmt = conn.prepareStatement(getRecurringTransactionQry)) {
                ResultSet rs = stmt.executeQuery();
                while (rs.next()){
                    RecurrentTransactionDTO trns = mapToTransactionDTO(rs);
                    System.out.println("from Rec Trans print rec trans: " + trns.getAmount());
                    transactionDTOs.add(trns);
                }
                if (transactionDTOs != null && transactionDTOs.size() > 0){
                    return transactionDTOs;
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private RecurrentTransactionDTO mapToTransactionDTO(ResultSet rs){
        RecurrentTransactionDTO trns = new RecurrentTransactionDTO();
        try {
            trns.setId(rs.getInt("id"));
            trns.setAmount(rs.getDouble("amount"));
            trns.setType(rs.getString("type"));
            trns.setBalance_before(rs.getDouble("balance_before"));
            trns.setBalance_after(rs.getDouble("balance_after"));
            trns.setTransaction_id(rs.getInt("transaction_id"));
            trns.setNext_execution_date(rs.getTimestamp("next_execution_date").toLocalDateTime());
            trns.setLast_execution_date(rs.getTimestamp("last_execution_date").toLocalDateTime());
            trns.setIs_completed(rs.getBoolean("is_completed"));
            trns.setCreated_at(rs.getTimestamp("created_at").toLocalDateTime());
            trns.setUpdated_at(rs.getTimestamp("updated_at").toLocalDateTime());
            trns.setCategory(rs.getString("category"));
            trns.setAccount_id(rs.getInt("account_id"));
            return trns;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

    private TransactionDTO mapRecTransToTransDTO(ResultSet rs){
        TransactionDTO trns = new TransactionDTO();
        try {
            trns.setRecurrent(true);
            trns.setType(rs.getString("type"));
            trns.setAccountId(rs.getInt("account_id"));
            trns.setTransactionId(rs.getInt("transaction_id"));
            trns.setAmount(String.valueOf(rs.getDouble("amount")));
            trns.setCategory(rs.getString("category"));
            trns.setTimestamp(rs.getTimestamp("next_execution_date").toString());
            trns.setBalance_after(String.valueOf(rs.getDouble("balance_after")));
            trns.setBalance_before(String.valueOf(rs.getDouble("balance_before")));
            return trns;
        } catch (Exception e) {
            e.printStackTrace();
            return trns;
        }
    }
    
    @Override
    public Boolean checkIfRecurringTransactionExists(int transactionId) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            try (PreparedStatement checkStmt = conn.prepareStatement(checkRecurringTransactionDTOQry)) {   
                checkStmt.setInt(1, transactionId);
                ResultSet rs = checkStmt.executeQuery();
                if (rs != null && rs.next()){
                    return true;
                }
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private Boolean checkIfRecurringTransactionExistsDb(Connection conn, int transactionId) throws Exception {
        try (PreparedStatement checkStmt = conn.prepareStatement(checkRecurringTransactionDTOQry)) {   
            checkStmt.setInt(1, transactionId);
            ResultSet rs = checkStmt.executeQuery();
            if (rs != null && rs.next()){
                return true;
            }
            return false;
        }
    }

    public Boolean checkIfRecurringTransactionExistsDatePassed(int transactionId) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            try(PreparedStatement stmt = conn.prepareStatement(checkRecurringTransactionDTOQry)){
                stmt.setInt(1, transactionId);
                ResultSet rs = stmt.executeQuery();
                if (rs != null && rs.next()){
                    LocalDateTime nextExecutionDate = rs.getTimestamp("next_execution_date").toLocalDateTime();
                    if ((nextExecutionDate.isBefore(LocalDateTime.now()) || nextExecutionDate.isEqual(LocalDateTime.now()) 
                    && !rs.getBoolean("is_completed"))){
                        return true;
                    }
                }
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public void updateRecTransIsComplete(Connection conn, int recurrentTransId){
            try (PreparedStatement updateStmt = conn.prepareStatement(updateEntryThatItIsDoneQry)){
                updateStmt.setInt(1, recurrentTransId);
                updateStmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private LocalDateTime checkIfRecurringTransactionExistsDateInDB(Connection conn, int transactionId) throws Exception {
        try (PreparedStatement checkStmt = conn.prepareStatement(checkRecurringTransactionDTOQry)) {   
            checkStmt.setInt(1, transactionId);
            ResultSet rs = checkStmt.executeQuery();
            if (rs != null && rs.next()){
                LocalDateTime nextExecutionDate = rs.getTimestamp("next_execution_date").toLocalDateTime();
                if ((nextExecutionDate.isBefore(LocalDateTime.now()) 
                || nextExecutionDate.isEqual(LocalDateTime.now()) 
                && !rs.getBoolean("is_completed"))){
                    int recId = rs.getInt("id");
                    try (PreparedStatement updateStmt = conn.prepareStatement(updateEntryThatItIsDoneQry)){
                        updateStmt.setInt(1, recId);
                        updateStmt.executeUpdate();
                    }
                    return nextExecutionDate;
                }
            }
            return null;
        }
    }

    private void insertRecurringTransaction(Connection conn, TransactionDTO transactionDTO) throws Exception {
        try (PreparedStatement stmt = conn.prepareStatement(saveRecurringTransactionQry)) {
            stmt.setDouble(1, Double.parseDouble(transactionDTO.getAmount()));
            stmt.setDouble(2, Double.parseDouble(transactionDTO.getBalance_before()));
            stmt.setDouble(3, Double.parseDouble(transactionDTO.getBalance_after()));
            stmt.setString(4, transactionDTO.getType());
            stmt.setInt(5, transactionDTO.getTransactionId());
            LocalDateTime nextTime = transactionDTO.getLocaldatetime();
            LocalDateTime newNextTime = calculateNextExecutionDateInDb(nextTime
                , transactionDTO.getTimeInterval());
            stmt.setTimestamp(6, Timestamp.valueOf(newNextTime));
            LocalDateTime nextExecutionDate2 = nextTime;
            stmt.setTimestamp(7, Timestamp.valueOf(nextExecutionDate2));
            stmt.setString(8, transactionDTO.getCategory());
            stmt.setInt(9, transactionDTO.getAccountId());
            stmt.executeUpdate();
        }
    }

    private void insertUpdatedRecurringTransaction(Connection conn, TransactionDTO transactionDTO) throws Exception {
        try (PreparedStatement stmt = conn.prepareStatement(saveRecurringTransactionQry)) {
            stmt.setDouble(1, Double.parseDouble(transactionDTO.getAmount()));
            stmt.setDouble(2, Double.parseDouble(transactionDTO.getBalance_after()));
            stmt.setDouble(3, (Double.parseDouble(transactionDTO.getBalance_after()) 
                + Double.parseDouble(transactionDTO.getAmount())));
            stmt.setString(4, transactionDTO.getType());
            stmt.setInt(5, transactionDTO.getTransactionId());
            LocalDateTime nextTime = calculateNextExecutionDateInDb(transactionDTO.getLocaldatetime()
                , transactionDTO.getTimeInterval());
            LocalDateTime localDateTime = LocalDateTime.now();
            stmt.setTimestamp(6, Timestamp.valueOf(nextTime));
            stmt.setTimestamp(7, Timestamp.valueOf(localDateTime));
            stmt.setString(8, transactionDTO.getCategory());
            stmt.setInt(9, transactionDTO.getAccountId());
            stmt.executeUpdate();
        }
    }

    private void insertNewRecurringTransaction(Connection conn, TransactionDTO transactionDTO) throws Exception {
        try (PreparedStatement stmt = conn.prepareStatement(saveRecurringTransactionQry)) {
            stmt.setDouble(1, Double.parseDouble(transactionDTO.getAmount()));
            stmt.setDouble(2, Double.parseDouble(transactionDTO.getBalance_before()));
            stmt.setDouble(3, Double.parseDouble(transactionDTO.getBalance_after()));
            stmt.setString(4, transactionDTO.getType());
            stmt.setInt(5, transactionDTO.getTransactionId());
            LocalDateTime nextTime = calculateNextExecutionDateInDb(transactionDTO.getLocaldatetime()
                , transactionDTO.getTimeInterval());
            LocalDateTime localDateTime = transactionDTO.getLocaldatetime();
            stmt.setTimestamp(6, Timestamp.valueOf(nextTime));
            stmt.setTimestamp(7, Timestamp.valueOf(localDateTime));
            stmt.setString(8, transactionDTO.getCategory());
            stmt.setInt(9, transactionDTO.getAccountId());
            stmt.executeUpdate();
        }
    }

    private LocalDateTime calculateNextExecutionDateInDb(LocalDateTime timestamp, String timeInterval){
        LocalDateTime originalDateTime = timestamp;
        ChronoUnit chronoUnit = mapTimeIntervalToChronoUnit(timeInterval);
        return originalDateTime.plus(1, chronoUnit);
    }

    private ChronoUnit mapTimeIntervalToChronoUnit(String timeInterval) {
        switch (timeInterval.toLowerCase()) {
            case "daily":
            return ChronoUnit.DAYS;
            case "weekly":
            return ChronoUnit.WEEKS;
            case "monthly":
            return ChronoUnit.MONTHS;
            case "yearly":
            return ChronoUnit.YEARS;
            default:
            throw new IllegalArgumentException("Invalid time interval: " + timeInterval);
        }
    }

}
