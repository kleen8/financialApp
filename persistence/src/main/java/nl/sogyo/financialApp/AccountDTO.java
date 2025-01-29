package nl.sogyo.financialApp;

public class AccountDTO {
    private int accountId;
	private String accountName;
    private String accountType;
	private double balance;
    private int userId;


	public AccountDTO(){}

    public AccountDTO(String accountName, String accountType, double balance, int accountId){
        this.accountName = accountName;
        this.accountType = accountType;
        this.balance = balance;
        this.accountId = accountId;
    }

    public AccountDTO(String accountName, String accountType, double balance){
        this.accountName = accountName;
        this.accountType = accountType;
        this.balance = balance;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
    public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	public String getAccountType() {
		return accountType;
	}
	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}
	public double getBalance() {
		return balance;
	}
	public void setBalance(double balance) {
		this.balance = balance;
	}

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }
}
