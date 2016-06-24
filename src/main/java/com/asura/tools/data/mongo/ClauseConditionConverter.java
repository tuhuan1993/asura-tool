package com.asura.tools.data.mongo;

import com.asura.tools.sql.BooleanCondition;
import com.asura.tools.sql.ISQL;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class ClauseConditionConverter implements IMongoConverter {
	public boolean canConvert(ISQL sql) {
		return sql instanceof BooleanCondition;
	}

	public DBObject convert(ISQL sql) {
		BooleanCondition bc = (BooleanCondition) sql;

		DBObject ob = null;

		if (bc.getAndList().size() > 0) {
			BasicDBList list = new BasicDBList();
			for (ISQL con : bc.getAndList()) {
				DBObject and = MongoConverter.convert(con);
				if (and != null) {
					list.add(and);
				}
			}

			if (list.size() > 0) {
				ob = new BasicDBObject("$and", list);
			}
		}

		if (bc.getOrList().size() > 0) {
			BasicDBList list = new BasicDBList();
			for (ISQL con : bc.getOrList()) {
				DBObject or = MongoConverter.convert(con);
				if (or != null) {
					list.add(or);
				}
			}

			if (list.size() > 0) {
				if (ob == null) {
					ob = new BasicDBObject("$or", list);
				} else {
					BasicDBList newList = new BasicDBList();
					newList.add(ob);
					newList.add(new BasicDBObject("$or", list));

					ob = new BasicDBObject("$and", newList);
				}
			}
		}

		return ob;
	}
}
