package nl.sogyo.financialApp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public class TransactionDAO implements ITransactionDAO{
    
    /* Database table: 
     * id | type | amount | category | recurrent | time_interval | timestamp | account_id
     */

    private static final String saveTransactionQry = """
    INSERT INTO transactions ( type, amount, category, recurrent, time_interval, timestamp, account_id) 
    VALUES (?, ?, ?, ?, ?, ?, ?);
    """;

    private static final String getAllTransactionsWithAccIdQry = """
    SELECT * FROM transactions WHERE account_id = ?;
    """;

	@Override
	public void save(TransactionDTO transactionDTO) {
        System.out.println("Trying to save a transaction, in the DAO");
        try (Connection connection = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement(saveTransactionQry);
            stmt.setString(1, transactionDTO.getType());
            stmt.setDouble(2, Double.parseDouble(transactionDTO.getAmount()));
            stmt.setString(3, transactionDTO.getCategory());
            stmt.setBoolean(4, transactionDTO.getRecurrent());
            stmt.setString(5, transactionDTO.getTimeInterval());
            System.out.println(transactionDTO.getTimestamp());
            stmt.setTimestamp(6,transformISOtoLocalDateTime(transactionDTO.getTimestamp()));
            stmt.setInt(7, transactionDTO.getAccountId());
            stmt.executeUpdate();
            System.out.println("Transaction saved in DAO");
        } catch (Exception e) {
            e.printStackTrace();
        }
	}

	@Override
    public List<TransactionDTO> getAllTransactionWitAccId(int accountId) {
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
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
	}

	private TransactionDTO mapToTransactionDTO(ResultSet resultSet) {
        try {
            TransactionDTO transactionDTO = new TransactionDTO();
            String type = resultSet.getString("type");
            double amount = resultSet.getDouble("amount");
            String category = resultSet.getString("category");
            Boolean recurrent = resultSet.getBoolean("recurrent");
            String timeInterval = resultSet.getString("time_interval");
            String timestamp = resultSet.getString("timestamp");
            transactionDTO.setType(type);
            transactionDTO.setAmount(Double.toString(amount));
            transactionDTO.setCategory(category);
            transactionDTO.setRecurrent(recurrent);
            transactionDTO.setTimeInterval(timeInterval);
            transactionDTO.setTimestamp(timestamp);
            return transactionDTO;
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return null;
	}

	@Override
	public void updateTransaction(int transactionId, Transaction transaction) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'updateTransaction'");
	}

	@Override
	public void deleteTransaction(int transactionId) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'deleteTransaction'");
	}

    @Override
    public Transaction getTransactionWitId(int transactionId){
        return null;
    }


    @Override
    public List<Transaction> getAllTransactionWitId(int accountId){
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
}
