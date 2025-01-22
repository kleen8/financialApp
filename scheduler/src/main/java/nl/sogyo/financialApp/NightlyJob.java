package nl.sogyo.financialApp;

import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class NightlyJob implements Job {

    private final IRecurrentTransactionDAO recurrentTransactionDAO;

    public NightlyJob() {
        this.recurrentTransactionDAO = new RecurrentTransactionDAO();
    }


    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        System.out.println("Quartz job executed at: " + java.time.LocalDateTime.now());

        TransactionDAO transactionDAO = new TransactionDAO();
        List<TransactionDTO> transactionsList = transactionDAO.getAllRecuTransactionDTO();
        for (TransactionDTO trns : transactionsList) {
            System.out.println("Transaction id: " + trns.getTransactionId() 
                + " transaction amount: " + trns.getAmount() 
                + " Account id: " + trns.getAccountId());
            recurrentTransactionDAO.save(trns);
        }
        // Make it so that a recurrent transaction gets put in the recurrent_transaction list
    }
    
}
