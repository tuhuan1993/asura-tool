package com.asura.tools.data.dictionary;

public class WordResult implements Comparable<WordResult> {
	private String word;
	private int count;

	public WordResult() {
	}

	public WordResult(String word, int count) {
		this.word = word;
		this.count = count;
	}

	public String getWord() {
		return this.word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public int getCount() {
		return this.count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String toString() {
		return this.word + ", " + this.count;
	}

	public int compareTo(WordResult o) {
		if (this.count < o.count)
			return 1;
		if (this.count == o.count) {
			return 0;
		}
		return -1;
	}
}
