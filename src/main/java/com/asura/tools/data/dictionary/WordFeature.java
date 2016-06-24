package com.asura.tools.data.dictionary;

import com.asura.tools.data.selection.IFeaturable;
import com.asura.tools.data.selection.SelectMethod;

public class WordFeature implements IFeaturable {
	private SingleWord word;
	private WordGroup group;

	public WordFeature(SingleWord word) {
		this.word = word;
	}

	public WordFeature(SingleWord word, WordGroup group) {
		this.word = word;
		this.group = group;
	}

	public SingleWord getWord() {
		return this.word;
	}

	public void setWord(SingleWord word) {
		this.word = word;
	}

	public WordGroup getGroup() {
		return this.group;
	}

	public void setGroup(WordGroup group) {
		this.group = group;
	}

	public static boolean isMeet(String exp, SingleWord word, WordGroup group) {
		SelectMethod sm = new SelectMethod(exp);
		WordFeature wf = new WordFeature(word, group);

		return sm.meet(wf);
	}

	public String getFeatureValue(String feature) {
		if ("h-max-percent".equals(feature))
			return String.valueOf(this.word.getHeaders().getMaxPercent());
		if ("h-count".equals(feature))
			return String.valueOf(this.word.getHeaders().count());
		if ("t-max-percent".equals(feature))
			return String.valueOf(this.word.getTails().getMaxPercent());
		if ("t-count".equals(feature))
			return String.valueOf(this.word.getTails().count());
		if ("count".equals(feature))
			return String.valueOf(this.word.getFrequency());
		if ("acount".equals(feature))
			return String.valueOf(this.word.getAllCount() / 10);
		if ("length".equals(feature))
			return String.valueOf(this.word.getWord().length());
		if ("start".equals(feature)) {
			return String.valueOf(this.word.getAverageStart());
		}

		return "";
	}

	public Object getObject() {
		return this.word;
	}

	public boolean hasFeature(String feature) {
		return true;
	}
}
