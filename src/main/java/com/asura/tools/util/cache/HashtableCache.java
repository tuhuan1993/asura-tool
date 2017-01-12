package com.asura.tools.util.cache;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class HashtableCache<K, V> implements ICache<K, V> {
	private Hashtable<K, V> table;
	private long capacity;
	private List<K> keyList;

	public HashtableCache(int capacity) {
		this.table = new Hashtable<>();
		this.keyList = new ArrayList<>();
		this.capacity = capacity;
	}

	public boolean isCached(K key) {
		return this.table.containsKey(key);
	}

	public void delete(K key) {
		this.table.remove(key);
	}

	public void cache(K key, V value) {
		if (this.table.size() >= this.capacity) {
			this.table.remove(this.keyList.get(0));
			this.keyList.remove(0);
		}
		this.table.put(key, value);
		this.keyList.add(key);
	}

	public V get(K key) {
		return this.table.get(key);
	}

	public int size() {
		return this.table.size();
	}
}