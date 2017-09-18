package com.weddingnotification;

import com.weddingnotification.job.EventNotificationJob;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Rohan Jain
 */
public class Engine implements ServletContextListener {
    Scheduler _scheduler;

    private static final int THIRTY_MINUTE_INTERVAL = 30;

    private static final String NOTIFICATION_GROUP_NAME = "notificationGroup";
    private static final String EVENT_NOTIFICATION_TRIGGER_NAME = "eventNotificationTrigger";
    private static final String EVENT_NOTIFICATION_JOB_NAME = "eventNotificationJob";

    private static Logger LOG = Logger.getLogger(Engine.class.getName());

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        try {
            SimpleScheduleBuilder notificationSchedule = SimpleScheduleBuilder.simpleSchedule()
                    .withIntervalInMinutes(THIRTY_MINUTE_INTERVAL).repeatForever();

            Trigger notificationTrigger = TriggerBuilder.newTrigger()
                    .withIdentity(EVENT_NOTIFICATION_TRIGGER_NAME, NOTIFICATION_GROUP_NAME)
                    .withSchedule(notificationSchedule)
                    .build();

            JobDetail eventNotificationJob = JobBuilder.newJob(EventNotificationJob.class)
                    .withIdentity(EVENT_NOTIFICATION_JOB_NAME, NOTIFICATION_GROUP_NAME).build();

            _scheduler = new StdSchedulerFactory().getScheduler();
            _scheduler.start();
            LOG.log(Level.INFO, "Scheduling job : " + eventNotificationJob.getKey().getName());
            _scheduler.scheduleJob(eventNotificationJob, notificationTrigger);

        } catch (SchedulerException e) {
            LOG.log(Level.SEVERE, "Error occurred in context initialization", e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        try {
            LOG.log(Level.INFO, "Shutting down scheduler");
            _scheduler.shutdown();
        } catch (SchedulerException e) {
            LOG.log(Level.SEVERE, "Unable to shutdown scheduler", e);
        }
    }
}