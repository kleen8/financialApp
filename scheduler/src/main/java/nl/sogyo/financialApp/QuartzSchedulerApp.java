package nl.sogyo.financialApp;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.listeners.SchedulerListenerSupport;

public class QuartzSchedulerApp {

    public static void main(String[] args) throws SchedulerException {

        JobDetail job = JobBuilder.newJob(NightlyJob.class)
                            .withIdentity("nightlyJob", "group1")
                            .build();

        Trigger trigger = TriggerBuilder.newTrigger()
                            .withIdentity("nightlyTrigger", "group1")
                            .withSchedule(CronScheduleBuilder.cronSchedule("0 * * ? * * *"))
                            .build();

        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();


        scheduler.scheduleJob(job, trigger);

       scheduler.getListenerManager().addSchedulerListener(new SchedulerListenerSupport() {
            @Override
            public void schedulerStarted(){
                try {
                    scheduler.triggerJob(job.getKey());
                } catch (SchedulerException e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }
            }
        });

        scheduler.start();

    }

}
