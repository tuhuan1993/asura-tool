package com.asura.tools.util;

import junit.framework.TestCase;

public class WordSpliterTest extends TestCase {
	public void testSplit() {
		WordSpliter spliter = WordSpliter.getInstance("key.dic");
		System.out.println(spliter.dic.get("手机"));
		System.out.println(StringUtil.getStringFromStrings(spliter.split("童话_小马过河_乐知家园"), " "));
	}
}
