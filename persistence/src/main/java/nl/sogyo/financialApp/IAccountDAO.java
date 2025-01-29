package nl.sogyo.financialApp;

import java.sql.Connection;
import java.util.List;

public interface IAccountDAO {
    void save(Account account, int userId);
    int saveAndReturnId(Account account, int userId);
    void delete(int accountId);
    void update(Account account, int accountId);
    List<Account> getAllAccountWithUserId(int id);
    List<AccountDTO> getAccountDTOsWithUserId(int userId);
    Account getAccountWithId(int accountId);
    void updateBalance(int accountId, double delta);
    Double getAccountBalance(int accountId);
    AccountDTO getAccountDTOWithId(int accountId);
    void updateBalance(Connection connection, int accountId, double delta); 
}
