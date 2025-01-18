package nl.sogyo.financialApp;

import java.util.List;

public interface IAccountDAO {
    void save(Account account, int userId);
    void delete(int accountId);
    void update(Account account, int accountId);
    List<Account> getAllAccountWithUserId(int id);
    List<AccountDTO> getAccountDTOsWithUserId(int userId);
    Account getAccountWithId(int id);
}
