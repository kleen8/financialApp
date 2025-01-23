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

	@Override
	public AccountType getAccountType() {
        return AccountType.SAVINGS;
	}

	@Override
	public void processRecurrentTransactions() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'processRecurrentTransactions'");
	}
}
