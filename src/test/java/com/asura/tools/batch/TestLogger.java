package com.asura.tools.batch;

import static org.junit.Assert.*;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestLogger {

	@Test
	public void test() {
		Logger log=LoggerFactory.getLogger(TestLogger.class);
		log.warn("this is from junit test");
	}

}
