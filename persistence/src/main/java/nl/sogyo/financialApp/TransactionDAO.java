package nl.sogyo.financialApp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.aspectj.weaver.ast.Instanceof;

public class TransactionDAO implements ITransactionDAO{
    
    /* Database table: 
     * id | type | amount | category | recurrent | time_interval | timestamp | account_id
     */

    private static final String saveTransactionQry = """
    INSERT INTO transaction ( type, amount, category, recurrent, time_interval, timestamp, account_id) 
    VALUES (?, ?, ?, ?, ?, ?, ?);
    """;

    private static final String getAllTransactionsWithAccIdQry = """
    SELECT * FROM transactions WHERE account_id = ?;
    """;

	@Override
	public void save(TransactionDTO transactionDTO) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement(saveTransactionQry);
            String type = transaction instanceof Expense ? "expense" : "income";
            String timeInterval = transaction.getTimeInterval() != null ? transaction.getTimeInterval().name().toLowerCase() : null;
            stmt.setString(1, transactionDTO.getType());
            stmt.setDouble(2, Double.parseDouble(transactionDTO.getAmount()));
            stmt.setString(3, transactionDTO.getCatergory());
            stmt.setBoolean(4, transactionDTO.getRecurrent());
            stmt.setString(5, transactionDTO.getTimeInterval());
            stmt.setTimestamp(6, Timestamp.valueOf(transactionDTO.getTimestamp()));
            stmt.setInt(7, transactionDTO.get);
            stmt.executeUpdate();
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
            String timeInterval = resultSet.getString("timeInterval");
            String timestamp = resultSet.getString("timestamp");
            transactionDTO.setType(type);
            transactionDTO.setAmount(Double.toString(amount));
            transactionDTO.setCatergory(category);
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

}
