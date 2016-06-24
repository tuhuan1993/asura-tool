package com.asura.tools.data.dictionary;

import java.util.HashSet;

public class WordSet {
	private HashSet<SingleWord> set;
	private String key;

	public WordSet(WordSet wordSet) {
		this.set = wordSet.set;
		this.key = wordSet.key;
	}

	public WordSet() {
		this.set = new HashSet();
	}

	public void addWord(SingleWord word) {
		this.set.add(word);
		this.key = word.getWord();
	}

	public int size() {
		return this.set.size();
	}

	public String getKey() {
		return this.key;
	}

	public int hashCode() {
		int prime = 31;
		int result = 1;
		result = 31 * result + ((this.key == null) ? 0 : this.key.hashCode());
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (super.getClass() != obj.getClass())
			return false;
		WordSet other = (WordSet) obj;
		if (this.key == null)
			if (other.key != null)
				return false;
			else if (!(this.key.equals(other.key)))
				return false;
		return true;
	}

	public String toString() {
		return this.key + ", " + this.set.size();
	}
}
