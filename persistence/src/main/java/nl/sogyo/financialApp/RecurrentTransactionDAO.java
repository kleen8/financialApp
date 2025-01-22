package nl.sogyo.financialApp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class RecurrentTransactionDAO implements IRecurrentTransactionDAO {

    private static final String saveRecurringTransactionQry = """
    INSERT INTO recurring_transactions
    (amount, transaction_id, execution_count, next_execution_date, last_execution_date)
    VALUES (?, ?, ?, ?, ?);
    """;

    private static final String checkRecurringTransactionQry = """
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
            Boolean transactionExists = checkIfRecurringTransactionExists(conn, transactionDTO.getTransactionId());
            if (transactionExists){
                LocalDateTime datePassed = checkIfRecurringTransactionExistsDate(conn, transactionDTO.getTransactionId());
                if (datePassed != null) {
                    transactionDTO.setTimestamp(datePassed.toString());
                    LocalDateTime nextExecutionDate2 = LocalDateTime.parse(transactionDTO.getTimestamp());
                    transactionDTO.setTimestamp(nextExecutionDate2.format(
                        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));
                    insertRecurringTransaction(conn, transactionDTO);
                    System.out.println("Updated the database with a recurring transaction and set the old transaction to be completed");
                }
            } else {
                insertNewRecurringTransaction(conn, transactionDTO);
                System.out.println("Inserted a new recurring transaction");
            }
            System.out.println("Transaction already exists so we have to wait for it to be completed");
        } catch (Exception e) {
            e.printStackTrace();
        } 
    }

    private Boolean checkIfRecurringTransactionExists(Connection conn, int transactionId) throws Exception {
        try (PreparedStatement checkStmt = conn.prepareStatement(checkRecurringTransactionQry)) {   
            checkStmt.setInt(1, transactionId);
            ResultSet rs = checkStmt.executeQuery();
            if (rs != null && rs.next()){
                return true;
            }
            return false;
        }
    }

    private LocalDateTime checkIfRecurringTransactionExistsDate(Connection conn, int transactionId) throws Exception {
        try (PreparedStatement checkStmt = conn.prepareStatement(checkRecurringTransactionQry)) {   
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
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ssXXX"));
            LocalDateTime localDateTime = offsetDateTime.toLocalDateTime();
            stmt.setTimestamp(4, Timestamp.valueOf(nextExecutionDate));
            stmt.setTimestamp(5, Timestamp.valueOf(localDateTime));
            stmt.executeUpdate();
        }
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
