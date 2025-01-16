package nl.sogyo.financialApp;


import java.time.*;
import java.time.temporal.ChronoUnit;

public abstract class Transaction {

    private double amount;
    private String category;
    private LocalDateTime timestamp;
    private ChronoUnit timeInterval;
    private Boolean recurrent;

    public Transaction(double amount, ChronoUnit timeInterval, String category){
        this.amount = amount;
        this.timestamp = LocalDateTime.now();
        this.timeInterval = timeInterval;
        this.recurrent = true;
        this.category = category;
    }

    public Transaction(double amount, ChronoUnit timeInterval) {
        this.amount = amount;
        this.timestamp = LocalDateTime.now();
        this.timeInterval = timeInterval;
        this.recurrent = true;
        this.category = "";
    }

    public Transaction(double amount){
        this.amount = amount;
        this.timestamp = LocalDateTime.now();
        this.timeInterval = null;
        this.category = "";
        this.recurrent = false;
    }

    public Transaction(double amount, String category){
        this.amount = amount;
        this.timestamp = LocalDateTime.now();
        this.timeInterval = null;
        this.recurrent = false;
        this.category = category;
    }

    public Transaction(double amount, LocalDateTime timestamp, String category){
        this.amount = amount;
        this.timestamp = timestamp;
        this.timeInterval = null;
        this.recurrent = false;
        this.category = category;
    }

    public Transaction(double amount, LocalDateTime timestamp, String category, ChronoUnit timeInterval){
        this.amount = amount;
        this.timestamp = timestamp;
        this.timeInterval = timeInterval;
        this.recurrent = true;
        this.category = category;
    }

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

	public ChronoUnit getTimeInterval() {
		return timeInterval;
	}

	public void setTimeInterval(ChronoUnit timeInterval) {
		this.timeInterval = timeInterval;
	}

	public Boolean isRecurrent() {
		return recurrent;
	}

	public void setRecurrent(Boolean recurrent) {
		this.recurrent = recurrent;
	}
}
