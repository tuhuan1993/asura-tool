package com.asura.tools.util.collection;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class Accumulator<T> implements Serializable {
	private static final long serialVersionUID = 9056230844665828614L;
	private Map<T, Integer> map;
	private int allCount;

	public Accumulator() {
		this.map = new ConcurrentHashMap<T, Integer>();
	}

	public void addKey(T t) {
		if (!(this.map.containsKey(t))) {
			this.map.put(t, Integer.valueOf(0));
		}

		this.map.put(t, Integer.valueOf(this.map.get(t).intValue() + 1));
		this.allCount += 1;
	}

	public void addKey(T t, int count) {
		if (!(this.map.containsKey(t))) {
			this.map.put(t, Integer.valueOf(0));
		}

		this.map.put(t, Integer.valueOf(this.map.get(t).intValue() + count));
		this.allCount += count;
	}

	public void minusKey(T t, int count) {
		if (this.map.containsKey(t)) {
			this.allCount -= Math.min(count, this.map.get(t).intValue());
			this.map.put(t, Integer.valueOf(this.map.get(t).intValue() - count));
			if (this.map.get(t).intValue() <= 0)
				this.map.remove(t);
		}
	}

	public void minusKey(T t) {
		if (this.map.containsKey(t)) {
			this.allCount -= Math.min(1, this.map.get(t).intValue());
			this.map.put(t, Integer.valueOf(this.map.get(t).intValue() - 1));
			if (this.map.get(t).intValue() <= 0)
				this.map.remove(t);
		}
	}

	public boolean containsKey(T t) {
		return this.map.containsKey(t);
	}

	public Set<T> getKeys() {
		return this.map.keySet();
	}

	public void clear() {
		this.map.clear();
		this.allCount = 0;
	}

	public void clear(T t) {
		this.allCount -= this.map.get(t).intValue();
		this.map.put(t, Integer.valueOf(0));
	}

	public void delete(T t) {
		this.allCount -= this.map.get(t).intValue();
		this.map.remove(t);
	}

	public int getCount(T t) {
		if (this.map.containsKey(t)) {
			return this.map.get(t).intValue();
		}
		return 0;
	}

	public int getAllCount() {
		return this.allCount;
	}

	public int keyCount() {
		return this.map.size();
	}

	public List<T> keysSortedByValue() {
		ArrayList<KV> list = new ArrayList<KV>();
		for (T k : this.map.keySet()) {
			list.add(new KV(k, this.map.get(k).intValue()));
		}

		Collections.sort(list);

		ArrayList<T> result = new ArrayList<T>();
		for (KV kv : list) {
			result.add(kv.getK());
		}

		return result;
	}

	public List<T> keysSortedByValue(int count) {
		ArrayList<KV> list = new ArrayList<KV>();
		for (T k : this.map.keySet()) {
			list.add(new KV(k, this.map.get(k).intValue()));
		}

		Collections.sort(list);

		ArrayList<T> result = new ArrayList<T>();
		for (int i = 0; i < Math.min(count, list.size()); i++) {
			result.add(list.get(i).getK());
		}

		return result;
	}

	@SuppressWarnings("unchecked")
	public List<T> keysSortedByKey() {
		ArrayList<T> list = new ArrayList<>();
		for (T key : this.map.keySet()) {
			list.add(key);
		}
		Object[] os = list.toArray();
		Arrays.sort(os);
		for (Object o : os) {
			list.add((T) o);
		}
		return list;
	}

	public Accumulator<T> clone() {
		Accumulator<T> clone = new Accumulator<T>();
		clone.map = new ConcurrentHashMap<T, Integer>();
		for (T key : this.map.keySet()) {
			clone.map.put(key, this.map.get(key));
		}

		clone.allCount = this.allCount;

		return clone;
	}

	public Accumulator<Integer> getStatistics() {
		Accumulator<Integer> acc = new Accumulator<Integer>();
		for (Integer v : this.map.values()) {
			acc.addKey(v);
		}

		return acc;
	}

	public String toString() {
		return this.map.toString();
	}

	class KV implements Comparable<Accumulator<T>.KV> {
		private T k;
		private int v;

		public KV(T k, int v) {
			this.k = k;
			this.v = v;
		}

		public T getK() {
			return this.k;
		}

		public void setK(T k) {
			this.k = k;
		}

		public int getV() {
			return this.v;
		}

		public void setV(int v) {
			this.v = v;
		}

		public int compareTo(Accumulator<T>.KV kv) {
			if (this.v < kv.v)
				return 1;
			if (this.v == kv.v) {
				return 0;
			}
			return -1;
		}
	}
}
