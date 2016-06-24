package com.asura.tools.monitor;

import java.util.Date;

public class DateFilter {
	public static StandardDate MIN_DATE = new StandardDate("2000-01-01 00:00:00");
	private StandardDate start;
	private StandardDate end;

	public DateFilter() {
	}

	public DateFilter(StandardDate start, StandardDate end) {
		this.start = start;
		this.end = end;
	}

	public static StandardDate getMaxDate() {
		return new StandardDate(new Date());
	}

	public static DateFilter getAllDate() {
		return new DateFilter(MIN_DATE, getMaxDate());
	}

	public static DateFilter monthAgo(int month) {
		return new DateFilter(new StandardDate(new Date().getTime() - Long.valueOf(-1702967296 * month).longValue()),
				getMaxDate());
	}

	public static DateFilter dayAgo(int day) {
		return new DateFilter(new StandardDate(new Date().getTime() - Long.valueOf(86400000 * day).longValue()),
				getMaxDate());
	}

	public static DateFilter hourAgo(int hour) {
		return new DateFilter(new StandardDate(new Date().getTime() - Long.valueOf(3600000 * hour).longValue()),
				getMaxDate());
	}

	public static DateFilter minuteAgo(int minute) {
		return new DateFilter(new StandardDate(new Date().getTime() - Long.valueOf(60000 * minute).longValue()),
				getMaxDate());
	}

	public StandardDate getStart() {
		return this.start;
	}

	public void setStart(StandardDate start) {
		this.start = start;
	}

	public StandardDate getEnd() {
		return this.end;
	}

	public void setEnd(StandardDate end) {
		this.end = end;
	}

	public boolean concludeDate(StandardDate date) {
		return date.between(this.start, this.end);
	}
}
