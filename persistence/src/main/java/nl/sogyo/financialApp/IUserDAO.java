package nl.sogyo.financialApp;

import java.util.List;

public interface IUserDAO {
    void save(User user, String password);
    User findById(int id);
    List<User> findAll();
    void update(User user);
    void delete(int id);
}
