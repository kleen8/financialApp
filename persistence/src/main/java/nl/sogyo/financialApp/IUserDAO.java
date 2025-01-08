package nl.sogyo.financialApp;

import java.util.List;

public interface IUserDAO {
    void save(User user, String password);
    User findByEmail(String email);
    List<User> findAll();
    void update(User user);
    void delete(String email);
}
