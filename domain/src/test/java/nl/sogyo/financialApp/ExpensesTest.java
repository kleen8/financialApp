package nl.sogyo.financialApp;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class ExpensesTest {

     @Test
    public void testRecurrentExpenseWithCategory() {
        Expense expense = new Expense(100, ChronoUnit.MONTHS, "Utilities");

        assertEquals(100, expense.getAmount(), 0.01);
        assertEquals("Utilities", expense.getCategory());
        assertNotNull(expense.getTimestamp());
        assertEquals(ChronoUnit.MONTHS, expense.getTimeInterval());
        assertTrue(expense.isRecurrent());
    }

    @Test
    public void testRecurrentExpenseWithoutCategory() {
        Expense expense = new Expense(200, ChronoUnit.WEEKS);

        assertEquals(200, expense.getAmount(), 0.01);
        assertEquals("", expense.getCategory());
        assertNotNull(expense.getTimestamp());
        assertEquals(ChronoUnit.WEEKS, expense.getTimeInterval());
        assertTrue(expense.isRecurrent());
    }

    @Test
    public void testOneTimeExpenseWithCategory() {
        Expense expense = new Expense(50, "Groceries");

        assertEquals(50, expense.getAmount(), 0.01);
        assertEquals("Groceries", expense.getCategory());
        assertNotNull(expense.getTimestamp());
        assertNull(expense.getTimeInterval());
        assertFalse(expense.isRecurrent());
    }

    @Test
    public void testOneTimeExpenseWithoutCategory() {
        Expense expense = new Expense(75);

        assertEquals(75, expense.getAmount(), 0.01);
        assertEquals("", expense.getCategory());
        assertNotNull(expense.getTimestamp());
        assertNull(expense.getTimeInterval());
        assertFalse(expense.isRecurrent());
    }

    @Test
    public void testExpenseWithCustomTimestamp() {
        LocalDateTime customTimestamp = LocalDateTime.of(2025, 1, 1, 12, 0);
        Expense expense = new Expense(120, customTimestamp, "Holiday");

        assertEquals(120, expense.getAmount(), 0.01);
        assertEquals("Holiday", expense.getCategory());
        assertEquals(customTimestamp, expense.getTimestamp());
        assertNull(expense.getTimeInterval());
        assertFalse(expense.isRecurrent());
    }

    @Test
    public void testRecurrentExpenseWithCustomTimestamp() {
        LocalDateTime customTimestamp = LocalDateTime.of(2025, 1, 1, 12, 0);
        Expense expense = new Expense(500, customTimestamp, "Rent", ChronoUnit.YEARS);

        assertEquals(500, expense.getAmount(), 0.01);
        assertEquals("Rent", expense.getCategory());
        assertEquals(customTimestamp, expense.getTimestamp());
        assertEquals(ChronoUnit.YEARS, expense.getTimeInterval());
        assertTrue(expense.isRecurrent());
    }

    @Test
    public void testDefaultTimestampIsNow() {
        Expense expense = new Expense(80, ChronoUnit.DAYS, "Subscription");
        LocalDateTime now = LocalDateTime.now();

        // Allow a small margin for timing differences
        assertTrue(expense.getTimestamp().isAfter(now.minusSeconds(1)));
        assertTrue(expense.getTimestamp().isBefore(now.plusSeconds(1)));
    } 
}
