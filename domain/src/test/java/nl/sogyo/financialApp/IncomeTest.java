package nl.sogyo.financialApp;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class IncomeTest {

    @Test
    public void testRecurrentIncomeWithCategory() {
        Income income = new Income(1500, ChronoUnit.MONTHS, "Salary");

        assertEquals(1500, income.getAmount(), 0.01);
        assertEquals("Salary", income.getCategory());
        assertNotNull(income.getTimestamp());
        assertEquals(ChronoUnit.MONTHS, income.getTimeInterval());
        assertTrue(income.isRecurrent());
    }

    @Test
    public void testRecurrentIncomeWithoutCategory() {
        Income income = new Income(300, ChronoUnit.WEEKS);

        assertEquals(300, income.getAmount(), 0.01);
        assertEquals("", income.getCategory());
        assertNotNull(income.getTimestamp());
        assertEquals(ChronoUnit.WEEKS, income.getTimeInterval());
        assertTrue(income.isRecurrent());
    }

    @Test
    public void testOneTimeIncomeWithCategory() {
        Income income = new Income(100, "Gift");

        assertEquals(100, income.getAmount(), 0.01);
        assertEquals("Gift", income.getCategory());
        assertNotNull(income.getTimestamp());
        assertNull(income.getTimeInterval());
        assertFalse(income.isRecurrent());
    }

    @Test
    public void testOneTimeIncomeWithoutCategory() {
        Income income = new Income(50);

        assertEquals(50, income.getAmount(), 0.01);
        assertEquals("", income.getCategory());
        assertNotNull(income.getTimestamp());
        assertNull(income.getTimeInterval());
        assertFalse(income.isRecurrent());
    }

    @Test
    public void testIncomeWithCustomTimestamp() {
        LocalDateTime customTimestamp = LocalDateTime.of(2025, 1, 1, 10, 30);
        Income income = new Income(1200, customTimestamp, "Bonus");

        assertEquals(1200, income.getAmount(), 0.01);
        assertEquals("Bonus", income.getCategory());
        assertEquals(customTimestamp, income.getTimestamp());
        assertNull(income.getTimeInterval());
        assertFalse(income.isRecurrent());
    }

    @Test
    public void testRecurrentIncomeWithCustomTimestamp() {
        LocalDateTime customTimestamp = LocalDateTime.of(2025, 1, 1, 12, 0);
        Income income = new Income(2500, customTimestamp, "Freelancing", ChronoUnit.YEARS);

        assertEquals(2500, income.getAmount(), 0.01);
        assertEquals("Freelancing", income.getCategory());
        assertEquals(customTimestamp, income.getTimestamp());
        assertEquals(ChronoUnit.YEARS, income.getTimeInterval());
        assertTrue(income.isRecurrent());
    }

    @Test
    public void testDefaultTimestampIsNow() {
        Income income = new Income(800, ChronoUnit.DAYS, "Savings Interest");
        LocalDateTime now = LocalDateTime.now();

        // Allow a small margin for timing differences
        assertTrue(income.getTimestamp().isAfter(now.minusSeconds(1)));
        assertTrue(income.getTimestamp().isBefore(now.plusSeconds(1)));
    }

    @Test
    public void testSettersUpdateAttributes() {
        Income income = new Income(100);

        income.setAmount(500);
        income.setCategory("Investment Return");
        income.setRecurrent(true);
        income.setTimeInterval(ChronoUnit.MONTHS);
        LocalDateTime customTimestamp = LocalDateTime.of(2025, 2, 15, 15, 0);
        income.setTimestamp(customTimestamp);

        assertEquals(500, income.getAmount(), 0.01);
        assertEquals("Investment Return", income.getCategory());
        assertTrue(income.getRecurrent());
        assertEquals(ChronoUnit.MONTHS, income.getTimeInterval());
        assertEquals(customTimestamp, income.getTimestamp());
    }
}

