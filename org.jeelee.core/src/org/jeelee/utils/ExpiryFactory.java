package org.jeelee.utils;

import java.util.Calendar;

public class ExpiryFactory {
	
	
	
	public static Expiry today(){
		Calendar calendar=Calendar.getInstance();
		return new Expiry(beginingOfDay(calendar).getTime(), latestOfDay(calendar).getTime());
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	private static Calendar latestOfDay(Calendar calendar) {
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND,59);	
		return calendar;
	}

	private static Calendar beginingOfDay(Calendar calendar) {
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND,0);
		return calendar;
	}
	
	
	
	
	
	
}
