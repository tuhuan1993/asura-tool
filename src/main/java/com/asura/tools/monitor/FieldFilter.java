package com.asura.tools.monitor;

import java.util.HashMap;

public class FieldFilter {
	private HashMap<String, ComparableValue> keyValues;

	public FieldFilter() {
		this.keyValues = new HashMap();
	}

	public void addFilter(String filterName, ComparableValue filterValue) {
		this.keyValues.put(filterName, filterValue);
	}

	public boolean meet(MonitedValue mValue) {
		for (String name : this.keyValues.keySet()) {
			String value = mValue.getFieldValue(name);
			ComparableValue cv = (ComparableValue) this.keyValues.get(name);
			if (!(cv.meet(value))) {
				return false;
			}
		}

		return true;
	}
}
