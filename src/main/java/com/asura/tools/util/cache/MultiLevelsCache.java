package com.asura.tools.util.cache;

import java.util.ArrayList;
import java.util.List;

public class MultiLevelsCache <K, V> implements ICache<K, V> {
	private List<ICache<K, V>> caches;

	public MultiLevelsCache() {
		this.caches = new ArrayList<ICache<K, V>>();
	}

	public void addCache(ICache<K, V> cache) {
		this.caches.add(cache);
	}

	public List<ICache<K, V>> getCaches() {
		return this.caches;
	}

	public void setCaches(List<ICache<K, V>> caches) {
		this.caches = caches;
	}

	public void cache(K key, V value) {
		for (ICache<K,V> cache : this.caches)
			cache.cache(key, value);
	}

	public V get(K key) {
		for (ICache<K,V> cache : this.caches) {
			if (cache.isCached(key)) {
				return cache.get(key);
			}
		}

		return null;
	}

	public boolean isCached(K key) {
		for (ICache<K,V> cache : this.caches) {
			if (cache.isCached(key)) {
				return true;
			}
		}

		return false;
	}

	public int size() {
		int sum = 0;
		for (ICache<K,V> cache : this.caches) {
			sum += cache.size();
		}
		return sum;
	}

	public void delete(K key) {
		for (ICache<K,V> cache : this.caches)
			if (cache.isCached(key)){
				cache.delete(key);
			}
	}

}
