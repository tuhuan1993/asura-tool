package com.asura.tools.monitor;

public class MonitorInfo implements ICategoryResult {
	private static final long serialVersionUID = -5600921480509744304L;
	private String host;
	private String path;
	private String key;

	public String getHost() {
		return this.host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getPath() {
		if (!(this.path.endsWith("/"))) {
			return this.path + "/";
		}
		return this.path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getKey() {
		return this.key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String toString() {
		return "host:" + this.host + " path:" + this.path + " key:" + this.key;
	}
}