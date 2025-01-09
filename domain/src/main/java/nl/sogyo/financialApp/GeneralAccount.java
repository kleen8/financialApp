package nl.sogyo.financialApp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class GeneralAccount implements IAccount {

    private User user;
    private double balance = 0;
    private AccountType accountType = AccountType.GENERAL;

    protected List<Expense> expenses = new ArrayList<Expense>();
    protected List<Income> incomes = new ArrayList<Income>();

    public GeneralAccount(User user, double balance){
            this.user = user;
            this.balance = balance;
    }


	@Override
	public void deposit(double amount) {
		if (amount > 0.0){
            this.balance+=amount;
        }else {
            System.out.println("can't deposit negative values hennie");
        }
	}

	@Override
	public void withdraw(double amount) {
        if (amount <= balance){
            balance-=amount;
        } else {
            System.out.println("Can't withdraw more then you own");
        };
	}

	@Override
	public double getBalance() {
        return balance;
	}

	@Override
	public void transfer(IAccount targetAccount, double amount) {
        if (amount <= this.balance && amount > 0){
            withdraw(amount);
            targetAccount.deposit(amount);
        } else {
            System.out.println("Insufficient amount");
        }
	}

   
    public void addIncome(Income income) {
        incomes.add(income);
        deposit(income.getAmount());
    }

    public void addExpense(Expense expense) {
        expenses.add(expense);
        withdraw(expense.getAmount());
    }

    private boolean shouldProcessTransaction(Expense expense, LocalDateTime now){
        return expense.getTimestamp().plus(1, expense.getTimeInterval()).isBefore(now) ||
               expense.getTimestamp().plus(1, expense.getTimeInterval()).isEqual(now);
    }

    private boolean shouldProcessTransaction(Income income, LocalDateTime now){
        return income.getTimestamp().plus(1, income.getTimeInterval()).isBefore(now) ||
               income.getTimestamp().plus(1, income.getTimeInterval()).isEqual(now);
    }

    public void processRecurrentTransactions(){
        LocalDateTime now = LocalDateTime.now();
        for (Income income : incomes) {
            if (income.isRecurrent() && shouldProcessTransaction(income, now)){
                deposit(income.getAmount());
                income.setTimestamp(now);
                System.out.println("deposit now balance after: " + balance);
            }
        }
        for (Expense expense : expenses){
            if (expense.isRecurrent() && shouldProcessTransaction(expense, now)){
                withdraw(expense.getAmount());
                expense.setTimestamp(now);
                System.out.println("withdraw now balance after: " + balance);
            }
        }
    }

    public List<Expense> getExpenses(){
        return expenses;
    }


    public List<Income> getIncomes() {
        return incomes;
    }


}
