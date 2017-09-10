package com.weddingnotification;

// Internal Stuff
import com.palipind.weddingdb.DeviceToken;
import com.palipind.weddingdb.Event;
import com.palipind.weddingdb.WeddingEvent;
import com.weddingnotification.util.HibernateUtil;
import com.weddingnotification.util.DateUtil;
import com.weddingnotification.Notification;

import org.hibernate.Session;

import java.util.List;
import java.util.ArrayList;
import java.util.Date;

/**
 * @author Rohan Jain
 */

public class Engine {
	/**
     * Main notification engine.
     * To be called by job scheduler.
     * @Input 'timeInterval' in minutes
     * Send event notification to all devices
     * for events lying between now() and now() + timeInterval
     * @return
     */
	private static int DEFAULT_INTERVAL = 15;
    public static void main(String[] args) {
    	int intervalMinutes = extractTimeInterval(args);
        Date startInterval = DateUtil.getCurrentUtcDate();
        Date endInterval = DateUtil.addInterval(startInterval, intervalMinutes);

        Session session = HibernateUtil.getSessionFactory().openSession();

        DbAccessor dba = new DbAccessor(session);
        List<WeddingEvent> weddingEvents = dba.getWeddingEventsBetween(startInterval, endInterval);
        for (WeddingEvent weddingEvent:weddingEvents) {
        	List<DeviceToken> deviceTokens = dba.getDeviceTokensForWeddingEvent(weddingEvent);
        	List<String> tokens = new ArrayList<String>();
            for(DeviceToken deviceToken : deviceTokens) {
                tokens.add(deviceToken.getDeviceToken());
            }
        	Notification notification = new Notification("dev", weddingEvent.getMessage(), tokens);
            notification.post();
        }
        session.close();
    }

    private static int extractTimeInterval(String[] args) {
    	if (args.length == 0)
    		return DEFAULT_INTERVAL;
    	try {
        	int intervalMinutes = Integer.parseInt(args[0]);
        	return (intervalMinutes == 0) ? DEFAULT_INTERVAL : intervalMinutes;
        }
        catch (Exception e) {
        	return DEFAULT_INTERVAL;
        }
    }
}