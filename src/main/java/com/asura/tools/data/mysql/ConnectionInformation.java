package com.asura.tools.data.mysql;

public class ConnectionInformation {
	private String host;
	private String port;
	private String userName;
	private String password;
	private String dbName;

	public String getHost() {
		return this.host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getPort() {
		return this.port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDbName() {
		return this.dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public String toString() {
		return "h:" + this.host + "port:" + this.port + "user:" + this.userName + "password:" + this.password
				+ "dbName:" + this.dbName;
	}
}
