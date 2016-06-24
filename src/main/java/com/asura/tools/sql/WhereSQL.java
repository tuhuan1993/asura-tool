package com.asura.tools.sql;

import com.asura.tools.util.SerializeUtil;
import com.asura.tools.util.StringUtil;

public class WhereSQL implements ISQL {
	private static final long serialVersionUID = 7905349494437641117L;
	private FieldValuesSQL fieldValues;

	public WhereSQL() {
		this.fieldValues = new FieldValuesSQL(" and ");
	}

	public void addCondition(String field, String value) {
		this.fieldValues.addFieldValue(field, value);
	}

	public void addCondition(SQLCondition condition) {
		this.fieldValues.addSQLCondition(condition);
	}

	public void addCondition(ISQL condition) {
		this.fieldValues.addSQLCondition(condition);
	}

	public void addCondition(BooleanCondition condition) {
		this.fieldValues.addSQLCondition(condition);
	}

	public FieldValuesSQL getConditions() {
		return this.fieldValues;
	}

	public WhereSQL clone() {
		return ((WhereSQL) SerializeUtil.ByteToObject(SerializeUtil.ObjectToByte(this)));
	}

	public String getSQLString(ISQL.DBType type) {
		if ((this.fieldValues.size() == 0) || (StringUtil.isNullOrEmpty(this.fieldValues.getSQLString(type)))) {
			return "";
		}
		return " where " + this.fieldValues.getSQLString(type);
	}
}
