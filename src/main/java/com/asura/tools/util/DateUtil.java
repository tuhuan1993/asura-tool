package com.asura.tools.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import com.asura.tools.util.DateUtil.TimeUnit;

public class DateUtil {
	public static int getIntyyyyMMdd(Date date) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		return Integer.parseInt(formatter.format(date));
	}

	public static String getDateString(Date date) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		return formatter.format(date);
	}

	public static String getTodayDate() {
		return getDateAndTimeString(new Date(), "yyyy-MM-dd");
	}

	public static String getTodayDateTime() {
		return getDateAndTimeString(new Date(), "yyyy-MM-dd HH:mm:ss");
	}

	public static String getTimeString(Date date) {
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
		return formatter.format(date);
	}

	public static String getDateAndTimeString(Date date) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return formatter.format(date);
	}

	public static String getDateAndTimeString(Date date, String format) {
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		return formatter.format(date);
	}

	public static Date getDateFromString(String date) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			return new Date(formatter.parse(date).getTime());
		} catch (ParseException localParseException) {
		}
		return null;
	}

	public static Date getDateFromString(String date, String format) {
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		try {
			return new Date(formatter.parse(date).getTime());
		} catch (Exception localException) {
		}
		return null;
	}

	public static double getTimeByMinute(Date date1, Date date2) {
		return (Math.abs(date1.getTime() - date2.getTime()) / Double.valueOf("60000").doubleValue());
	}

	public static double getTimeBySecond(Date date1, Date date2) {
		return (Math.abs(date1.getTime() - date2.getTime()) / Double.valueOf("1000").doubleValue());
	}

	public static double getTimeByHour(Date date1, Date date2) {
		return (Math.abs(date1.getTime() - date2.getTime()) / Double.valueOf("3600000").doubleValue());
	}

	public static double getTimeByDay(Date date1, Date date2) {
		return (Math.abs(date1.getTime() - date2.getTime()) / Double.valueOf("86400000").doubleValue());
	}

	public static double getTimeByWeek(Date date1, Date date2) {
		return (Math.abs(date1.getTime() - date2.getTime()) / Double.valueOf("604800000").doubleValue());
	}

	public static double getTimeByMonth(Date date1, Date date2) {
		return (Math.abs(date1.getTime() - date2.getTime()) / Double.valueOf("2592000000").doubleValue());
	}

	public static double getTimeValue(TimeUnit tu, Date date1, Date date2) {
		switch (tu.ordinal()) {
		case 1:
			return getTimeByDay(date1, date2);
		case 2:
			return getTimeByMonth(date1, date2);
		case 3:
			return getTimeByWeek(date1, date2);
		case 4:
			return getTimeByHour(date1, date2);
		case 5:
			return getTimeByMinute(date1, date2);
		case 6:
			return getTimeBySecond(date1, date2);
		}
		return 0.0D;
	}

	public static long getMiniseconds(TimeUnit tu, double value) {
		Double d;
		switch (tu.ordinal()) {
		case 1:
			d = Double.valueOf(Long.valueOf("86400000").longValue() * value);
			break;
		case 2:
			d = Double.valueOf(Long.valueOf("2592000000").longValue() * value);
			break;
		case 3:
			d = Double.valueOf(Long.valueOf("604800000").longValue() * value);
			break;
		case 4:
			d = Double.valueOf(Long.valueOf("3600000").longValue() * value);
			break;
		case 5:
			d = Double.valueOf(Long.valueOf("60000").longValue() * value);
			break;
		case 6:
			d = Double.valueOf(Long.valueOf("1000").longValue() * value);
			break;
		default:
			d = Double.valueOf(0.0D);
		}

		return d.longValue();
	}

	public static Date getBeforeDay(TimeUnit tu, double value) {
		Double time = Double.valueOf(1.0D);
		switch (tu.ordinal()) {
		case 1:
			time = Double.valueOf(value * Double.valueOf(86400000.0D).doubleValue());
			break;
		case 2:
			time = Double.valueOf(value * Double.valueOf("2592000000").doubleValue());
			break;
		case 3:
			time = Double.valueOf(value * Double.valueOf(604800000.0D).doubleValue());
			break;
		case 4:
			time = Double.valueOf(value * Double.valueOf(3600000.0D).doubleValue());
			break;
		case 5:
			time = Double.valueOf(value * Double.valueOf(60000.0D).doubleValue());
			break;
		case 6:
			time = Double.valueOf(value * Double.valueOf(1000.0D).doubleValue());
		}

		return new Date(new Date().getTime() - time.longValue());
	}

	public static int getDateCount(TimeUnit tu, int value) {
		switch (tu.ordinal()) {
		case 1:
			return (value * 1);
		case 2:
			return (value * 30);
		case 3:
			return (value * 7);
		}

		return 1;
	}

	public static Date getClosestDate(Date date, List<Date> list) {
		long min = 9223372036854775807L;
		Date result = null;
		for (Date d : list) {
			long value = Math.abs(date.getTime() - d.getTime());
			if (min > value) {
				min = value;
				result = d;
			}
		}

		if ((list.size() > 0) && (result.getTime() < ((Date) list.get(0)).getTime())) {
			result = null;
		}

		return result;
	}

	public static List<Date> getDateList(TimeUnit tu, int value, int displayCount, Date earliestDay) {
		Double count = Double.valueOf(Math.min(Double.valueOf(getDateCount(tu, value)).doubleValue(),
				getTimeValue(TimeUnit.day, new Date(), earliestDay)));
		double fre = count.doubleValue() / Double.valueOf(displayCount).doubleValue();
		if (fre < 1.0D) {
			displayCount = count.intValue();
			fre = Double.valueOf(1.0D).doubleValue();
		}
		List list = new ArrayList();
		for (int i = 1; i < displayCount + 1; ++i) {
			list.add(getBeforeDay(TimeUnit.day, Double.valueOf(displayCount - i).doubleValue() * fre));
		}

		return list;
	}

	public static boolean isBetweenTime(String start, String end) {
		String now = getTimeString(new Date());
		return ((now.compareTo(start) > 0) && (now.compareTo(end) < 0));
	}

	public static String getDayByToday(int count) {
		Date date = new Date();
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(5, count);
		date = calendar.getTime();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String dateString = formatter.format(date);

		return dateString;
	}

	public static Date getDayBeforeTaody(int count) {
		Date date = new Date();
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(5, -count);
		date = calendar.getTime();

		return date;
	}

	public static Date getDayBeforeDay(Date date, int count) {
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(5, -count);
		date = calendar.getTime();

		return date;
	}

	public static Date getDateByTodayWithTime(int count, String time) {
		Date date = new Date();
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(5, 1);
		date = calendar.getTime();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String dateString = formatter.format(date);

		return getDateFromString(dateString + " " + time);
	}

	public static boolean isYeaterday(Date oldTime) throws ParseException {
		Date newTime = new Date();

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String todayStr = format.format(newTime);
		Date today = format.parse(todayStr);

		return ((today.getTime() - oldTime.getTime() > 0L) && (today.getTime() - oldTime.getTime() <= 86400000L));
	}

	public static boolean isDaybeforeYeaterday(Date oldTime) throws ParseException {
		Date newTime = new Date();

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String todayStr = format.format(newTime);
		Date today = format.parse(todayStr);

		return ((today.getTime() - oldTime.getTime() > 86400000L)
				&& (today.getTime() - oldTime.getTime() <= 172800000L));
	}

	public static boolean isToady(Date oldTime) throws ParseException {
		Date newTime = new Date();

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String todayStr = format.format(newTime);
		Date today = format.parse(todayStr);

		return (today.getTime() - oldTime.getTime() <= 0L);
	}

	public static int getWeeksAgo(Date oldTime) throws ParseException {
		Date newTime = new Date();

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String todayStr = format.format(newTime);
		Date today = format.parse(todayStr);

		int n = (int) ((today.getTime() - oldTime.getTime()) / 604800000L);

		return n;
	}

	public static int getDaysAgo(Date oldTime) throws ParseException {
		Date newTime = new Date();

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String todayStr = format.format(newTime);
		Date today = format.parse(todayStr);

		int n = (int) ((today.getTime() - oldTime.getTime()) / 86400000L);

		return n;
	}

	public static void main(String[] args) {
		try {
			System.out.println(getWeeksAgo(getDateFromString("2014-06-18 14:24:26")));
			System.out.println(getDaysAgo(getDateFromString("2014-06-18 14:24:26")));
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	public static enum TimeUnit {
		day, month, week, hour, minute, second;
	}
}
