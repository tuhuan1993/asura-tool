package com.asura.tools.util;

import static org.junit.Assert.*;

import java.util.concurrent.TimeUnit;

import org.junit.Test;

import com.asura.tools.util.cache.SimpleCache;
import com.google.common.util.concurrent.Uninterruptibles;

public class TestSimpleCache {

	@Test
	public void test() {
		SimpleCache<String, String> cache=new SimpleCache<>(2);
		cache.cache("a", "bbb");
		cache.cache("b", "ccc");
		System.out.println(cache.get("a"));
		System.out.println(cache.get("b"));
		System.out.println(cache.get("c"));
		cache.cache("c", "eee");
		System.out.println(cache.get("a"));
		System.out.println(cache.get("b"));
		System.out.println(cache.get("c"));
		cache.cache("e", "111", 5);
		while(true){
			System.out.println(cache.get("e"));
			Uninterruptibles.sleepUninterruptibly(500, TimeUnit.MILLISECONDS);
		}
	}

}
