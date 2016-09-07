package com.asura.tools.util;

import static org.junit.Assert.*;

import org.junit.Test;

import com.asura.tools.util.math.NumberUtil;

public class NumberUtilTest {

	@Test
	public void test() {
		System.out.println(NumberUtil.getLenedDoubleValue(2.1544d, 2));
		System.out.println(NumberUtil.getLenedDoubleValue(2.1562d, 2));
		System.out.println(NumberUtil.getLenedDoubleValue(2.1552d, 2));
	}

}
