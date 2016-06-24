package com.asura.tools.data.mongo;

import java.util.ArrayList;
import java.util.List;

import com.asura.tools.sql.ISQL;
import com.mongodb.DBObject;

public class MongoConverter {
	private static List<IMongoConverter> converters = new ArrayList();

	static {
		converters.add(new ClauseConditionConverter());
		converters.add(new SqlConditionConverter());
	}

	public static DBObject convert(ISQL sql) {
		for (IMongoConverter converter : converters) {
			if (converter.canConvert(sql)) {
				return converter.convert(sql);
			}
		}

		return null;
	}
}
