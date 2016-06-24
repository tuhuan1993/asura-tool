package com.asura.tools.data.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.commons.lang.StringUtils;

public class MysqlConnection {
	private static HashMap<String, java.sql.Connection> conMap = new HashMap();

	public static synchronized Connection getInstance(ConnectionInformation pro) {
		com.mysql.jdbc.Connection connection;
		try {
			if (StringUtils.isEmpty(pro.getHost()) || StringUtils.isEmpty(pro.getPort())
					|| StringUtils.isEmpty(pro.getDbName()))
				throw new RuntimeException(
						"Host不能为空,Port不能为空,数据库名字不能为空");
			if (!conMap.containsKey(pro.toString()) || conMap.get(pro.toString()) == null
					|| ((Connection) conMap.get(pro.toString())).isClosed()) {
				String url = (new StringBuilder("jdbc:mysql://")).append(pro.getHost()).append(":")
						.append(pro.getPort()).append("/").append(pro.getDbName())
						.append("?useUnicode=true&characterEncoding=UTF8&zeroDateTimeBehavior=convertToNull")
						.toString();
				Class.forName("com.mysql.jdbc.Driver");
				conMap.put(pro.toString(), DriverManager.getConnection(url, pro.getUserName(), pro.getPassword()));
			} else {
				connection = (com.mysql.jdbc.Connection) conMap.get(pro.toString());
				try {
					connection.ping();
				} catch (Exception e) {
					connection.close();
					conMap.put(pro.toString(), getNewInstance(pro));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		connection = (com.mysql.jdbc.Connection) conMap.get(pro.toString());
		try {
			connection.ping();
		} catch (Exception e) {
			try {
				connection.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			conMap.put(pro.toString(), getNewInstance(pro));
		}
		return (Connection) conMap.get(pro.toString());
	}

	public static java.sql.Connection getNewInstance(ConnectionInformation pro) {
		String url = "jdbc:mysql://" + pro.getHost() + ":" + pro.getPort() + "/" + pro.getDbName()
				+ "?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull";
		try {
			Class.forName("com.mysql.jdbc.Driver");
			return DriverManager.getConnection(url, pro.getUserName(), pro.getPassword());
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		return null;
	}
}
