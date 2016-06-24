package com.asura.tools.data.selection.expression;

import com.asura.tools.data.selection.IOrderValue;
import com.asura.tools.data.selection.SequenceOrderValue;

public class BooleanExpressionParser implements IExpressionParser {
	private static final String ONLY_TRUE = "t";
	private static final String ONLY_FLASE = "f";
	private static final String TRUE_FALSE = "tf";
	private static final String FALSE_TRUE = "ft";

	public IOrderValue parse(String type) {
		type = type.trim().toLowerCase();
		if ("t".equals(type))
			return new SequenceOrderValue(new String[] { "true" });
		if ("f".equals(type))
			return new SequenceOrderValue(new String[] { "false", "null" });
		if ("tf".equals(type))
			return new SequenceOrderValue(new String[] { "true", "false", "null" });
		if ("ft".equals(type)) {
			return new SequenceOrderValue(new String[] { "false", "null", "true" });
		}

		return null;
	}

	public boolean canParse(String value) {
		value = value.toLowerCase().trim();
		return (("tf".equals(value)) || ("ft".equals(value)) || ("f".equals(value)) || ("t".equals(value)));
	}
}
