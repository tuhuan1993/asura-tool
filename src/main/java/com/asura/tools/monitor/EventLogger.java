package com.asura.tools.monitor;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;

import org.apache.log4j.Appender;
import org.apache.log4j.RollingFileAppender;
import org.apache.log4j.SimpleLayout;

public class EventLogger {
	public static final String MONITOR_KEY = "monitor";
	private static HashMap<String, MultiLogger> loggerMap = new HashMap();
	private static HashMap<String, Long> idMap = new HashMap();
	private static HashMap<Long, String> keyMap = new LinkedHashMap();

	public static synchronized long startNewEvent(String key) {
		long id = new Date().getTime();
		while (keyMap.containsKey(Long.valueOf(id))) {
			id += 1L;
		}

		if (idMap.containsKey(key)) {
			long oldId = ((Long) idMap.get(key)).longValue();
			idMap.remove(key);
			keyMap.remove(Long.valueOf(oldId));
		}

		idMap.put(key, Long.valueOf(id));
		keyMap.put(Long.valueOf(id), key);

		return id;
	}

	public static long getCurrentEventId(String key) {
		return ((Long) idMap.get(key)).longValue();
	}

	public static boolean containsEventKey(String key) {
		return idMap.containsKey(key);
	}

	public static String logIdInfo() {
		HashMap map = new HashMap();
		map.put("id size less than 10000", Integer.valueOf(keyMap.size()));
		if (keyMap.size() > 10000) {
			for (Long id : keyMap.keySet()) {
				String key = (String) keyMap.get(id);
				if (!(map.containsKey(key))) {
					map.put(key, Integer.valueOf(0));
				}
				map.put(key, Integer.valueOf(((Integer) map.get(key)).intValue() + 1));
			}
		}

		return map.toString();
	}

	public static void endEvent(long eventId) {
		keyMap.remove(Long.valueOf(eventId));
	}

	private static MultiLogger getLogger(long eventId) {
		return getLogger((String) keyMap.get(Long.valueOf(eventId)));
	}

	private static synchronized MultiLogger getLogger(String key) {
		if (!(loggerMap.containsKey(key))) {
			Log4jLogger man = new Log4jLogger(key + "manual");
			man.addFileAppender(getAppender(key, false));
			Log4jLogger machine = new Log4jLogger(key + "machine");
			machine.addFileAppender(getAppender(key, true));
			MultiLogger ml = new MultiLogger(man, machine);
			loggerMap.put(key, ml);
		}

		return ((MultiLogger) loggerMap.get(key));
	}

	private static Appender getAppender(String key, boolean machine) {
		RollingFileAppender rfa = null;
		try {
			if (machine) {
				String folder = FileHierarchy.getMachineEventsFolder() + key + "/";
				File file = new File(folder);
				if (!(file.exists())) {
					file.mkdirs();
				}
				rfa = new RollingFileAppender(new SimpleLayout(), folder + key + ".log");
			} else {
				String folder = FileHierarchy.getManualEventsFolder() + key + "/";
				File file = new File(folder);
				if (!(file.exists())) {
					file.mkdirs();
				}
				rfa = new RollingFileAppender(new SimpleLayout(), folder + key + ".log");
			}
			rfa.setMaxFileSize("1MB");
			rfa.setMaxBackupIndex(10000);
			rfa.setEncoding("UTF8");
			rfa.activateOptions();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (MonitorException e) {
			e.printStackTrace();
		}
		return rfa;
	}

	public static void info(MonitedValue value, String key) {
		if (idMap.containsKey(key)) {
			info(value, getCurrentEventId(key));
		} else {
			value.setLevel(MonitedValue.MonitorLevel.info);
			value.setEventId(new Date().getTime());
			value.setKey(key);

			getLogger(key).info(value.toManualString(), value.toMachineString());
		}
	}

	public static void info(String message, String key) {
		info(new MonitedValue(message), key);
	}

	public static void info(MonitedValue value, long eventId) {
		value.setLevel(MonitedValue.MonitorLevel.info);
		value.setEventId(eventId);
		value.setKey((String) keyMap.get(Long.valueOf(eventId)));

		getLogger(eventId).info(value.toManualString(), value.toMachineString());
	}

	public static void error(MonitedValue value, long eventId) {
		value.setLevel(MonitedValue.MonitorLevel.error);
		value.setEventId(eventId);
		value.setKey((String) keyMap.get(Long.valueOf(eventId)));
		getLogger(eventId).error(value.toManualString(), value.toMachineString());
	}

	public static void error(MonitedValue value, String key) {
		if (idMap.containsKey(key)) {
			error(value, getCurrentEventId(key));
		} else {
			value.setLevel(MonitedValue.MonitorLevel.error);
			value.setEventId(new Date().getTime());
			value.setKey(key);

			getLogger(key).error(value.toManualString(), value.toMachineString());
		}
	}

	public static void error(String message, String key) {
		error(new MonitedValue(message), key);
	}

	public static void debug(MonitedValue value, long eventId) {
		value.setLevel(MonitedValue.MonitorLevel.debug);
		value.setEventId(eventId);
		value.setKey((String) keyMap.get(Long.valueOf(eventId)));
		getLogger(eventId).debug(value.toManualString(), value.toMachineString());
	}

	public static void debug(MonitedValue value, String key) {
		if (idMap.containsKey(key)) {
			debug(value, getCurrentEventId(key));
		} else {
			value.setLevel(MonitedValue.MonitorLevel.debug);
			value.setEventId(new Date().getTime());
			value.setKey(key);

			getLogger(key).debug(value.toManualString(), value.toMachineString());
		}
	}

	public static void debug(String message, String key) {
		debug(new MonitedValue(message), key);
	}

	public static void warn(MonitedValue value, long eventId) {
		value.setLevel(MonitedValue.MonitorLevel.warn);
		value.setEventId(eventId);
		value.setKey((String) keyMap.get(Long.valueOf(eventId)));
		getLogger(eventId).warn(value.toManualString(), value.toMachineString());
	}

	public static void warn(MonitedValue value, String key) {
		if (idMap.containsKey(key)) {
			warn(value, getCurrentEventId(key));
		} else {
			value.setLevel(MonitedValue.MonitorLevel.warn);
			value.setEventId(new Date().getTime());
			value.setKey(key);

			getLogger(key).warn(value.toManualString(), value.toMachineString());
		}
	}

	public static void warn(String message, String key) {
		warn(new MonitedValue(message), key);
	}
}
