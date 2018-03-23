package com.starin.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Seconds;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KYCDateUtil {

	private static final Logger logger = LoggerFactory.getLogger(KYCDateUtil.class);

	/**
	 * 
	 * @param startDate
	 * @param endDate
	 * @param format
	 * @return
	 */
	public static int compareDates(String startDate, String endDate, String format) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			Date date1 = sdf.parse(startDate);
			Date date2 = sdf.parse(endDate);

			System.out.println(date1);
			System.out.println(date2);

			if (date1.compareTo(date2) > 0) {
				return 1;
			} else if (date1.compareTo(date2) < 0) {
				return -1;
			} else if (date1.compareTo(date2) == 0) {
				return 0;
			} else {
				logger.debug("inside unknown case");
				return -1;
			}
		} catch (ParseException ex) {
			ex.printStackTrace();
			return -1;
		}

	}

	/**
	 * Utility Function used to check whether the link expired or not
	 * 
	 * @param creationDate
	 * @param timeLimitInSeconds
	 * @return true/false;
	 */
	public static boolean isLinkExpire(Date creationDate, int timeLimitInSeconds) {
		DateTime currDate = new DateTime(new Date(), DateTimeZone.UTC);
		logger.debug("creation Date : " + currDate);
		DateTime creationDate2 = new DateTime(creationDate, DateTimeZone.UTC);
		logger.debug("comparing date " + creationDate2);
		boolean isExpired = Seconds.secondsBetween(creationDate2, currDate).getSeconds() > timeLimitInSeconds;
		logger.debug("Reset link TimeExpire status :" + isExpired);
		return isExpired;
	}

	/**
	 * Utility to check if date is valid or not
	 * 
	 * @param inputDate
	 * @param formatString
	 * @return
	 */
	public static boolean isValidDate(String inputDate, String formatString) {
		try {
			SimpleDateFormat format = new SimpleDateFormat(formatString);
			format.setLenient(false);
			format.parse(inputDate);
			return true;
		} catch (ParseException e) {
			e.printStackTrace();
			return false;
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			return false;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static String formatDate(Date date, String format) throws Exception {
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		return formatter.format(date);
	}

	public static Date addDays(Date date, int day) {
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(date);
		cal.add(Calendar.DATE, day);
		return cal.getTime();
	}

	public static Date addYears(Date date, int year) {
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(date);
		cal.add(Calendar.YEAR, year);
		return cal.getTime();
	}

	public static Date addMonths(Date date, int months) {
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(date);
		cal.add(Calendar.MONTH, months);
		return cal.getTime();
	}

	public static boolean validateYear(String year) {
		Integer yearValue = KYCUtilities.parseIntoInteger(year);
		if (yearValue == null) {
			return false;
		}
		if (year.length() != 4) {
			return false;
		}
		return true;
	}

	/*
	 * public static void main(String args[]){
	 * 
	 * try { JSONObject res=new JSONObject("{}");
	 * System.out.println(res.get("error"));
	 * 
	 * } catch (JSONException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); } }
	 */

}
