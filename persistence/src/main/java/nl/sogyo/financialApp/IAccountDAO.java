package nl.sogyo.financialApp;

import java.util.List;

public interface IAccountDAO {
    void save(Account account);
    void delete(Account account);
    void update(Account account);
    List<Account> getAllAccountWithUserId(int id);
}
