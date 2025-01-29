package nl.sogyo.financialApp;

import java.time.LocalDateTime;

public class RecurrentTransactionDTO {
   private int id;
   private double amount;
   private double balance_before;
   private double balance_after;
   private String type;
   private int transaction_id;
   private LocalDateTime next_execution_date;
   private LocalDateTime last_execution_date;
   private Boolean is_completed;
   private LocalDateTime created_at;
   private LocalDateTime updated_at;
   private String category;
   private int account_id;
   private String timeInterval;

    public String getTimeInterval() {
        return timeInterval;
    }
    public void setTimeInterval(String timeInterval) {
        this.timeInterval = timeInterval;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public double getBalance_before() {
		return balance_before;
	}
	public void setBalance_before(double balance_before) {
		this.balance_before = balance_before;
	}
	public double getBalance_after() {
		return balance_after;
	}
	public void setBalance_after(double balance_after) {
		this.balance_after = balance_after;
	}
	public int getTransaction_id() {
		return transaction_id;
	}
	public void setTransaction_id(int transaction_id) {
		this.transaction_id = transaction_id;
	}
	public LocalDateTime getNext_execution_date() {
		return next_execution_date;
	}
	public void setNext_execution_date(LocalDateTime next_execution_date) {
		this.next_execution_date = next_execution_date;
	}
	public LocalDateTime getLast_execution_date() {
		return last_execution_date;
	}
	public void setLast_execution_date(LocalDateTime last_execution_date) {
		this.last_execution_date = last_execution_date;
	}
	public Boolean getIs_completed() {
		return is_completed;
	}
	public void setIs_completed(Boolean is_completed) {
		this.is_completed = is_completed;
	}
	public LocalDateTime getCreated_at() {
		return created_at;
	}
	public void setCreated_at(LocalDateTime created_at) {
		this.created_at = created_at;
	}
	public LocalDateTime getUpdated_at() {
		return updated_at;
	}
	public void setUpdated_at(LocalDateTime updated_at) {
		this.updated_at = updated_at;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String catergory) {
		this.category = catergory;
	}
	public int getAccount_id() {
		return account_id;
	}
	public void setAccount_id(int account_id) {
		this.account_id = account_id;
	}
}
