package com.asura.tools.cjf;

import com.asura.tools.cjf.imp.ChineseJFImpl;

public class CJFBeanFactory {
	private static ChineseJF chineseJF = new ChineseJFImpl();

	public static ChineseJF getChineseJF() {
		return chineseJF;
	}
}
