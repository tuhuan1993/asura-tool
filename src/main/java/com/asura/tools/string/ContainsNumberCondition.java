package com.asura.tools.string;

import com.asura.tools.util.StringUtil;

public class ContainsNumberCondition implements IStringCondition {
	private static final long serialVersionUID = 8485787291034786939L;

	public boolean meet(String string) {
		string = StringUtil.getStandardString(string);
		return StringUtil.containsNumber(string);
	}
}
