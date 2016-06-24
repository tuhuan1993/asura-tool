package com.asura.tools.string;

import com.asura.tools.util.StringUtil;

public class EnglishOrNumberCondition implements IStringCondition {
	private static final long serialVersionUID = 2781699180402514317L;

	public boolean meet(String string) {
		string = StringUtil.getStandardString(string);
		return StringUtil.isCharOrNumberString(string);
	}
}
