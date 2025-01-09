package nl.sogyo.financialApp;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;



public class AccountTest {

    private User user = User.createUser("Jelle", "Jacobs", "jelle.jacobs99@gmail.com", "Derkinderenstraat",
        "1061 vx", "196", "Amsterdam", "Netherlands");
    private GeneralAccount account = new GeneralAccount(user, 1000);
    private GeneralAccount account2 = new GeneralAccount(user, 500);

    @Test
    public void testAccountSetup(){
        assertEquals(1000, account.getBalance());
    }

    @Test
    public void testDepositNegative(){
        account.deposit(-10);
        assertEquals(1000, account.getBalance());
    }

    @Test
    public void testWithdrawInsufficientFunds() {
        account.withdraw(1500);
        assertEquals(1000, account.getBalance());
    }

    @Test
    public void testTransferToSameAccount() {
        account.transfer(account, 100);
        assertEquals(1000, account.getBalance());
    }


    @Test
    public void testTransferNegativeAmount() {
        account.transfer(account2, -100);
        assertEquals(1000, account.getBalance());
        assertEquals(500, account2.getBalance());
    }

    @Test
    public void testTransferZeroAmount() {
        account.transfer(account2, 0);
        assertEquals(1000, account.getBalance());
        assertEquals(500, account2.getBalance());
    }

    @Test
    public void testAccountInitializationZeroBalance() {
        IAccount zeroBalanceAccount = new GeneralAccount(user, 0);
        assertEquals(0, zeroBalanceAccount.getBalance());
    }

    @Test
    public void testDeposit(){
        account.deposit(10);
        assertEquals(1010, account.getBalance());
    }

    @Test
    public void testDepositDouble(){
        account.deposit(10.56);
        assertEquals(1010.56, account.getBalance());
    }

    @Test
    public void testTransfer(){
        account.transfer(account2, 500);
        assertEquals(500, account.getBalance());
        assertEquals(1000, account2.getBalance());
    }

    @Test
    public void testTransferInsufficientFunds() {
        account.transfer(account2, 1500);
        assertEquals(1000, account.getBalance());
        assertEquals(500, account2.getBalance());
    }
    

     @Test
    public void testAddIncome() {
        Income income = new Income(200.0, ChronoUnit.WEEKS, "Salary");
        account.addIncome(income);

        assertEquals(1200.0, account.getBalance());
        assertEquals(1, account.getIncomes().size());
        assertEquals("Salary", account.getIncomes().get(0).getCategory());
    }

    @Test
    public void testAddExpense() {
        Expense expense = new Expense(150.0, ChronoUnit.MONTHS, "Rent");
        account.addExpense(expense);

        assertEquals(850.0, account.getBalance());
        assertEquals(1, account.getExpenses().size());
        assertEquals("Rent", account.getExpenses().get(0).getCategory());
    }

    @Test
    public void testProcessRecurrentIncome() {
        LocalDateTime pastTimestamp = LocalDateTime.now().minusWeeks(1);
        Income income = new Income(300.0, pastTimestamp, "Weekly Income", ChronoUnit.WEEKS);
        account.addIncome(income);

        account.processRecurrentTransactions();

        assertEquals(1600.0, account.getBalance()); // Recurrent income added
    }

    @Test
    public void testProcessRecurrentExpense() {
        LocalDateTime pastTimestamp = LocalDateTime.now().minusMonths(1);
        Expense expense = new Expense(400.0, pastTimestamp, "Monthly Rent", ChronoUnit.MONTHS);
        account.addExpense(expense);

        account.processRecurrentTransactions();

        assertEquals(200.0, account.getBalance()); // Recurrent expense deducted
    }

    @Test
    public void testNonRecurrentIncomeNotProcessed() {
        Income income = new Income(100.0, "Gift");
        account.addIncome(income);

        account.processRecurrentTransactions();

        assertEquals(1100.0, account.getBalance()); // Non-recurrent income not processed again
    }

    @Test
    public void testNonRecurrentExpenseNotProcessed() {
        Expense expense = new Expense(200.0, "Groceries");
        account.addExpense(expense);

        account.processRecurrentTransactions();

        assertEquals(800.0, account.getBalance()); // Non-recurrent expense not processed again
    }

    @Test
    public void testMultipleRecurrentTransactions() {
        Income weeklyIncome = new Income(250.0, LocalDateTime.now().minusWeeks(1), "Weekly Bonus", ChronoUnit.WEEKS);
        Expense monthlyExpense = new Expense(100.0, LocalDateTime.now().minusMonths(1), "Utilities", ChronoUnit.MONTHS);

        account.addIncome(weeklyIncome);
        account.addExpense(monthlyExpense);
        System.out.println(account.getBalance());
        account.processRecurrentTransactions();

        assertEquals(1300.0, account.getBalance()); // Income added twice, expense deducted once
    }

}
