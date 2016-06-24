package com.asura.tools.monitor;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedHashMap;

import com.asura.tools.monitor.MonitedValue.MonitorLevel;
import com.asura.tools.util.SerializeUtil;

public class MonitedValue implements Serializable {
	private static final long serialVersionUID = 5567749922039951259L;
	private LinkedHashMap<String, String> keyValues;
	private static final String START = "logstart";
	private static final String END = "logend";
	public static final String MESSAGE = "message";
	private String key;
	private Date date;
	private long eventId;
	private MonitorLevel level;

	public MonitedValue() {
		this.keyValues = new LinkedHashMap();
		this.date = new Date();
	}

	public MonitedValue(String message) {
		this.keyValues = new LinkedHashMap();
		this.date = new Date();
		this.keyValues.put("message", message);
	}

	public boolean isMessage() {
		return ((this.keyValues.size() == 1) && (this.keyValues.containsKey("message")));
	}

	public String getMessage() {
		return ((String) this.keyValues.get("message"));
	}

	public void addFieldValue(String field, String value) {
		this.keyValues.put(field, value);
	}

	public String[] getFields() {
		return ((String[]) this.keyValues.keySet().toArray(new String[0]));
	}

	public String getFieldValue(String field) {
		return ((String) this.keyValues.get(field));
	}

	public String toMachineString() {
		return "logstart" + MonitedValueUtil.toLogString(this) + "logend";
	}

	public String toManualString() {
		return "key:" + this.key + " date:" + this.date + " value:" + this.keyValues.toString();
	}

	public static MonitedValue fromLogString(String logString) {
		if ((logString.contains("logstart")) && (logString.contains("logend"))) {
			String value = logString.substring(logString.indexOf("logstart"), logString.indexOf("logend"))
					.replace("logstart", "").replace("logend", "");
			MonitedValue mv = MonitedValueUtil.fromLogString(value);
			if (mv == null) {
				return ((MonitedValue) SerializeUtil.ByteStringToObject(value));
			}
			return mv;
		}

		return null;
	}

	public MonitorLevel getLevel() {
		return this.level;
	}

	public void setLevel(MonitorLevel level) {
		this.level = level;
	}

	public String getKey() {
		return this.key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Date getDate() {
		return this.date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public long getEventId() {
		return this.eventId;
	}

	public void setEventId(long eventId) {
		this.eventId = eventId;
	}

	public static enum MonitorLevel {
		info, debug, error, warn, all;
	}
}
