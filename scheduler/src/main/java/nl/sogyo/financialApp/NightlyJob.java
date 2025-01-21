package nl.sogyo.financialApp;

import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class NightlyJob implements Job {
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        System.out.println("Quartz job executed at: " + java.time.LocalDateTime.now());

        TransactionDAO transactionDAO = new TransactionDAO();
        List<TransactionDTO> transactionsList = transactionDAO.getAllRecuTransactionDTO();
        for (TransactionDTO trns : transactionsList) {
            System.out.println(trns.getTransactionId());
        }
    }
    
}
