package com.asura.tools.monitor;

public class LogedException extends Exception {
	private static final long serialVersionUID = -402377976332647347L;

	public LogedException() {
	}

	public LogedException(String message) {
		super(message);
	}

	public LogedException(String message, String key) {
		super(message);
		EventLogger.error(new MonitedValue(message), key);
	}

	public LogedException(Exception e) {
		super(e);
	}
}
