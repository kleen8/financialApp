package nl.sogyo.financialApp;

import java.util.List;

public interface ITransactionDAO {
    void save(TransactionDTO transactionDTO, int accountId);
    TransactionDTO getTransactionDTOWitId(int transactionId);
    List<TransactionDTO> getAllTransactionWitId(int accountId);
    void updateTransaction(int transactionId, TransactionDTO transactionDTO);
    void deleteTransaction(int transactionId);
    List<TransactionDTO> getAllTransactionDTOWitAccId(int accountId);
}
