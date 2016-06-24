package com.asura.tools.monitor;

public class ConfigurationParseException extends LogedException {
	private static final long serialVersionUID = 441163601825861708L;

	public ConfigurationParseException() {
	}

	public ConfigurationParseException(String message) {
		super(message);
	}

	public ConfigurationParseException(String message, String key) {
		super(message, key);
	}

	public ConfigurationParseException(Exception e) {
		super(e);
	}
}