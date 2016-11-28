package com.asura.tools.data.redis;

import org.junit.Test;

public class TestRedisConnection2 {

	@Test
	public void test() {
		RedisHandler handler = new RedisHandler();
		while (true) {
			System.out.println(handler.hget("haha", "name"));
		}
	}
}
