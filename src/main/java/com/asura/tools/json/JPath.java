package com.asura.tools.json;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.jayway.jsonpath.Filter;
import com.jayway.jsonpath.JsonPath;

public class JPath {
	public static String[] getValues(String content, String path) {
		List objs = (List) JsonPath.read(content, path, new Filter[0]);

		List list = new ArrayList();
		for (Iterator localIterator = objs.iterator(); localIterator.hasNext();) {
			Object obj = localIterator.next();
			list.add(obj.toString());
		}

		return ((String[]) list.toArray(new String[0]));
	}

	public static String getValue(String content, String path) {
		Object obj = JsonPath.read(content, path, new Filter[0]);

		return obj.toString();
	}
}
