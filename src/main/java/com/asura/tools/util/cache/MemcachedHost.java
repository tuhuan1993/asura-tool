package com.asura.tools.util.cache;

public class MemcachedHost {
	private String host;
	private int port;

	public MemcachedHost() {
	}

	public MemcachedHost(String host, int port) {
		this.host = host;
		this.port = port;
	}

	public String getHost() {
		return this.host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return this.port;
	}

	public void setPort(int port) {
		this.port = port;
	}
}
