package com.asura.tools.util.performance;

public class TimeScope implements Comparable<TimeScope> {
	private long up;
	private long low;

	public TimeScope() {
	}

	public TimeScope(long low, long up) {
		this.up = up;
		this.low = low;
	}

	public long getUp() {
		return this.up;
	}

	public void setUp(long up) {
		this.up = up;
	}

	public long getLow() {
		return this.low;
	}

	public void setLow(long low) {
		this.low = low;
	}

	public int hashCode() {
		return String.valueOf(this.up + this.low).hashCode();
	}

	public boolean equals(Object obj) {
		return (hashCode() == obj.hashCode());
	}

	public int compareTo(TimeScope o) {
		if (this.up < o.up)
			return -1;
		if (this.up == o.up) {
			return 0;
		}
		return 1;
	}
}