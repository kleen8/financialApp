package nl.sogyo.financialApp;

import java.util.List;

public class PersistenceMain {
    public static void main(String[] args) {
        UserDao userDao = new UserDao();
        List<User> users = userDao.getAllUsers();

        for (User user : users){
            System.out.println(user);
        }
    }
}
