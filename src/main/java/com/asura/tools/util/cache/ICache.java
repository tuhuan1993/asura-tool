package com.asura.tools.util.cache;

public interface ICache<K,V> {
	public boolean isCached(K paramK);

	public void cache(K paramK, V paramV);

	public V get(K paramK);

	public void delete(K paramK);

	public int size();
}
