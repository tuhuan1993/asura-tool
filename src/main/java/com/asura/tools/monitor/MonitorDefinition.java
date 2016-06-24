package com.asura.tools.monitor;

import java.util.HashSet;
import java.util.List;

public class MonitorDefinition {
	private List<Category> categories;
	private String monitorUrl;
	private int registerInterval;
	private HashSet<String> keySet;

	public List<Category> getCategories() {
		return this.categories;
	}

	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}

	public String getMonitorUrl() {
		return this.monitorUrl;
	}

	public void setMonitorUrl(String monitorUrl) {
		this.monitorUrl = monitorUrl;
	}

	public int getRegisterInterval() {
		return this.registerInterval;
	}

	public void setRegisterInterval(int registerInterval) {
		this.registerInterval = registerInterval;
	}

	public boolean containsEvent(String key) {
		if (this.keySet == null) {
			this.keySet = new HashSet();
			for (Category cate : this.categories) {
				this.keySet.add(cate.getResult().getKey());
			}
		}

		return this.keySet.contains(key);
	}

	public String getLocation() {
		if (this.categories.size() > 0) {
			return ((MonitorInfo) ((Category) this.categories.get(0)).getResult()).getPath();
		}

		return "monitorlogs";
	}
}
