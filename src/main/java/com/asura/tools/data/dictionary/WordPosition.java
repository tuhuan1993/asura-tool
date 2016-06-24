package com.asura.tools.data.dictionary;

import java.io.Serializable;

public class WordPosition implements Serializable {
	private static final long serialVersionUID = -4213651261985278668L;
	private String word;
	private WordSource source;
	private int start;
	private int end;

	public String getWord() {
		return this.word;
	}

	public boolean confict(String word) {
		if (this.word.contains(word)) {
			return false;
		}
		for (int i = 1; i < word.length(); ++i) {
			String p = this.source.getString(this.start - i, this.end);
			if (p.contains(word)) {
				return true;
			}
		}

		for (int i = 1; i < word.length(); ++i) {
			String p = this.source.getString(this.start, this.end + i);
			if (p.contains(word)) {
				return true;
			}
		}

		return false;
	}

	public String getPreviousChar() {
		if (this.start > 0) {
			return this.source.getSource().substring(this.start - 1, this.start);
		}

		return "";
	}

	public String getNextChar() {
		if (this.end < this.source.getSource().length()) {
			return this.source.getSource().substring(this.end, this.end + 1);
		}

		return "";
	}

	public void setWord(String word) {
		this.word = word;
	}

	public WordPosition() {
	}

	public WordPosition(WordSource source, int start, int end) {
		this.source = source;
		this.start = start;
		this.end = end;
	}

	public WordPosition(String word, WordSource source, int start, int end) {
		this.word = word;
		this.source = source;
		this.start = start;
		this.end = end;
	}

	public WordSource getSource() {
		return this.source;
	}

	public void setSource(WordSource source) {
		this.source = source;
	}

	public int getStart() {
		return this.start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getEnd() {
		return this.end;
	}

	public void setEnd(int end) {
		this.end = end;
	}

	public int hashCode() {
		int prime = 31;
		int result = 1;
		result = 31 * result + ((this.source == null) ? 0 : this.source.hashCode());
		result = 31 * result + this.start;
		result = 31 * result + ((this.word == null) ? 0 : this.word.hashCode());
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (super.getClass() != obj.getClass())
			return false;
		WordPosition other = (WordPosition) obj;
		if (this.source == null)
			if (other.source != null)
				return false;
			else if (!(this.source.equals(other.source)))
				return false;
		if (this.start != other.start)
			return false;
		if (this.word == null)
			if (other.word != null)
				return false;
			else if (!(this.word.equals(other.word)))
				return false;
		return true;
	}
}