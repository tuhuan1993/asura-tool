package com.asura.tools.monitor;

public class MonitorFilter {
	private DateFilter dateFilter;
	private FieldFilter fieldFilter;
	private String key;
	private MonitedValue.MonitorLevel level;
	private int latestCount;

	public MonitorFilter() {
		this.latestCount = 2147483647;
	}

	public DateFilter getDateFilter() {
		return this.dateFilter;
	}

	public void setDateFilter(DateFilter dateFilter) {
		this.dateFilter = dateFilter;
	}

	public FieldFilter getFieldFilter() {
		return this.fieldFilter;
	}

	public void setFieldFilter(FieldFilter fieldFilter) {
		this.fieldFilter = fieldFilter;
	}

	public String getKey() {
		return this.key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public MonitedValue.MonitorLevel getLevel() {
		return this.level;
	}

	public void setLevel(MonitedValue.MonitorLevel level) {
		this.level = level;
	}

	public int getLatestCount() {
		return this.latestCount;
	}

	public void setLatestCount(int latestCount) {
		this.latestCount = latestCount;
	}
}
