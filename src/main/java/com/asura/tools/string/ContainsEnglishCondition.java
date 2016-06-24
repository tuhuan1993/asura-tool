package com.asura.tools.string;

import com.asura.tools.util.StringUtil;

public class ContainsEnglishCondition implements IStringCondition {
	private static final long serialVersionUID = -6168391383958728838L;

	public boolean meet(String string) {
		string = StringUtil.getStandardString(string);
		return StringUtil.containsEnglish(string);
	}
}
