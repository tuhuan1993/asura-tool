package com.asura.tools.util.cache;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

import net.spy.memcached.MemcachedClient;


public class MemcachedResultCache {
	private MemcachedClient c;
	private List<MemcachedHost> hosts;
	public int timeOut = 3600;

	public MemcachedResultCache() {
		this.hosts = new ArrayList<MemcachedHost>();
	}

	public void cache(String key, Object result) {
		MemcachedClient mc = getClient();
		if (mc != null) {
			mc.delete(key);
			mc.add(key, this.timeOut, result);
		}
	}

	public void close() {
		this.c.flush();
		this.c.shutdown();
	}

	public void cache(String key, Object result, int expireTime) {
		MemcachedClient mc = getClient();
		if (mc != null) {
			mc.delete(key);
			mc.add(key, expireTime, result);
		}
	}

	public void delete(String key) {
		MemcachedClient mc = getClient();
		if (mc != null)
			mc.delete(key);
	}

	public Object getCached(String key) {
		MemcachedClient mc = getClient();
		if (mc != null)
			try {
				return mc.get(key);
			} catch (Exception localException) {
			}
		return null;
	}

	public boolean isCached(String key) {
		return (getCached(key) != null);
	}

	public List<MemcachedHost> getHosts() {
		return this.hosts;
	}

	public void setHosts(List<MemcachedHost> hosts) {
		this.hosts = hosts;
	}

	public void addHost(MemcachedHost host) {
		this.hosts.add(host);
	}

	private void initial() {
		List<InetSocketAddress> list = new ArrayList<InetSocketAddress>();
		if (this.hosts != null) {
			for (MemcachedHost host : this.hosts)
				list.add(new InetSocketAddress(host.getHost(), host.getPort()));
			try {
				this.c = new MemcachedClient(list);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public MemcachedClient getClient() {
		if (this.c == null) {
			initial();
		}

		return this.c;
	}
}
