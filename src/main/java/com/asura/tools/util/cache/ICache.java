package com.asura.tools.util.cache;

public interface ICache<K, V> {
	public boolean isCached(K key);

	public void cache(K key, V value);

	public V get(K key);

	public void delete(K key);

	public int size();
}
