package com.asura.tools.monitor;

public class FileHierarchy {
	private static final String MACHINE = "machine";
	private static final String MANUAL = "manual";
	private static final String NORMAL = "normal";
	private static final String EVENTS = "events";
	private static final String RECORDS = "records";
	private static String path = "monitorlogs";

	public static void setPath(String path) {
		path = path;
	}

	private static String getPath() throws MonitorException {
		if (!(path.endsWith("/"))) {
			path += "/";
		}

		return path;
	}

	public static String getMachineFolder(String path) {
		return getStandardPath(path) + "machine";
	}

	public static String getManualFolder(String path) {
		return getStandardPath(path) + "manual";
	}

	public static String getMachineNormalFolder(String path) {
		return getStandardPath(path) + "machine" + "/" + "normal" + "/";
	}

	public static String getManualNormalFolder(String path) {
		return getStandardPath(path) + "manual" + "/" + "normal" + "/";
	}

	public static String getMachineRecordsFolder(String path) {
		return getStandardPath(path) + "machine" + "/" + "records" + "/";
	}

	public static String getManualRecordsFolder(String path) {
		return getStandardPath(path) + "manual" + "/" + "records" + "/";
	}

	public static String getMachineEventsFolder(String path) {
		return getStandardPath(path) + "machine" + "/" + "events" + "/";
	}

	private static String getStandardPath(String path) {
		if (path.endsWith("/")) {
			return path;
		}
		return path + "/";
	}

	public static String getManualEventsFolder(String path) {
		return getStandardPath(path) + "manual" + "/" + "events" + "/";
	}

	public static String getMachineFolder() throws MonitorException {
		return getPath() + "machine";
	}

	public static String getManualFolder() throws MonitorException {
		return getPath() + "manual";
	}

	public static String getMachineNormalFolder() throws MonitorException {
		return getPath() + "machine" + "/" + "normal" + "/";
	}

	public static String getManualNormalFolder() throws MonitorException {
		return getPath() + "manual" + "/" + "normal" + "/";
	}

	public static String getMachineRecordsFolder() throws MonitorException {
		return getPath() + "machine" + "/" + "records" + "/";
	}

	public static String getManualRecordsFolder() throws MonitorException {
		return getPath() + "manual" + "/" + "records" + "/";
	}

	public static String getMachineEventsFolder() throws MonitorException {
		return getPath() + "machine" + "/" + "events" + "/";
	}

	public static String getManualEventsFolder() throws MonitorException {
		return getPath() + "manual" + "/" + "events" + "/";
	}
}
