package com.asura.tools.data.selection.expression;

import com.asura.tools.data.selection.ordervalue.CharOrderValue;
import com.asura.tools.data.selection.ordervalue.IOrderValue;

public class CharOrderExpressionParser implements IExpressionParser {
	public static final String CHAR = "char";

	public IOrderValue parse(String type) {
		type = type.trim().toLowerCase();

		if ("char".equals(type)) {
			return new CharOrderValue();
		}

		return null;
	}

	public boolean canParse(String value) {
		return "char".equals(value);
	}
}