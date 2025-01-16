package nl.sogyo.financialApp;

import java.util.List;

public interface ITransactionDAO {
    void save(Transaction transaction, int accountId);
    Transaction getTransactionWitId(int transactionId);
    List<Transaction> getAllTransactionWitId(int accountId);
    void updateTransaction(int transactionId, Transaction transaction);
    void deleteTransaction(int transactionId);
}
