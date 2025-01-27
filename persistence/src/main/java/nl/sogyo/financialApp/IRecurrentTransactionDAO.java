package nl.sogyo.financialApp;

import java.util.List;

public interface IRecurrentTransactionDAO {
    void saveInDb(TransactionDTO transactionDTO);
    List<RecurrentTransactionDTO> getAllRecurrentTransactionDTOs();
    List<RecurrentTransactionDTO> getAllRecurrentTransactionDTOsId(int transactionId);
    Boolean checkIfRecurringTransactionExistsDatePassed(int transactionId);
    void save(TransactionDTO transactionDTO);
    void updateRecTransIsComplete(int recurrentTransId);
    void saveRecTrans(TransactionDTO transactionDTO);
    Boolean checkIfRecurringTransactionExists(int transactionId);
    List<TransactionDTO> getRecTransForAccInTransDTO(int account_id);
}

