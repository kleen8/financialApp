package nl.sogyo.financialApp;

import java.time.LocalDateTime;
import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class NightlyJob implements Job {

    private final IRecurrentTransactionDAO recurrentTransactionDAO;
    private final ITransactionDAO transactionDAO;
    private final IAccountDAO accountDAO;

    public NightlyJob() {
        this.recurrentTransactionDAO = new RecurrentTransactionDAO();
        this.transactionDAO = new TransactionDAO();
        this.accountDAO = new AccountDAO();
    }


    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        System.out.println("Quartz job executed at: " + java.time.LocalDateTime.now());

        List<TransactionDTO> transactionsList = transactionDAO.getAllRecuTransactionDTO();

        if (transactionsList != null && transactionsList.size() > 0){
            for (TransactionDTO trns : transactionsList){
                System.out.println(trns.getTransactionId() + " " + trns.getAmount());
                System.out.println(recurrentTransactionDAO.checkIfRecurringTransactionExists(trns.getTransactionId()));
                if (!recurrentTransactionDAO.checkIfRecurringTransactionExists(trns.getTransactionId())){
                    recurrentTransactionDAO.save(trns);
                    System.out.println("New recurrent transaction saved with id: " + trns.getTransactionId());
                } else{
                    List<RecurrentTransactionDTO> recurrentTransactionsDTOsForId = 
                    recurrentTransactionDAO.getAllRecurrentTransactionDTOsId(trns.getTransactionId());
                    for (RecurrentTransactionDTO rTrns : recurrentTransactionsDTOsForId){
                        if((rTrns.getNext_execution_date().withHour(0).withMinute(0).withSecond(0).withNano(0).isBefore(LocalDateTime.now())
                                || rTrns.getNext_execution_date().withHour(0).withMinute(0).withSecond(0).withNano(0)
                                    .isEqual(LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0)))
                                && !rTrns.getIs_completed()){
                            System.out.println("Transaction should be done for transaction with id: " + trns.getTransactionId());   
                            System.out.println(trns.getAccountId());
                            Account account = accountDAO.getAccountWithId(trns.getAccountId());
                            System.out.println("Account is: " + account.toString() + " With balance: " 
                                + account.getBalance());
                            Transaction transaction = transactionDAO.getTransaction(trns.getTransactionId());
                            account.addTransaction(transaction); 
                            account.processRecurrentTransactions();
                            System.out.println("Account object new balance is: " + account.getBalance());
                            Double currentBalance = accountDAO.getAccountBalance(trns.getAccountId());
                            Double transactionAmount = transactionDAO.getTransactionAmount(trns.getTransactionId());
                            String type = transactionDAO.getTransactionType(trns.getTransactionId());
                            System.out.println("Account balance: " + currentBalance + " transaction amount: "
                                + transactionAmount + " " + type.toLowerCase());
                            if (type.equalsIgnoreCase("income")){
                                System.out.println("Is income");
                                currentBalance+=transactionAmount;
                            } else if (type.equalsIgnoreCase("expense")){
                                currentBalance-=transactionAmount;
                                System.out.println("Is Expense");
                            }
                            System.out.println("New balance after transaction is: " + currentBalance);
                            accountDAO.updateBalance(trns.getAccountId(), currentBalance); recurrentTransactionDAO.updateRecTransIsComplete(rTrns.getId()); 
                            trns.setTimestamp(rTrns.getNext_execution_date().withHour(0).withMinute(0).withSecond(1).toString());
                            recurrentTransactionDAO.saveRecTrans(trns);
                        }
                    }
                }
            }
        }
    }
}
