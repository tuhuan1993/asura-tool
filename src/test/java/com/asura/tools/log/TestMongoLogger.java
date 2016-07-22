package com.asura.tools.log;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Test;

import com.asura.tools.util.DateUtil;

public class TestMongoLogger {

	@Test
	public void test() {
		System.out.println(DateUtil.getDateString(new Date()));
		MLog logger=MongoLogFactory.getLogger("abctask@asdadsf");
		logger.info("first one");
		logger.error("exception occurred", new NullPointerException("hahahaha"));
		logger.info("second one");
	}

}
