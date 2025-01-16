package nl.sogyo.financialApp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
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

	@Override
	public void save(Transaction transaction, int accountId) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement(saveTransactionQry);
            String type = transaction instanceof Expense ? "expense" : "income";
            String timeInterval = transaction.getTimeInterval() != null ? transaction.getTimeInterval().name().toLowerCase() : null;
            stmt.setString(1, type);
            stmt.setDouble(2, transaction.getAmount());
            stmt.setString(3, transaction.getCategory());
            stmt.setBoolean(4, transaction.isRecurrent());
            stmt.setString(5, timeInterval);
            stmt.setTimestamp(6, Timestamp.valueOf(transaction.getTimestamp()));
            stmt.setInt(7, accountId);
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }

	}

	@Override
	public Transaction getTransactionWitId(int transactionId) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'getTransactionWitId'");
	}

	@Override
	public List<Transaction> getAllTransactionWitId(int accountId) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'getAllTransactionWitId'");
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

    
}
