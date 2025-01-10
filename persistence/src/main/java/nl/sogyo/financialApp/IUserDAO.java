package nl.sogyo.financialApp;

import java.util.HashMap;
import java.util.List;

public interface IUserDAO {
    void save(User user, String password);
    User getUserWithEmail(String email);
    User getUserWithId(int id);
    List<User> findAll();
    void update(User user);
    void delete(String email);
    boolean doesUserExist(String email);
    HashMap<String, String> loginUser(String email, String password);
    int getUserIdByEmail(String email);
}
