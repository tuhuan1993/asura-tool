package com.asura.tools.debug;

import java.util.ArrayList;
import java.util.List;

import com.asura.tools.util.StringUtil;

public class StringConsoleOutputer implements IDebugOutputer {
	private List<String> list;

	public StringConsoleOutputer() {
		this.list = new ArrayList();
	}

	public void output(String key, String detail, String value) {
		this.list.add(value);
	}

	public String getLog() {
		return StringUtil.getStringFromStrings(this.list, "\n");
	}
}
