package com.asura.tools.data.mongo;

import com.asura.tools.util.cache.SimpleCache;

public class MongoService {
	private static SimpleCache<String, MongoHandler> handler = new SimpleCache(1000);

	public static void addRecord(String host, String db, String table, String[] keys, String[] index, String sql) {
		if (!(handler.iscached(host)))
			handler.cache(host, new MongoHandler(host));
	}

	public static void execute(String host, String db, String table, String sql) {
	}
}
