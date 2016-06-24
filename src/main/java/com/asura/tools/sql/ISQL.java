package com.asura.tools.sql;

import java.io.Serializable;

import com.asura.tools.sql.ISQL.DBType;

public interface ISQL extends Serializable {
	public abstract String getSQLString(DBType paramDBType);

	public static enum DBType {
		mysql, oracle;
	}
}