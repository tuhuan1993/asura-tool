package com.asura.tools.data.dictionary;

import java.io.Serializable;

public class WordSource implements Serializable {
	private static final long serialVersionUID = -3280967092593634640L;
	private String source;
	private int count;

	public WordSource() {
	}

	public WordSource(String source) {
		this.source = source;
	}

	public WordSource(String source, int count) {
		this.source = source;
		this.count = count;
	}

	public String getSource() {
		return this.source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public int getCount() {
		return this.count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getString(int start, int end) {
		if (start < 0) {
			start = 0;
		}
		if (end > this.source.length()) {
			end = this.source.length();
		}

		return this.source.substring(start, end);
	}

	public int hashCode() {
		int prime = 31;
		int result = 1;
		result = 31 * result + ((this.source == null) ? 0 : this.source.hashCode());
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (super.getClass() != obj.getClass())
			return false;
		WordSource other = (WordSource) obj;
		if (this.source == null)
			if (other.source != null)
				return false;
			else if (!(this.source.equals(other.source)))
				return false;
		return true;
	}
}