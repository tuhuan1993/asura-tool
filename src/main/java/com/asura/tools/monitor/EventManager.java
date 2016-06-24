package com.asura.tools.monitor;

import java.util.HashMap;

public class EventManager {
	public HashMap<String, EventContainer> containerList;

	public EventManager() {
		this.containerList = new HashMap();
	}

	public void addEventContainer(EventContainer container) {
		this.containerList.put(container.getName(), container);
	}

	public void removeEventContainer(EventContainer container) {
		this.containerList.remove(container.getName());
	}

	public EventContainer[] getEventContainers() {
		return ((EventContainer[]) this.containerList.values().toArray(new EventContainer[0]));
	}
}
