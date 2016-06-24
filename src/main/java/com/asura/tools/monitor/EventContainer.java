package com.asura.tools.monitor;

import java.util.ArrayList;
import java.util.List;

public class EventContainer {
	private String name;
	private String description;
	private List<MonitorInfo> infoList;

	public EventContainer() {
		this.infoList = new ArrayList();
	}

	public void addMonitorInfo(MonitorInfo info) {
		this.infoList.add(info);
	}

	public void removeMonitorInfo(MonitorInfo info) {
		this.infoList.remove(info);
	}

	public List<MonitorInfo> getInfoList() {
		return this.infoList;
	}

	public void setInfoList(List<MonitorInfo> infoList) {
		this.infoList = infoList;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
