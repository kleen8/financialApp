package nl.sogyo.financialApp;

public class SavingsAccount extends Account{

    public SavingsAccount(String accountName, User owner, double balance){
        super(accountName, owner, balance);
    }

	@Override
	public boolean transfer(double amount, Account targerAccount) {
        if (getOwner().equals(targerAccount.getOwner())){
            if (amount > 0 && amount <= getBalance()){
                withdraw(amount);
                targerAccount.deposit(amount);
                return true;
            }
        }
        return false;
	}
}
