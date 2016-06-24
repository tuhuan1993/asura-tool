package com.asura.tools.debug;

import java.util.HashMap;
import java.util.HashSet;

public class SpendTimer {
	private static HashMap<String, SpendTimer> map = new HashMap();
	private static boolean able;
	private long lastTime;
	private long lastNTime;
	private String key;
	private static HashSet<String> disableds = new HashSet();
	private static HashSet<String> enables = new HashSet();

	static {
		able = true;
	}

	private SpendTimer(String key) {
		this.key = key;
	}

	public static synchronized SpendTimer getInstance(String key) {
		if (!(map.containsKey(key))) {
			map.put(key, new SpendTimer(key));
		}

		return ((SpendTimer) map.get(key));
	}

	public static synchronized SpendTimer getInstance(Object object) {
		String key = object.getClass().getSimpleName();
		if (!(map.containsKey(key))) {
			map.put(key, new SpendTimer(key));
		}

		((SpendTimer) map.get(key)).startTime();
		return ((SpendTimer) map.get(key));
	}

	public static void setAble(boolean able) {
		able = able;
	}

	public static void disableKey(String key) {
		disableds.add(key);
	}

	public static void enableKey(String key) {
		enables.add(key);
	}

	private boolean isAble() {
		return ((able) && (!(disableds.contains(this.key))) && (enables.contains(this.key)));
	}

	public void startTime() {
		this.lastTime = System.currentTimeMillis();
		this.lastNTime = System.nanoTime();
		isAble();
	}

	public synchronized void printSpendTime() {
		if (isAble()) {
			long current = System.currentTimeMillis();

			if (current - this.lastTime > 1L) {
				System.out.println(this.key + ":" + (current - this.lastTime));
			}

			this.lastTime = current;
		}
	}

	public synchronized void printNanoSpendTime() {
		if (isAble()) {
			long current = System.nanoTime();

			System.out.println(this.key + ":" + (current - this.lastNTime));

			this.lastNTime = current;
		}
	}

	public synchronized void printNanoSpendTime(String name) {
		if (isAble()) {
			long current = System.nanoTime();

			System.out.print(this.key + ":" + (current - this.lastNTime));
			System.out.println(" details:" + name);

			this.lastNTime = current;
		}
	}

	public synchronized void printSpendTime(String name) {
		if (isAble()) {
			long current = System.currentTimeMillis();
			if (current - this.lastTime > 1L) {
				System.out.print(this.key + ":" + (current - this.lastTime));
				System.out.println(" details:" + name);
			}

			this.lastTime = current;
		}
	}

	public synchronized long getSpendTime(String name) {
		long current = System.currentTimeMillis();

		long time = current - this.lastTime;

		this.lastTime = current;

		return time;
	}

	public synchronized void printSpendTime(String name, int min) {
		if (isAble()) {
			long current = System.currentTimeMillis();

			long time = current - this.lastTime;
			if (time >= min) {
				System.out.print(this.key + ":" + (current - this.lastTime));
				System.out.println(" details:" + name);
			}

			this.lastTime = current;
		}
	}
}
