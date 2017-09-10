package com.weddingnotification.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.Calendar;

/*
 * @author Neel Jain
 */

public class DateUtil {

	public static Date getCurrentUtcDate() {
		SimpleDateFormat dateFormatGmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormatGmt.setTimeZone(TimeZone.getTimeZone("GMT"));
        String dfgmt = dateFormatGmt.format(new Date());
        SimpleDateFormat newFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return newFormatter.parse(dfgmt);
        } catch (Exception e) {
        	// log error
        	return new Date();
        }
	}

	public static Date addInterval(Date startInterval, int minutes) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(startInterval);
		cal.add(Calendar.MINUTE, minutes);
		return cal.getTime();
	}
}