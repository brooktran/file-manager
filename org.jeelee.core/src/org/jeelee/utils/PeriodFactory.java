package org.jeelee.utils;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

public class PeriodFactory {
    private static final int MILLIS_PER_MINUTE = 	60 * 1000;
    private static final int MILLIS_PER_HOUR = 		60 * MILLIS_PER_MINUTE;
    private static final int MILLIS_PER_DAY = 		24 * MILLIS_PER_HOUR;

    
	private static Calendar latestOfDay(Calendar calendar) {
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 999);
		return calendar;
	}
	
	private static Calendar beginingOfDay(Calendar calendar) {
		calendar.set(Calendar.HOUR_OF_DAY, 00);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 000);
		return calendar;
	}
	public static Period today() {
		Calendar calendar = Calendar.getInstance();
		return new Period(beginingOfDay(calendar).getTimeInMillis(), latestOfDay(
				calendar).getTimeInMillis());
	}
	
	public static Period yesterDay(){
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DATE,calendar.get(Calendar.DATE)-1);
		return new Period(beginingOfDay(calendar).getTimeInMillis(), latestOfDay(
				calendar).getTimeInMillis());
	}


	public static Period thisWeek() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		long from = beginingOfDay(calendar).getTimeInMillis();
		
		calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
		long to = latestOfDay(calendar).getTimeInMillis();
		return new Period(from, to);
	}
	
	public static Period lastWeek(){
		Calendar calendar = Calendar.getInstance();
		
		calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		calendar.set(Calendar.DATE,calendar.get(Calendar.DATE)-7);

		long from = beginingOfDay(calendar).getTimeInMillis();
		
		calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
		long to = latestOfDay(calendar).getTimeInMillis();
		return new Period(from, to);
	}
	
	public static Period thisMonth(){
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		long from = beginingOfDay(calendar).getTimeInMillis();
		
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getMaximum(Calendar.DAY_OF_MONTH)-1);
		long to = latestOfDay(calendar).getTimeInMillis();

		return new Period(from, to);
	}
	public static Period lastMonth(){
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH)-1);
		
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		long from = beginingOfDay(calendar).getTimeInMillis();
		
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getMaximum(Calendar.DAY_OF_MONTH)-1);
		long to = latestOfDay(calendar).getTimeInMillis();

		return new Period(from, to);
	}
	
	/**
	 * get a period before last month
	 */
	public static Period earierThisYear(){
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_YEAR, 1);
		long from = beginingOfDay(calendar).getTimeInMillis();
		long to = lastMonth().getFrom()-1;
		return new Period(from, to);

	}
	public static Period beforeThisYear(){
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR)-1);
		calendar.set(Calendar.DAY_OF_YEAR, 1);
		long from = beginingOfDay(calendar).getTimeInMillis();
		long to = earierThisYear().getFrom()-1;
		return new Period(from, to);
	}
	
	public static Period beforeNow() {
		return new Period(-Long.MIN_VALUE, System.currentTimeMillis());
	}
	
	
	
	public static void main(String[] args) {
//		DateFormat format = new SimpleDateFormat("yy-MM-dd HH:mm:ss:SSS");
		DateFormat format = DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL);
		System.out.println("------------thisWeek-----------");
		System.out.println(format.format(new Date(thisWeek().getFrom())));
		System.out.println(format.format(new Date(thisWeek().getTo())));
	
		System.out.println("------------thisMonth-----------");
		System.out.println(format.format(new Date(thisMonth().getFrom())));
		System.out.println(format.format(new Date(thisMonth().getTo())));

		System.out.println("------------yesterDay-----------");
		System.out.println(format.format(new Date(yesterDay().getFrom())));
		System.out.println(format.format(new Date(yesterDay().getTo())));

		System.out.println("------------lastWeek-----------");
		System.out.println(format.format(new Date(lastWeek().getFrom())));
		System.out.println(format.format(new Date(lastWeek().getTo())));

		System.out.println("------------lastMonth-----------");
		System.out.println(format.format(new Date(lastMonth().getFrom())));
		System.out.println(format.format(new Date(lastMonth().getTo())));

		System.out.println("------------earierThisYear-----------");
		System.out.println(format.format(new Date(earierThisYear().getFrom())));
		System.out.println(format.format(new Date(earierThisYear().getTo())));

		System.out.println("------------beforeThisYear-----------");
		System.out.println(format.format(new Date(beforeThisYear().getFrom())));
		System.out.println(format.format(new Date(beforeThisYear().getTo())));
		
	}

	
}

