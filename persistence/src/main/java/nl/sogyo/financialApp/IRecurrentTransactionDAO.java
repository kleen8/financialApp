package nl.sogyo.financialApp;

import java.sql.Connection;
import java.util.List;

public interface IRecurrentTransactionDAO {
    void saveInDb(TransactionDTO transactionDTO);
    List<RecurrentTransactionDTO> getAllRecurrentTransactionDTOs();
    List<RecurrentTransactionDTO> getAllRecurrentTransactionDTOsId(int transactionId);
    Boolean checkIfRecurringTransactionExistsDatePassed(int transactionId);
    void save(TransactionDTO transactionDTO);
    void updateRecTransIsComplete(Connection conn, int recurrentTransId);
    void saveRecTrans(Connection conn, TransactionDTO transactionDTO);
    Boolean checkIfRecurringTransactionExists(int transactionId);
    List<TransactionDTO> getRecTransForAccInTransDTO(int account_id);
}

