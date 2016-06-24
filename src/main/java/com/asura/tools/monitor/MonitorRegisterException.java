package com.asura.tools.monitor;

public class MonitorRegisterException extends RuntimeException {
	private static final long serialVersionUID = 449184687971310263L;

	public MonitorRegisterException() {
	}

	public MonitorRegisterException(String message) {
		super(message);
	}

	public MonitorRegisterException(Exception e) {
		super(e);
	}
}