package com.asura.tools.data.selection.ordervalue;

import java.util.HashMap;

import com.asura.tools.util.StringUtil;

public class CharOrderValue implements IOrderValue {
	private static final long serialVersionUID = 6706324246785664306L;
	private HashMap<String, Integer> set;
	private int currentId = 0;

	public CharOrderValue() {
		this.set = new HashMap<>();
	}

	public boolean contains(String value) {
		return true;
	}

	public int getBlockIndex(String value) {
		if (StringUtil.isNullOrEmpty(value)) {
			value = "";
		}

		if (!(this.set.containsKey(value))) {
			this.set.put(value, Integer.valueOf(this.currentId++));
		}

		return ((Integer) this.set.get(value)).intValue();
	}

	public void reset() {
		this.currentId = 0;
		this.set.clear();
	}

	public String toString() {
		return this.set.toString();
	}
}
