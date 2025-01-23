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

public class RecurrentTransactionDAO implements IRecurrentTransactionDAO {

    private static final String saveRecurringTransactionQry = """
    INSERT INTO recurring_transactions
    (amount, transaction_id, execution_count, next_execution_date, last_execution_date)
    VALUES (?, ?, ?, ?, ?);
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
            System.out.println("Transaction already exists so we have to wait for it to be completed");
        } catch (Exception e) {
            e.printStackTrace();
        } 
    }

    @Override
    public void saveRecTrans(TransactionDTO transactionDTO){
        try (Connection conn = DatabaseConnection.getConnection()) {
            insertUpdatedRecurringTransaction(conn, transactionDTO);
            System.out.println("Transaction already exists so we have to wait for it to be completed");
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
                    System.out.println("Updated the database with a recurring transaction and set the old transaction to be completed");
                }
            } else {
                System.out.println("Inserted a new recurring transaction");
                insertNewRecurringTransaction(conn, transactionDTO);
            }
            System.out.println("Transaction already exists so we have to wait for it to be completed");
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
            //trns.setBalance_before(rs.getDouble("balance_before"));
            //trns.setBalance_after(rs.getDouble("balance_after"));
            trns.setTransaction_id(rs.getInt("transaction_id"));
            trns.setNext_execution_date(rs.getTimestamp("next_execution_date").toLocalDateTime());
            trns.setLast_execution_date(rs.getTimestamp("last_execution_date").toLocalDateTime());
            trns.setIs_completed(rs.getBoolean("is_completed"));
            trns.setCreated_at(rs.getTimestamp("created_at").toLocalDateTime());
            trns.setUpdated_at(rs.getTimestamp("updated_at").toLocalDateTime());
            return trns;
        } catch (Exception e) {
            e.getMessage();
            return null;
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
    public void updateRecTransIsComplete(int recurrentTransId){
        try (Connection conn = DatabaseConnection.getConnection()) {
            try (PreparedStatement updateStmt = conn.prepareStatement(updateEntryThatItIsDoneQry)){
                updateStmt.setInt(1, recurrentTransId);
                updateStmt.executeUpdate();
            }
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
                if ((nextExecutionDate.isBefore(LocalDateTime.now()) || nextExecutionDate.isEqual(LocalDateTime.now()) 
                && !rs.getBoolean("is_completed"))){
                    int recId = rs.getInt("id");
                    try (PreparedStatement updateStmt = conn.prepareStatement(updateEntryThatItIsDoneQry)){
                        updateStmt.setInt(1, recId);
                        updateStmt.executeUpdate();
                    }
                    System.out.println("Recurring transaction date has passed and is set to be completed");
                    return nextExecutionDate;
                }
            }
            return null;
        }
    }

    private void insertRecurringTransaction(Connection conn, TransactionDTO transactionDTO) throws Exception {
        try (PreparedStatement stmt = conn.prepareStatement(saveRecurringTransactionQry)) {
            stmt.setDouble(1, Double.parseDouble(transactionDTO.getAmount()));
            stmt.setInt(2, transactionDTO.getTransactionId());
            stmt.setInt(3, 0);
            LocalDateTime nextExecutionDate = calculateNextExecutionDateInDb(
                transactionDTO.getTimestamp(), transactionDTO.getTimeInterval());
            System.out.println("In insert next exec date: " + nextExecutionDate.format(
                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));
            stmt.setTimestamp(4, Timestamp.valueOf(nextExecutionDate));
            LocalDateTime nextExecutionDate2 = LocalDateTime.parse(transactionDTO.getTimestamp());
            transactionDTO.setTimestamp(nextExecutionDate2.format(
                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));
            stmt.setTimestamp(5, Timestamp.valueOf(nextExecutionDate2));
            stmt.executeUpdate();
        }
    }

    private void insertUpdatedRecurringTransaction(Connection conn, TransactionDTO transactionDTO) throws Exception {
        try (PreparedStatement stmt = conn.prepareStatement(saveRecurringTransactionQry)) {
            stmt.setDouble(1, Double.parseDouble(transactionDTO.getAmount()));
            stmt.setInt(2, transactionDTO.getTransactionId());
            stmt.setInt(3, 0);
            LocalDateTime nextExecutionDate = updCalculateNextExecutionDate(
                transactionDTO.getTimestamp(), transactionDTO.getTimeInterval());
            System.out.println("In insert next exec date: " + nextExecutionDate.format(
                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));
            System.out.println(LocalDateTime.now().toString());
            LocalDateTime localDateTime = LocalDateTime.now();
            System.out.println("local date time with ofset is: " + localDateTime.toString());
            stmt.setTimestamp(4, Timestamp.valueOf(nextExecutionDate));
            stmt.setTimestamp(5, Timestamp.valueOf(localDateTime));
            stmt.executeUpdate();
        }
    }

    private void insertNewRecurringTransaction(Connection conn, TransactionDTO transactionDTO) throws Exception {
        try (PreparedStatement stmt = conn.prepareStatement(saveRecurringTransactionQry)) {
            stmt.setDouble(1, Double.parseDouble(transactionDTO.getAmount()));
            stmt.setInt(2, transactionDTO.getTransactionId());
            stmt.setInt(3, 0);
            LocalDateTime nextExecutionDate = calculateNextExecutionDate(
                transactionDTO.getTimestamp(), transactionDTO.getTimeInterval());
            System.out.println("In insert next exec date: " + nextExecutionDate.format(
                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));
            System.out.println(LocalDateTime.now().toString());
            OffsetDateTime offsetDateTime = OffsetDateTime.parse(transactionDTO.getTimestamp(), 
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ssX"));
            LocalDateTime localDateTime = offsetDateTime.toLocalDateTime();
            System.out.println("local date time with ofset is: " + localDateTime.toString());
            stmt.setTimestamp(4, Timestamp.valueOf(nextExecutionDate));
            stmt.setTimestamp(5, Timestamp.valueOf(localDateTime));
            stmt.executeUpdate();
        }
    }

    private LocalDateTime updCalculateNextExecutionDate(String timestamp, String timeInterval){
        System.out.println(timestamp);
        LocalDateTime originalDateTime = LocalDateTime.parse(timestamp, 
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
        ChronoUnit chronoUnit = mapTimeIntervalToChronoUnit(timeInterval);
        return originalDateTime.plus(1, chronoUnit);
    }

    private LocalDateTime calculateNextExecutionDate(String timestamp, String timeInterval){
        LocalDateTime originalDateTime = LocalDateTime.parse(timestamp, 
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ssX"));
        ChronoUnit chronoUnit = mapTimeIntervalToChronoUnit(timeInterval);
        return originalDateTime.plus(1, chronoUnit);
    }

    private LocalDateTime calculateNextExecutionDateInDb(String timestamp, String timeInterval){
        System.out.println("Timestamp in calc exec in db: " + timestamp);
        LocalDateTime originalDateTime = LocalDateTime.parse(timestamp, 
            DateTimeFormatter.ISO_LOCAL_DATE_TIME).withSecond(0);
        originalDateTime = originalDateTime.withHour(0).withMinute(0).withSecond(0);
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
