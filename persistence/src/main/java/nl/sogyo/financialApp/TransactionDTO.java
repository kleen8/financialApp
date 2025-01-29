package nl.sogyo.financialApp;

import java.time.LocalDateTime;

public class TransactionDTO {
    private int accountId;
    private int transactionId;
	private String type;
    private String amount;
    private String category;
    private Boolean recurrent;
    private String timeInterval;
    private String timestamp;
    private String balance_before;
    private String balance_after;
    private LocalDateTime localdatetime;

    public String getBalance_before() {
		return balance_before;
	}

	public void setBalance_before(String balance_before) {
		this.balance_before = balance_before;
	}

	public String getBalance_after() {
		return balance_after;
	}

	public void setBalance_after(String balance_after) {
		this.balance_after = balance_after;
	}

	public LocalDateTime getLocaldatetime() {
		return localdatetime;
	}

	public void setLocaldatetime(LocalDateTime localdatetime) {
		this.localdatetime = localdatetime;
	}

	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public Boolean getRecurrent() {
		return recurrent;
	}
	public void setRecurrent(Boolean recurrent) {
		this.recurrent = recurrent;
	}
	public String getTimeInterval() {
		return timeInterval;
	}
	public void setTimeInterval(String timeInterval) {
		this.timeInterval = timeInterval;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

    public int getAccountId() {
        return accountId;
    }
    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }
	public int getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(int transactionId) {
		this.transactionId = transactionId;
	}

}
