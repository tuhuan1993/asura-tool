package com.asura.tools.data.mongo;

import com.asura.tools.sql.ISQL;
import com.mongodb.DBObject;

public interface IMongoConverter {
	public  DBObject convert(ISQL paramISQL);

	public  boolean canConvert(ISQL paramISQL);
}
