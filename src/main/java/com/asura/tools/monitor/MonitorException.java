package com.asura.tools.monitor;

public class MonitorException extends Exception {
	private static final long serialVersionUID = 3259537138127094594L;

	public MonitorException() {
	}

	public MonitorException(String message) {
		super(message);
	}

	public MonitorException(Exception e) {
		super(e);
	}
}
