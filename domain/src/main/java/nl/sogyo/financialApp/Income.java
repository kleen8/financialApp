package nl.sogyo.financialApp;

import java.time.*;
import java.time.temporal.ChronoUnit;


// TODO: Write factory for Income class

public class Income extends Transaction{

    public Income(double amount, ChronoUnit timeInterval, String category){
        super(amount, timeInterval, category);
    }

    public Income(double amount, ChronoUnit timeInterval) {
        super(amount, timeInterval);
    }

    public Income(double amount){
        super(amount);
    }

    public Income(double amount, String category){
        super(amount, category);
    }

    public Income(double amount, LocalDateTime timestamp, String category){
        super(amount, timestamp, category);
    }

    public Income(double amount, LocalDateTime timestamp, String category, ChronoUnit timeInterval){
        super(amount, timestamp, category, timeInterval);
    }
}
