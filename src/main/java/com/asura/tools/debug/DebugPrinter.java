package com.asura.tools.debug;

import java.util.HashMap;
import java.util.HashSet;

public class DebugPrinter {
	private static HashMap<String, DebugPrinter> map = new HashMap();
	private static boolean able;
	private String key;
	private static HashSet<String> enables = new HashSet();
	private static IDebugOutputer outputer;

	static {
		able = true;
	}

	private DebugPrinter(String key) {
		this.key = key;
	}

	public static void setOutputer(IDebugOutputer outputer) {
		outputer = outputer;
	}

	public static synchronized DebugPrinter getInstance(String key) {
		if (!(map.containsKey(key))) {
			map.put(key, new DebugPrinter(key));
		}

		return ((DebugPrinter) map.get(key));
	}

	public static synchronized DebugPrinter getInstance(Object object) {
		String key = object.getClass().getSimpleName();
		if (!(map.containsKey(key))) {
			map.put(key, new DebugPrinter(key));
		}

		return ((DebugPrinter) map.get(key));
	}

	public static void setAble(boolean able) {
		able = able;
	}

	public static void disableKey(String key) {
		enables.remove(key);
	}

	public static void enableKey(String key) {
		enables.add(key);
	}

	public static void clearEnables() {
		enables.clear();
	}

	private boolean isAble() {
		return ((able) && (enables.contains(this.key)));
	}

	public synchronized void print(Object info, String detail) {
		if (isAble()) {
			String tracker = getTrack();
			getOutputer().output(this.key, detail, tracker + "details:" + detail);
			getOutputer().output(this.key, detail, tracker + info.toString());
		}
	}

	public synchronized void printClassDetail(Object info) {
		if (isAble()) {
			String tracker = getTrack();
			String className = getClassName();
			getOutputer().output(this.key, className, tracker + info.toString());
		}
	}

	public synchronized void println() {
		if (isAble())
			getOutputer().output(this.key, "", "");
	}

	public synchronized void println(String detail) {
		if (isAble())
			getOutputer().output(this.key, "", detail);
	}

	public synchronized void print(Object info) {
		if (isAble()) {
			String tracker = getTrack();
			getOutputer().output(this.key, "", tracker + info.toString());
		}
	}

	private String getClassName() {
		Throwable ex = new Throwable();

		StackTraceElement[] stackElements = ex.getStackTrace();

		if ((stackElements != null) && (stackElements.length > 2)) {
			String cn = stackElements[2].getClassName();
			if (cn.contains(".")) {
				cn = cn.substring(cn.lastIndexOf(".") + 1);
			}
			return cn;
		}

		return "";
	}

	private String getTrack() {
		Throwable ex = new Throwable();

		StackTraceElement[] stackElements = ex.getStackTrace();

		if ((stackElements != null) && (stackElements.length > 2)) {
			String cn = stackElements[2].getClassName();
			if (cn.contains(".")) {
				cn = cn.substring(cn.lastIndexOf(".") + 1);
			}
			return cn + " " + stackElements[2].getMethodName() + ":" + stackElements[2].getLineNumber() + "->";
		}

		return "";
	}

	private IDebugOutputer getOutputer() {
		if (outputer == null) {
			outputer = new ConsoleOutputer();
		}

		return outputer;
	}
}
