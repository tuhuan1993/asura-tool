package com.asura.tools.util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class SortUtil {
	public static LinkedHashMap<Object, Object> getTopsByValue(Map<?, ?> dic, int top) {
		Object[] objs = dic.values().toArray();
		Arrays.sort(objs);
		HashSet<Object> addedSet = new HashSet<Object>();
		LinkedHashMap<Object,Object> map = new LinkedHashMap<Object,Object>();
		for (int i = 0; i < Math.min(objs.length, top); ++i) {
			for (Iterator<?> localIterator = dic.keySet().iterator(); localIterator.hasNext();) {
				Object key = localIterator.next();
				if ((!(dic.get(key).equals(objs[(objs.length - i - 1)]))) || (addedSet.contains(key)))
					continue;
				map.put(key, dic.get(key));
				addedSet.add(key);
			}

		}

		return map;
	}
}
