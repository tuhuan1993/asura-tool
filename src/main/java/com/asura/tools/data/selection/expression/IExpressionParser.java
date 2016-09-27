package com.asura.tools.data.selection.expression;

import com.asura.tools.data.selection.ordervalue.IOrderValue;

public interface IExpressionParser {
	public static final String UNDERLINE = "_";

	public IOrderValue parse(String paramString);

	public boolean canParse(String paramString);
}
