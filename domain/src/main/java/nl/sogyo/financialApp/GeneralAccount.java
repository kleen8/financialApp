package nl.sogyo.financialApp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class GeneralAccount extends Account{

    protected List<Expense> expenses = new ArrayList<Expense>();
    protected List<Income> incomes = new ArrayList<Income>();

    public GeneralAccount(String accountName, User owner, double balance){
            super(accountName, owner, balance);
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
                System.out.println("deposit now balance after: " + getBalance());
            }
        }
        for (Expense expense : expenses){
            if (expense.isRecurrent() && shouldProcessTransaction(expense, now)){
                withdraw(expense.getAmount());
                expense.setTimestamp(now);
                System.out.println("withdraw now balance after: " + getBalance());
            }
        }
    }

    public List<Expense> getExpenses(){
        return expenses;
    }

    public List<Income> getIncomes() {
        return incomes;
    }

	@Override
	public boolean transfer(double amount, Account targerAccount) {
        if (amount > 0 && amount <= getBalance()){
            withdraw(amount);
            targerAccount.deposit(amount);
            return true;
        }
        return false;
    }

	@Override
	public AccountType getAccountType() {
        return AccountType.GENERAL;
	}


}
