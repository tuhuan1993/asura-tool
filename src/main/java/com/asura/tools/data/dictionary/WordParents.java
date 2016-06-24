package com.asura.tools.data.dictionary;

import java.io.Serializable;
import java.util.HashMap;

public class WordParents implements Serializable {
	private static final long serialVersionUID = -854447841490623334L;
	private HashMap<String, SingleWord> map;
	private SingleWord current;

	public WordParents(SingleWord current) {
		this.map = new HashMap();
		this.current = current;
	}

	public void addSingleWords(SingleWord sw) {
		if (sw.contains(this.current))
			this.map.put(sw.getWord(), sw);
	}

	public int count() {
		return this.map.size();
	}
}
