package nl.sogyo.financialApp;

import java.time.LocalDateTime;
import java.util.List;

public class GeneralAccount extends Account{

    
    public GeneralAccount(String accountName, User owner, double balance){
            super(accountName, owner, balance);
    }

    private boolean shouldProcessTransaction(Transaction transaction, LocalDateTime now){
        return transaction.getTimestamp().plus(1, transaction.getTimeInterval()).isBefore(now) ||
               transaction.getTimestamp().plus(1, transaction.getTimeInterval()).isEqual(now);
    }

    public void processRecurrentTransactions(){
        LocalDateTime now = LocalDateTime.now();
        for (Transaction transaction : transactions){
            if (transaction instanceof Income &&
                transaction.isRecurrent() && 
                shouldProcessTransaction(transaction, now)){
                deposit(transaction.getAmount());
                transaction.setTimestamp(now);
                System.out.println("deposit now balance after: " + getBalance());
            } else if (transaction instanceof Expense &&
                transaction.isRecurrent() && 
                shouldProcessTransaction(transaction, now)){
                withdraw(transaction.getAmount());
                transaction.setTimestamp(now);
                System.out.println("deposit now balance after: " + getBalance());
            }
        }
    }

    public List<Transaction> getTransactions(){
        return transactions;
    }

	@Override
	public boolean transfer(double amount, Account targerAccount) {
        if (amount > 0 && amount <= getBalance()){
            withdraw(amount);
            targerAccount.deposit(amount);
            return true;
        }
        return false;
    }

	@Override
	public AccountType getAccountType() {
        return AccountType.GENERAL;
	}


}
