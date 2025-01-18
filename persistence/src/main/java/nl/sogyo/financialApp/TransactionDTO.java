package nl.sogyo.financialApp;

public class TransactionDTO {
    private int accountId;
	private String type;
    private String amount;
    private String category;
    private Boolean recurrent;
    private String timeInterval;
    private String timestamp;

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

}
