package nl.sogyo.financialApp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AccountTest {

private User owner;
    private User otherUser;
    private GeneralAccount generalAccount;
    private SavingsAccount savingsAccount;
    private InvestmentsAccount investmentAccount;

    @BeforeEach
    void setUp() {
        owner = User.createUser("Jelle", "Jacobs", "jelle.jacobs99@gmail.com", "Derkinderenstraat",
                "1061 vx", "196", "Amsterdam", "Netherlands");
        otherUser = User.createUser("John", "Jacobs", "john.jacobs99@gmail.com", "Derkinderenstraat",
                "1061 vx", "196", "Amsterdam", "Netherlands");

        generalAccount = new GeneralAccount("Main" ,owner, 1000);
        savingsAccount = new SavingsAccount("Savings" ,owner, 500);
        investmentAccount = new InvestmentsAccount("Investing", owner, 1500);
    }

    @Test
    void testGeneralAccountTransferToOtherUser() {
        GeneralAccount otherAccount = new GeneralAccount("Main" ,otherUser, 500);

        generalAccount.transfer(200 , otherAccount);

        assertEquals(800, generalAccount.getBalance());
        assertEquals(700, otherAccount.getBalance());
    }

    @Test
    void testSavingsAccountTransferToSameUser() {
        SavingsAccount otherSavingsAccount = new SavingsAccount("Savings",owner, 300);

        savingsAccount.transfer(200, otherSavingsAccount);

        assertEquals(300, savingsAccount.getBalance());
        assertEquals(500, otherSavingsAccount.getBalance());
    }

    @Test
    void testSavingsAccountTransferToDifferentUserFails() {
        SavingsAccount otherSavingsAccount = new SavingsAccount("Savings", otherUser, 300);


        assertEquals(500, savingsAccount.getBalance());
        assertEquals(300, otherSavingsAccount.getBalance());
    }

    @Test
    void testInvestmentAccountWithdraw() {
        investmentAccount.withdraw(200);

        assertEquals(1300, investmentAccount.getBalance());
    }

    @Test
    void testInvestmentAccountWithdrawExceedsBalance() {
        investmentAccount.withdraw(1600);

        assertEquals(1500, investmentAccount.getBalance());
    }

    @Test
    void testDepositInGeneralAccount() {
        generalAccount.deposit(500);

        assertEquals(1500, generalAccount.getBalance());
    }

    @Test
    void testDepositInSavingsAccount() {
        savingsAccount.deposit(300);

        assertEquals(800, savingsAccount.getBalance());
    }

    @Test
    void testInvestmentAccountTransferToSameUser() {
        InvestmentsAccount otherInvestmentAccount = new InvestmentsAccount("Investing", owner, 700);

        investmentAccount.transfer(500, otherInvestmentAccount);

        assertEquals(1000, investmentAccount.getBalance());
        assertEquals(1200, otherInvestmentAccount.getBalance());
    }

    @Test
    void testInvestmentAccountTransferToDifferentUserFails() {
        InvestmentsAccount otherInvestmentAccount = new InvestmentsAccount("Inversting 2", otherUser, 700);

        investmentAccount.transfer(500, otherInvestmentAccount);

        assertEquals(1500, investmentAccount.getBalance());
        assertEquals(700, otherInvestmentAccount.getBalance());
    }
}
