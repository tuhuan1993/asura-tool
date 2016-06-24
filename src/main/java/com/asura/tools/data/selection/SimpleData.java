package com.asura.tools.data.selection;

import java.util.HashMap;

public class SimpleData implements IFeaturable {
	private HashMap<String, String> map;

	public SimpleData() {
		this.map = new HashMap();
	}

	public void addFeatureValue(String feature, String value) {
		this.map.put(feature, value);
	}

	public void addFeatureValue(String feature, Object value) {
		this.map.put(feature, value.toString());
	}

	public String getFeatureValue(String feature) {
		if (this.map.containsKey(feature)) {
			return ((String) this.map.get(feature));
		}
		return "";
	}

	public Object getObject() {
		return this.map;
	}

	public boolean hasFeature(String feature) {
		return this.map.containsKey(feature);
	}

	public String toString() {
		return this.map.toString();
	}
}