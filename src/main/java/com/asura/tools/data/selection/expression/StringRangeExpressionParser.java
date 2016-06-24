package com.asura.tools.data.selection.expression;

import java.util.ArrayList;
import java.util.List;

import com.asura.tools.data.selection.IOrderValue;
import com.asura.tools.data.selection.RangeStringOrderValue;

public class StringRangeExpressionParser implements IExpressionParser {
	public static final String TYPE = "sr";

	public IOrderValue parse(String type) {
		String[] ss = type.split("_");
		List values = new ArrayList();
		for (int i = 1; i < ss.length; ++i) {
			values.add(ss[i]);
		}

		return new RangeStringOrderValue((String[]) values.toArray(new String[0]));
	}

	public boolean canParse(String value) {
		String[] ss = value.split("_");
		if (ss.length > 1) {
			return "sr".equals(ss[0].trim().toLowerCase());
		}

		return false;
	}
}