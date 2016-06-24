package com.asura.tools.util.performance;

public class TimeRecord {
	private long total;
	private long count;

	public void addTime(long time) {
		this.total += time;
		this.count += 1L;
	}

	public long getTotal() {
		return this.total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public long getCount() {
		return this.count;
	}

	public void setCount(long count) {
		this.count = count;
	}

	public long getAverage() {
		if (this.count == 0L) {
			return -1L;
		}
		return (this.total / this.count);
	}
}
