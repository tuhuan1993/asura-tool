package com.asura.tools.util;

public class PointUtil {

	private static int count = 0;

	public static void resetPoint() {
		count = 0;
	}

	public static int getPoint() {
		return count;
	}

	public static boolean triggerred(int cnt) {
		if (++count % cnt == 0) {
			return true;
		} else {
			return false;
		}
	}

}
