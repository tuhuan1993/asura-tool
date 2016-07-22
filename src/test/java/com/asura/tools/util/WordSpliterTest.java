package com.asura.tools.util;

import org.junit.Test;
import com.asura.tools.util.StringUtil;
import com.asura.tools.util.WordSpliter;

public class WordSpliterTest {
	
	@Test
	public void testSplit() {
		WordSpliter spliter = WordSpliter.getInstance("key.dic");
		System.out.println(spliter.dic.get("手机"));
		System.out.println(StringUtil.getStringFromStrings(spliter.split("童话_小马过河_乐知家园"), " "));
	}
}
