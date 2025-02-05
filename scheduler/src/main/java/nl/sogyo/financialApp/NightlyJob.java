package nl.sogyo.financialApp;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class NightlyJob implements Job {

    private final IRecurrentTransactionDAO recurrentTransactionDAO;
    private final ITransactionDAO transactionDAO;
    private final IAccountDAO accountDAO;
    private final IUserDAO userDAO;
    private List<TransactionDTO> transactionsList = new ArrayList<>();
    private List<RecurrentTransactionDTO> recurrentTransactionDTOs = new ArrayList<>();
    private Double currentBalance;
    private String type;
    private Double transactionAmount;

    public NightlyJob() throws SQLException {
        this.userDAO = new UserDAO();
        this.recurrentTransactionDAO = new RecurrentTransactionDAO();
        this.transactionDAO = new TransactionDAO();
        this.accountDAO = new AccountDAO();
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        System.out.println("Quartz job executed at: " + java.time.LocalDateTime.now());
        getTransactionDTOLists();
        if (checkTransactionDTOList()) {
            walkThroughTransactionList();
        }
    }

    private void walkThroughTransactionList() {
        for (TransactionDTO trns : transactionsList) {
            if (!checkIfRecurringTransactionExists(trns.getTransactionId())) {
                recurrentTransactionDAO.save(trns);
            } else {
                getRecTransactionList(trns.getTransactionId());
                walkThroughRecurrentTransAction(trns);
            }
        }
    }

    private void walkThroughRecurrentTransAction(TransactionDTO trns) {
        for (RecurrentTransactionDTO rTrns : recurrentTransactionDTOs) {
            if(checkIfRecTransNeedsToBeExcecuted(rTrns)) {
                AccountDTO accountDTO = accountDAO.getAccountDTOWithId(trns.getAccountId());
                User user = userDAO.getUserWithId(accountDTO.getUserId());
                getTransactionInformation(trns);
                currentBalance = calculateBalance(type, currentBalance, transactionAmount);
                checkIfEmailNeedsToBeSend(currentBalance, type, user);
                trns = updateTransactionDTO(trns, rTrns);
                updateDatabase(trns, rTrns, currentBalance);
            }
        }
    }
 
    private TransactionDTO updateTransactionDTO(TransactionDTO trns, RecurrentTransactionDTO rTrns) {
        trns.setTimestamp(rTrns.getNext_execution_date().withHour(0).withMinute(0).withSecond(1).toString());
        trns.setLocaldatetime(rTrns.getNext_execution_date());
        return trns;
    }

    private void getTransactionInformation(TransactionDTO trns) {
        this.currentBalance = accountDAO.getAccountBalance(trns.getAccountId());
        this.type = transactionDAO.getTransactionType(trns.getTransactionId());
        this.transactionAmount = transactionDAO.getTransactionAmount(trns.getTransactionId());
    }

    private void updateDatabase(TransactionDTO trns, RecurrentTransactionDTO rTrns, Double currentBalance) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            try {
                conn.setAutoCommit(false);
                accountDAO.setAccountBalance(conn, trns.getAccountId(), currentBalance);
                recurrentTransactionDAO.updateRecTransIsComplete(conn, rTrns.getId()); 
                System.out.println("Trying to save recurring transaction");
                recurrentTransactionDAO.saveRecTrans(conn, trns);
                conn.commit();
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getTransactionDTOLists() {
        transactionsList = transactionDAO.getAllRecuTransactionDTO();
    }

    private void getRecTransactionList(int transactionId) {
        recurrentTransactionDTOs = recurrentTransactionDAO.getAllRecurrentTransactionDTOsId(transactionId);
    }

    private Boolean checkTransactionDTOList() {
        return (transactionsList != null && transactionsList.size() > 0);
    }

    private boolean checkIfRecurringTransactionExists(int transactionId) {
        return (recurrentTransactionDAO.checkIfRecurringTransactionExists(transactionId));
    }

    private Boolean checkIfRecTransNeedsToBeExcecuted(RecurrentTransactionDTO rTrns) {
        return ((rTrns.getNext_execution_date().withHour(0).withMinute(0).withSecond(0).withNano(0).isBefore(LocalDateTime.now())
                                || rTrns.getNext_execution_date().withHour(0).withMinute(0).withSecond(0).withNano(0)
                                    .isEqual(LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0)))
                                && !rTrns.getIs_completed());
    }

    private double calculateBalance(String type, Double currentBalance, Double transactionAmount) {
        if (type.equalsIgnoreCase("income")) {
            currentBalance+=transactionAmount;
        } else if (type.equalsIgnoreCase("expense")) {
            currentBalance-=transactionAmount;
        }
        return currentBalance;
    }

    private void checkIfEmailNeedsToBeSend(Double currentBalance, String type, User user) {
        if (currentBalance <=5 && type.equalsIgnoreCase("expense")) {
            user.sendEmail();
        }
    }

}
