package nl.sogyo.financialApp;

public interface IAccount {
    void deposit(double amount);
    void withdraw(double amount);
    double getBalance();
    void transfer(IAccount targetAccount, double amount);
}
