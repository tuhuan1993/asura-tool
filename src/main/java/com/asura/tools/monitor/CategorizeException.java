package com.asura.tools.monitor;

public class CategorizeException extends Exception {
	private static final long serialVersionUID = 3259537138127094594L;

	public CategorizeException() {
	}

	public CategorizeException(String message) {
		super(message);
	}

	public CategorizeException(Exception e) {
		super(e);
	}
}
