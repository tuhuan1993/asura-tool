package com.asura.tools.data.dictionary;

import com.asura.tools.data.selection.data.IFeaturable;

public class WordGroupFeature implements IFeaturable {
	private WordGroup group;

	public WordGroupFeature(WordGroup group) {
		this.group = group;
	}

	public WordGroup getGroup() {
		return this.group;
	}

	public String getFeatureValue(String feature) {
		if ("count".equals(feature))
			return String.valueOf(this.group.count() - this.group.emptyCount());
		if ("empty".equals(feature)) {
			String empty = String.valueOf(this.group.emptyCount());
			return empty;
		}

		return "";
	}

	public Object getObject() {
		return this.group;
	}

	public boolean hasFeature(String feature) {
		return true;
	}
}
