package com.asura.tools.sql;

import java.io.Serializable;

public interface ISQL extends Serializable {
	public abstract String getSQLString(DBType paramDBType);

	public static enum DBType {
		mysql, oracle;
	}
}