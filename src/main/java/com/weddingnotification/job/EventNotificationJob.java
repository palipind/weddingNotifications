package com.weddingnotification.job;

import com.palipind.weddingdb.DeviceToken;
import com.palipind.weddingdb.WeddingEvent;
import com.weddingnotification.DbAccessor;
import com.weddingnotification.Notification;
import com.weddingnotification.util.DateUtil;
import com.weddingnotification.util.HibernateUtil;
import org.hibernate.Session;
import org.quartz.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This job sends event notification to all devices for events lying
 * between DateUtil#getCurrentUtcDate and DateUtil#getCurrentUtcDate + DEFAULT_INTERVAL
 *
 * @author Neel Jain
 */
@DisallowConcurrentExecution
public class EventNotificationJob implements Job {

    private static int DEFAULT_INTERVAL = 15;

    private static Logger LOG = Logger.getLogger(EventNotificationJob.class.getName());

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        LOG.info("Starting execution of this job");
        Session session = null;
        try {
            Date startInterval = DateUtil.getCurrentUtcDate();
            Date endInterval = DateUtil.addInterval(startInterval, DEFAULT_INTERVAL);

            session = HibernateUtil.getSessionFactory().openSession();

            DbAccessor dba = new DbAccessor(session);
            List<WeddingEvent> weddingEvents = dba.getWeddingEventsBetween(startInterval, endInterval);
            for (WeddingEvent weddingEvent : weddingEvents) {
                List<DeviceToken> deviceTokens = dba.getDeviceTokensForWeddingEvent(weddingEvent);
                List<String> tokens = new ArrayList<String>();
                for (DeviceToken deviceToken : deviceTokens) {
                    tokens.add(deviceToken.getDeviceToken());
                }
                Notification notification = new Notification("dev", weddingEvent.getMessage(), tokens);
                notification.post();
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error occurred in Event Notification Job", e);
        } finally {
            session.close();
        }
    }
}
