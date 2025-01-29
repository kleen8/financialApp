package nl.sogyo.financialApp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public abstract class Account {

    private String accountName;
    private double balance;
    private User owner;
    protected List<Transaction> transactions = new ArrayList<Transaction>();
    
    public Account(String accountName, User owner, double balance){
        this.accountName = accountName;
        this.balance = balance;
        this.owner = owner;
    }

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }

    public String getAccountName(){
        return accountName;
    }

    public double getBalance(){
        return balance;
    }

    public User getOwner(){
        return owner;
    }
    
    public void deposit(double amount){
        if(amount > 0){
            balance+=amount;
        }
    }

    public void withdraw(double amount){
        if (amount > 0 && amount <= balance){
            balance-=amount;
        }
    }
   
    public abstract void processRecurrentTransactions();
    public abstract boolean transfer(double amount, Account targerAccount);
    public abstract AccountType getAccountType();
}
