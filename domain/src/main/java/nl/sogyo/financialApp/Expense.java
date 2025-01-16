
package nl.sogyo.financialApp;

import java.time.*;
import java.time.temporal.ChronoUnit;


// TODO: Write factory for Expense class

public class Expense extends Transaction{

    public Expense(double amount, ChronoUnit timeInterval, String category){
        super(amount, timeInterval, category);
    }

    public Expense(double amount, ChronoUnit timeInterval) {
        super(amount, timeInterval);
    }

    public Expense(double amount){
        super(amount);
    }

    public Expense(double amount, String category){
        super(amount, category);
    }

    public Expense(double amount, LocalDateTime timestamp, String category){
        super(amount, timestamp, category);
    }

    public Expense(double amount, LocalDateTime timestamp, String category, ChronoUnit timeInterval){
        super(amount, timestamp, category, timeInterval);
    }
}
