package com.asura.tools.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.asura.tools.data.redis.RedisHandler;

public class RedisUtil {

	private static final Logger logger = LoggerFactory.getLogger(RedisUtil.class);

	public static void syncHashKVs(RedisHandler handler, String key, Map<String, String> result, int batchSize,
			boolean overried) {
		if (!overried) {
			int count = 0;
			Set<String> memFields = handler.hkeys(key);
			memFields.removeAll(result.keySet());
			logger.info("deprecated field size is " + memFields.size() + " for key " + key);
			List<String> subFields = new ArrayList<>();
			for (String field : memFields) {
				subFields.add(field);
				if (++count % batchSize == 0) {
					handler.hdel(key, subFields.toArray(new String[] {}));
					subFields.clear();
					logger.info("delete depreacated field size " + count);
				}
			}
			if (subFields.size() > 0) {
				handler.hdel(key, subFields.toArray(new String[] {}));
				subFields.clear();
				logger.info("delete depreacated field size " + count);
			}
		}

		Map<String, String> subMap = new HashMap<>();
		int count = 0;
		for (Entry<String, String> entry : result.entrySet()) {
			subMap.put(entry.getKey(), entry.getValue());
			if (++count % batchSize == 0) {
				handler.hmset(key, subMap);
				subMap.clear();
				logger.info("added field size " + count);
			}
		}
		if (subMap.size() > 0) {
			handler.hmset(key, subMap);
			subMap.clear();
			logger.info("added field size " + count);
		}
	}
}
