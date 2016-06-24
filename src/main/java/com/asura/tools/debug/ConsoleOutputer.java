package com.asura.tools.debug;

public class ConsoleOutputer implements IDebugOutputer {
	public void output(String key, String detail, String value) {
		System.out.println(value);
	}
}
