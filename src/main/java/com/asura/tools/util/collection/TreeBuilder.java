package com.asura.tools.util.collection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class TreeBuilder {
	private HashMap<String, List<String>> dic;
	private boolean attr;
	private String name;
	private HashMap<String, String> map;
	private String attrName;

	public TreeBuilder(String name) {
		this.name = name;
		this.dic = new HashMap();
		this.map = new HashMap();
	}

	public TreeBuilder() {
		this.dic = new HashMap();
		this.map = new HashMap();
	}

	public boolean isAttr() {
		return this.attr;
	}

	public void setAttr(boolean attr) {
		this.attr = attr;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAttrName() {
		return this.attrName;
	}

	public void setAttrName(String attrName) {
		this.attrName = attrName;
	}

	public String[] GetChildren(String name) {
		return ((String[]) getChild(name).toArray(new String[0]));
	}

	public String[] GetParents(String name) {
		if (this.dic.containsKey(name)) {
			return ((String[]) ((List) this.dic.get(name)).toArray(new String[0]));
		}

		return new String[0];
	}

	public String[] GetSblings(String name) {
		HashSet set = new HashSet();

		if (this.dic.containsKey(name)) {
			for (String p : (List<String>) this.dic.get(name)) {
				set.addAll(getChild(p));
			}
		}

		return ((String[]) set.toArray(new String[0]));
	}

	public void AddMap(String name, String type) {
		if (!(this.map.containsKey(name)))
			this.map.put(name, type);
	}

	public void AddNode(String parent, String node) {
		if (!(this.dic.containsKey(node))) {
			this.dic.put(node, new ArrayList());
		}

		if (!(((List) this.dic.get(node)).contains(parent)))
			((List) this.dic.get(node)).add(parent);
	}

	public String[] GetAllChild(String name) {
		List result = new ArrayList();
		findAllChildren(name, result);

		return ((String[]) result.toArray(new String[0]));
	}

	private void findAllChildren(String name, List<String> result) {
		String[] cs = (String[]) getChild(name).toArray(new String[0]);
		result.addAll(Arrays.asList(cs));
		for (String c : cs)
			findAllChildren(c, result);
	}

	private HashSet<String> getChild(String node) {
		HashSet list = new HashSet();
		for (String key : this.dic.keySet()) {
			if (((List) this.dic.get(key)).contains(node)) {
				list.add(key);
			}
		}

		return list;
	}
}
