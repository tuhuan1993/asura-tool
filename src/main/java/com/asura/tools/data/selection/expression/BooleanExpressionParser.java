package com.asura.tools.data.selection.expression;

import com.asura.tools.data.selection.ordervalue.IOrderValue;
import com.asura.tools.data.selection.ordervalue.SequenceOrderValue;

public class BooleanExpressionParser implements IExpressionParser {
	private static final String ONLY_TRUE = "t";
	private static final String ONLY_FLASE = "f";
	private static final String TRUE_FALSE = "tf";
	private static final String FALSE_TRUE = "ft";

	public IOrderValue parse(String type) {
		type = type.trim().toLowerCase();
		if (ONLY_TRUE.equals(type))
			return new SequenceOrderValue(new String[] { "true" });
		if (ONLY_FLASE.equals(type))
			return new SequenceOrderValue(new String[] { "false", "null" });
		if (TRUE_FALSE.equals(type))
			return new SequenceOrderValue(new String[] { "true", "false", "null" });
		if (FALSE_TRUE.equals(type)) {
			return new SequenceOrderValue(new String[] { "false", "null", "true" });
		}

		return null;
	}

	public boolean canParse(String value) {
		value = value.toLowerCase().trim();
		return ((TRUE_FALSE.equals(value)) || (FALSE_TRUE.equals(value)) || (ONLY_FLASE.equals(value))
				|| (ONLY_TRUE.equals(value)));
	}
}
