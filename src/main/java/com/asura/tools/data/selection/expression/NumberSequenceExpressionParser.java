package com.asura.tools.data.selection.expression;

import com.asura.tools.data.selection.IOrderValue;
import com.asura.tools.data.selection.NumberSequenceOrderValue;
import com.asura.tools.util.math.NumberUtil;

public class NumberSequenceExpressionParser implements IExpressionParser {
	public static final String TYPE = "ns";

	public IOrderValue parse(String type) {
		String[] ss = type.split("_");

		NumberSequenceOrderValue orderValue = new NumberSequenceOrderValue();

		int i1 = NumberUtil.getInt(ss[1]);
		int i2 = NumberUtil.getInt(ss[2]);
		if (i1 > i2) {
			orderValue.setMin(i2);
			orderValue.setMax(i1);
			orderValue.setDesc(true);
		} else {
			orderValue.setMin(i1);
			orderValue.setMax(i2);
			orderValue.setDesc(false);
		}

		return orderValue;
	}

	public boolean canParse(String value) {
		String[] ss = value.split("_");
		if (ss.length == 3) {
			return "ns".equals(ss[0].trim().toLowerCase());
		}

		return false;
	}
}
