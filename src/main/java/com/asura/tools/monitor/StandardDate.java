package com.asura.tools.monitor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StandardDate {
	private Date date;
	private SimpleDateFormat format;

	public StandardDate() {
		this.date = new Date();
		this.format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	}

	public StandardDate(long time) {
		this.date = new Date(time);
		this.format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	}

	public StandardDate(Date date) {
		this.format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		this.date = date;
	}

	public StandardDate(String date) {
		this.format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			this.date = this.format.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	public String toDateString() {
		return this.format.format(this.date);
	}

	public boolean before(StandardDate date) {
		return this.date.before(date.date);
	}

	public boolean isSame(StandardDate date) {
		return (this.date.getTime() == date.date.getTime());
	}

	public boolean after(StandardDate date) {
		return this.date.after(date.date);
	}

	public boolean between(StandardDate date1, StandardDate date2) {
		return ((date1.date.equals(this.date)) || (date2.date.equals(this.date))
				|| ((date1.date.before(this.date)) && (date2.date.after(this.date))));
	}

	public boolean between(String date1, String date2) {
		try {
			Date d1 = this.format.parse(date1);
			Date d2 = this.format.parse(date2);
			return ((d2.equals(this.date)) || (d1.equals(this.date))
					|| ((d1.before(this.date)) && (d2.after(this.date))));
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return false;
	}
}
