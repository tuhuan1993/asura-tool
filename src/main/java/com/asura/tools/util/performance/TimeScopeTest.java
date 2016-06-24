package com.asura.tools.util.performance;

import com.asura.tools.util.collection.Accumulator;

import junit.framework.TestCase;

public class TimeScopeTest extends TestCase {
	public void testTimeScope() {
		Accumulator acc = new Accumulator();
		acc.addKey(new TimeScope(0L, 10L));
		acc.addKey(new TimeScope(0L, 10L));
		System.out.println(acc.toString());

		Accumulator acc1 = new Accumulator();
		acc1.addKey("10");
		acc1.addKey("10");
		System.out.println(acc1.toString());
	}
}