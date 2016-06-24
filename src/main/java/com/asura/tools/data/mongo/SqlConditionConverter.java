package com.asura.tools.data.mongo;

import java.util.regex.Pattern;

import com.asura.tools.sql.ISQL;
import com.asura.tools.sql.SQLCondition;
import com.asura.tools.util.math.NumberUtil;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class SqlConditionConverter implements IMongoConverter {
	public DBObject convert(ISQL sql) {
		SQLCondition con = (SQLCondition) sql;

		Object o = getValue(con);

		DBObject ob = new BasicDBObject(con.getField(), o);

		if (con.getCondition().equals("="))
			ob = new BasicDBObject(con.getField(), o);
		else if (con.getCondition().equals(">"))
			ob = new BasicDBObject(con.getField(), new BasicDBObject("$gt", o));
		else if (con.getCondition().equals(">="))
			ob = new BasicDBObject(con.getField(), new BasicDBObject("$gte", o));
		else if (con.getCondition().equals("<"))
			ob = new BasicDBObject(con.getField(), new BasicDBObject("$lt", o));
		else if (con.getCondition().equals("<="))
			ob = new BasicDBObject(con.getField(), new BasicDBObject("$lte", o));
		else if (con.getCondition().equals("<>"))
			ob = new BasicDBObject(con.getField(), new BasicDBObject("$ne", o));
		else if (con.getCondition().equals("like")) {
			ob = new BasicDBObject(con.getField(), Pattern.compile(con.getValue(), 2));
		}

		return ob;
	}

	public boolean canConvert(ISQL sql) {
		return sql instanceof SQLCondition;
	}

	private Object getValue(SQLCondition con) {
		if (con.isNumber()) {
			return Double.valueOf(NumberUtil.getDouble(con.getValue()));
		}

		return con.getValue();
	}
}
