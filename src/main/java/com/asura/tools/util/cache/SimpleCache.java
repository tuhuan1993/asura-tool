package com.asura.tools.util.cache;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class SimpleCache<K, V> implements Serializable {
	private static final long serialVersionUID = -412584736164379691L;
	private LinkedHashMap<K, V> table;
	private HashMap<K, Long> startTime;
	private HashMap<K, Integer> cacheTime;
	private long capacity;

	public SimpleCache(int capacity) {
		this.table = new LinkedHashMap<K,V>();
		this.capacity = capacity;
		this.startTime = new HashMap<K,Long>();
		this.cacheTime = new HashMap<K,Integer>();
	}

	public synchronized boolean iscached(K key) {
		removeTimeOut(key);

		return this.table.containsKey(key);
	}

	public synchronized void remove(K key) {
		this.table.remove(key);
	}

	public synchronized void cache(K key, V value) {
		if (this.table.size() >= this.capacity) {
			this.table.remove(this.table.keySet().iterator().next());
		}

		this.table.put(key, value);
	}

	public synchronized void cache(K key, V value, int seconds) {
		if (this.table.size() >= this.capacity) {
			this.table.remove(this.table.keySet().iterator().next());
		}

		this.startTime.put(key, Long.valueOf(new Date().getTime()));
		this.cacheTime.put(key, Integer.valueOf(seconds));
		this.table.put(key, value);
	}

	public synchronized V get(K key) {
		removeTimeOut(key);
		V v = this.table.get(key);
		return v;
	}

	private void removeTimeOut(K key) {
		if (this.startTime.containsKey(key)) {
			long now = new Date().getTime();
			if (now - ((Long) this.startTime.get(key)).longValue() > ((Integer) this.cacheTime.get(key)).intValue()
					* 1000) {
				remove(key);
				this.startTime.remove(key);
				this.cacheTime.remove(key);
			}
		}
	}

	public void clear() {
		this.startTime.clear();
		this.cacheTime.clear();
		this.capacity = 0L;
		this.table.clear();
	}

	public synchronized int size() {
		return this.table.size();
	}
} 

