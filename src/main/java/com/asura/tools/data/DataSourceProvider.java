package com.asura.tools.data;

import java.util.List;

import com.asura.tools.data.mongo.MongoConnection;
import com.asura.tools.data.mongo.MongoHandler;
import com.asura.tools.data.mysql.ConnectionInformation;
import com.asura.tools.data.mysql.MysqlHandler;
import com.asura.tools.data.oracle.OracleHandler;
import com.asura.tools.sql.SelectSQL;
import com.asura.tools.util.cache.SimpleCache;

public class DataSourceProvider {
	private static SimpleCache<String, Object> cache = new SimpleCache(1000);
	private static final String MYSQL = "mysql";
	private static final String MONGO = "mongo";
	private static final String ORACLE = "oracle";

	public static MysqlHandler getMysqlHandler(String key) throws DataException {
		String cKey = "mysql" + key;
		if (!(cache.iscached(key))) {
			try {
				String config = findDataSource("mysql", key);
				ConnectionInformation ci = ConnectionInformation.fromXml(config);

				cache.cache(cKey, new MysqlHandler(ci));
			} catch (Exception e) {
				throw new DataException("mysql datasource " + key + " is not configed correctly.");
			}
		}

		return ((MysqlHandler) cache.get(cKey));
	}

	public static OracleHandler getOracleHandler(String key) throws DataException {
		String cKey = "oracle" + key;
		if (!(cache.iscached(key))) {
			try {
				String config = findDataSource("oracle", key);
				ConnectionInformation ci = ConnectionInformation.fromXml(config);

				cache.cache(cKey, new OracleHandler(ci));
			} catch (Exception e) {
				throw new DataException("mysql datasource " + key + " is not configed correctly.");
			}
		}

		return ((OracleHandler) cache.get(cKey));
	}

	public static MongoHandler getMongoHandler(String key) throws DataException {
		String cKey = (new StringBuilder("mongo")).append(key).toString();
		if (!cache.iscached(key))
			try {
				String config = findDataSource("mongo", key);
				if (config.contains("<") && config.contains(">"))
					cache.cache(cKey, new MongoHandler(MongoConnection.fromXml(config)));
				else
					cache.cache(cKey, new MongoHandler(config));
			} catch (Exception e) {
				throw new DataException((new StringBuilder("mongo datasource ")).append(key)
						.append(" is not configed correctly.").toString());
			}
		return (MongoHandler) cache.get(cKey);

	}

	private static String findDataSource(String type, String key) {
		SelectSQL sql = new SelectSQL("datasource");
		sql.addWhereCondition("type", type);
		sql.addWhereCondition("key", key);
		List list = new MysqlHandler(ConnectionInformation.fromClassLoaderFile("datasource.xml")).selectList(sql);
		if (list.size() > 0) {
			return ((DataRecord) list.get(0)).getFieldValue("config");
		}

		return "";
	}
}
