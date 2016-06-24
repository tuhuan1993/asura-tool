package com.asura.tools.data.dictionary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class WordMap {
	private HashMap<String, WordSet> map;

	public WordMap() {
		this.map = new HashMap();
	}

	public void addWord(SingleWord word) {
		if (!(this.map.containsKey(word.getWord()))) {
			this.map.put(word.getWord(), new WordSet());
		}

		((WordSet) this.map.get(word.getWord())).addWord(word);
	}

	public void addWords(SingleWord[] words) {
		for (SingleWord word : words)
			addWord(word);
	}

	public boolean hasNext() {
		removeLess(1);
		return (this.map.size() > 0);
	}

	public WordSet[] next() {
		WordSet[] set = maxWord();

		set = adjust(set);

		remove(set);

		return set;
	}

	private void removeLess(int min) {
		List<String> list = new ArrayList();
		for (String key : this.map.keySet()) {
			if (((WordSet) this.map.get(key)).size() <= min) {
				list.add(key);
			}
		}

		for (String key : list)
			this.map.remove(key);
	}

	private WordSet[] adjust(WordSet[] maxWords) {
		List list = new ArrayList();
		for (WordSet max : maxWords) {
			boolean replaced = false;
			for (WordSet word : this.map.values()) {
				if ((!(word.getKey().contains(max.getKey()))) || (Double.valueOf(word.size()).doubleValue()
						/ Double.valueOf(max.size()).doubleValue() <= 0.8D))
					continue;
				if (!(list.contains(word))) {
					list.add(word);
				}
				replaced = true;
				break;
			}

			if ((replaced) || (list.contains(max)))
				continue;
			list.add(max);
		}

		return ((WordSet[]) list.toArray(new WordSet[0]));
	}

	private WordSet[] maxWord() {
		int count = 0;
		int max = 0;

		List list = new ArrayList();

		for (String key : this.map.keySet()) {
			WordSet set = (WordSet) this.map.get(key);
			if (set.size() > max) {
				max = set.size();
				list.clear();
				list.add(set);
			} else if (set.size() == max) {
				if (list.size() > 0)
					if (((WordSet) list.get(0)).getKey().length() == set.getKey().length()) {
						list.add(set);
					} else if (((WordSet) list.get(0)).getKey().length() < set.getKey().length()) {
						list.clear();
						list.add(set);
					} else {
						list.add(set);
					}

			}

			System.out.println("find max:" + (count++));
		}

		return ((WordSet[]) list.toArray(new WordSet[0]));
	}

	private void remove(WordSet[] maxWords) {
		int count = 0;
		for (Iterator iterator = map.values().iterator(); iterator.hasNext();) {
			WordSet set = (WordSet) iterator.next();
			System.out.println((new StringBuilder("remove contains:")).append(count++).toString());
			WordSet awordset1[];
			int l = (awordset1 = maxWords).length;
			for (int k = 0; k < l; k++) {
				WordSet max = awordset1[k];
				max.getKey().equals(set.getKey());
			}

		}

		WordSet awordset[];
		int j = (awordset = maxWords).length;
		for (int i = 0; i < j; i++) {
			WordSet max = awordset[i];
			map.remove(max.getKey());
		}

	}
}
