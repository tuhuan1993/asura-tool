package com.asura.tools.util;

public class LongCounter {
	private long count;

	public LongCounter() {
		this.count = 0L;
	}

	public void increase() {
		this.count += 1L;
	}

	public void decrease() {
		this.count -= 1L;
	}

	public void increase(int i) {
		this.count += i;
	}

	public void decrease(int i) {
		this.count -= i;
	}

	public long getCount() {
		return this.count;
	}
}
