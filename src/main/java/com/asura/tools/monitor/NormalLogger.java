package com.asura.tools.monitor;

import java.io.IOException;

import org.apache.log4j.RollingFileAppender;
import org.apache.log4j.SimpleLayout;

public class NormalLogger {
	private static Log4jLogger logger = new Log4jLogger(NormalLogger.class.getName());

	static {
		RollingFileAppender rfa = null;
		try {
			rfa = new RollingFileAppender(new SimpleLayout(), FileHierarchy.getManualNormalFolder() + "normal.log");

			rfa.setEncoding("UTF8");
			rfa.activateOptions();
			logger.addFileAppender(rfa);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (MonitorException e) {
			e.printStackTrace();
		}
	}

	public static void info(String message, String key) {
		MonitedValue mv = new MonitedValue(message);
		mv.setKey(key);
		mv.setLevel(MonitedValue.MonitorLevel.info);
		mv.setEventId(1L);
		logger.info(mv.toMachineString());
	}

	public static void error(String message, String key) {
		MonitedValue mv = new MonitedValue(message);
		mv.setKey(key);
		mv.setLevel(MonitedValue.MonitorLevel.info);
		mv.setEventId(1L);
		logger.error(mv.toMachineString());
	}

	public static void warn(String message, String key) {
		MonitedValue mv = new MonitedValue(message);
		mv.setKey(key);
		mv.setLevel(MonitedValue.MonitorLevel.info);
		mv.setEventId(1L);
		logger.warn(mv.toMachineString());
	}
}
