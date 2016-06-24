package com.asura.tools.monitor;

import com.asura.tools.monitor.ComparableValue.ResultType;

public class ComparableValue {
	private String value;
	private ResultType type;

	public ComparableValue(String value) {
		this.value = value;
		this.type = ResultType.Equals;
	}

	public ResultType getType() {
		return this.type;
	}

	public void setType(ResultType type) {
		this.type = type;
	}

	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public boolean meet(String value) {
		int cvalue = value.compareTo(this.value);
		if (cvalue > 0)
			return (this.type == ResultType.Bigger);
		if (cvalue == 0) {
			return (this.type == ResultType.Equals);
		}
		return (this.type == ResultType.Smaller);
	}

	public static enum ResultType {
		Bigger, Smaller, Equals;
	}
}
