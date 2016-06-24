package com.asura.tools.monitor;

import java.util.ArrayList;
import java.util.List;

public class EventRequest {
	private List<String> paths;
	private MonitorFilter filter;

	public EventRequest() {
		this.filter = new MonitorFilter();
		this.paths = new ArrayList();
	}

	public void addMonitorInfo(MonitorInfo info) throws MonitorException {
		if ((this.filter.getKey() != null) && (!(info.getKey().equals(this.filter.getKey())))) {
			throw new MonitorException("不同的事件不能合并查看\nfilterKey:" + this.filter.getKey() + " infoKey:" + info.getKey());
		}
		this.paths.add(info.getPath());
		this.filter.setKey(info.getKey());
	}

	public MonitorFilter getFilter() {
		return this.filter;
	}

	public void setFilter(MonitorFilter filter) {
		this.filter = filter;
	}

	public String[] getPaths() {
		return ((String[]) this.paths.toArray(new String[0]));
	}
}
