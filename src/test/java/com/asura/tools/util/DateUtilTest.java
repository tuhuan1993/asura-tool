package com.asura.tools.util;

import java.util.Date;

import org.junit.Test;

public class DateUtilTest {

	@Test
	public void test() {
		String date="2016-07-31";

		System.out.println(DateUtil.getTodayDate());
		System.out.println(DateUtil.getDatePeriodsByRelativeDays(date, -10));
		System.out.println(DateUtil.getDayByToday(-1));
		System.out.println(DateUtil.getDayByToday(0));
		System.out.println(DateUtil.getDayByToday(1));
		
		Date bDate=DateUtil.getDateFromString(date,"yyyy-MM-dd");
		Date cDate=DateUtil.getDayBeforeDay(bDate, 0);
		System.out.println(DateUtil.getDateString(cDate));
		cDate=DateUtil.getDayBeforeDay(bDate, 1);
		System.out.println(DateUtil.getDateString(cDate));
		cDate=DateUtil.getDayBeforeDay(bDate, -1);
		System.out.println(DateUtil.getDateString(cDate));
	}

}
