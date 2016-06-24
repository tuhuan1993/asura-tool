package com.asura.tools.data.multithread;

import com.asura.tools.timer.ReflectionMethod;

public class BackgroundRunner {
	public static void run(Object obj, String methodName, Class<?>[] classes, Object[] params) {
		ReflectionMethod rm = new ReflectionMethod();
		rm.setInstance(obj);
		rm.setMethodName(methodName);
		rm.setClassName(obj.getClass().getName());
		rm.setClasses(classes);
		rm.setParams(params);

		Thread td = new Thread(getRunnable(rm));
		td.start();
	}

	public static void run(Object obj, String methodName) {
		run(obj, methodName, null, null);
	}

	private static Runnable getRunnable(final ReflectionMethod rm) {
		return new Runnable() {
			public void run() {
				rm.run();
			}
		};
	}
}
