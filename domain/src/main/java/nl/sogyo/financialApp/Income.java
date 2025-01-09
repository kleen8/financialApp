package nl.sogyo.financialApp;

import java.time.*;
import java.time.temporal.ChronoUnit;


// TODO: Write factory for Income class

public class Income {

    private double amount;
    private String category;
    private LocalDateTime timestamp;
    private ChronoUnit timeInterval;
    private Boolean recurrent;


    public Income(double amount, ChronoUnit timeInterval, String category){
        this.amount = amount;
        this.timestamp = LocalDateTime.now();
        this.timeInterval = timeInterval;
        this.recurrent = true;
        this.category = category;
    }

    public Income(double amount, ChronoUnit timeInterval) {
        this.amount = amount;
        this.timestamp = LocalDateTime.now();
        this.timeInterval = timeInterval;
        this.recurrent = true;
        this.category = "";
    }

    public Income(double amount){
        this.amount = amount;
        this.timestamp = LocalDateTime.now();
        this.timeInterval = null;
        this.category = "";
        this.recurrent = false;
    }

    public Income(double amount, String category){
        this.amount = amount;
        this.timestamp = LocalDateTime.now();
        this.timeInterval = null;
        this.recurrent = false;
        this.category = category;
    }

    public Income(double amount, LocalDateTime timestamp, String category){
        this.amount = amount;
        this.timestamp = timestamp;
        this.timeInterval = null;
        this.recurrent = false;
        this.category = category;
    }

    public Income(double amount, LocalDateTime timestamp, String category, ChronoUnit timeInterval){
        this.amount = amount;
        this.timestamp = timestamp;
        this.timeInterval = timeInterval;
        this.recurrent = true;
        this.category = category;
    }

    public double getAmount(){
        return this.amount;
    }

    public LocalDateTime getTimestamp(){
        return this.timestamp;
    }

    public ChronoUnit getTimeInterval(){
        return this.timeInterval;
    }

    public String getCategory(){
        return this.category;
    }

    public Boolean isRecurrent(){
        return this.recurrent;
    }

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

	public void setTimeInterval(ChronoUnit timeInterval) {
		this.timeInterval = timeInterval;
	}

	public Boolean getRecurrent() {
		return recurrent;
	}

	public void setRecurrent(Boolean recurrent) {
		this.recurrent = recurrent;
	}
            
}
