package com.asura.tools.data.selection.expression;

import com.asura.tools.data.selection.IOrderValue;
import com.asura.tools.data.selection.RangeNumberOrderValue;
import com.asura.tools.util.math.NumberUtil;

public class NumberRangeExpressionParser implements IExpressionParser {
	public static final String TYPE = "nr";

	public IOrderValue parse(String type) {
		String[] ss = type.split("_");

		RangeNumberOrderValue orderValue = new RangeNumberOrderValue();
		orderValue.setStart(NumberUtil.getInt(ss[1]));
		orderValue.setEnd(NumberUtil.getInt(ss[2]));
		orderValue.setContainsStart(true);
		orderValue.setContainsEnd(true);

		return orderValue;
	}

	public boolean canParse(String value) {
		String[] ss = value.split("_");
		if (ss.length == 3) {
			return "nr".equals(ss[0].trim().toLowerCase());
		}

		return false;
	}
}