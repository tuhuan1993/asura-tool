package com.asura.tools.data.dictionary;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import com.asura.tools.util.SerializeUtil;

public class SingleWord implements Comparable<SingleWord>, Serializable {
	private static final long serialVersionUID = 9094083322700468090L;
	private String word;
	private HashSet<WordPosition> positions;
	private WordParents parents;
	private WordDistribution headers;
	private WordDistribution tails;
	private boolean modified;

	public SingleWord(String word) {
		this.word = word;
		this.positions = new HashSet();
		this.parents = new WordParents(this);
		this.tails = new WordDistribution();
		this.headers = new WordDistribution();
	}

	public boolean isModified() {
		return this.modified;
	}

	public void setModified(boolean modified) {
		this.modified = modified;
	}

	public void addParent(SingleWord sw) {
		this.parents.addSingleWords(sw);
	}

	public void addPosition(WordPosition wp) {
		if (!(this.positions.contains(wp))) {
			this.positions.add(wp);
			this.headers.add(wp.getPreviousChar());
			this.tails.add(wp.getNextChar());
		}
	}

	public int removeConflict(SingleWord sw) {
		if (this != sw && !equals(sw)) {
			List removes = new ArrayList();
			for (Iterator iterator = positions.iterator(); iterator.hasNext();) {
				WordPosition wp = (WordPosition) iterator.next();
				if (wp.confict(sw.word))
					removes.add(wp);
			}

			WordPosition remove;
			for (Iterator iterator1 = removes.iterator(); iterator1.hasNext(); tails.delete(remove.getNextChar())) {
				remove = (WordPosition) iterator1.next();
				positions.remove(remove);
				headers.delete(remove.getPreviousChar());
			}

			return removes.size();
		} else {
			return 0;
		}

	}

	public WordDistribution getHeaders() {
		return this.headers;
	}

	public WordDistribution getTails() {
		return this.tails;
	}

	public int getFrequency() {
		return this.positions.size();
	}

	public int getPrioritiedFrequency() {
		int i = 0;
		for (WordPosition wp : this.positions) {
			i += wp.getSource().getCount();
		}
		return i;
	}

	public int getAverageStart() {
		int i = 0;
		for (WordPosition wp : this.positions) {
			i += wp.getStart();
		}
		return (i / this.positions.size());
	}

	public HashSet<WordPosition> getPositions() {
		return this.positions;
	}

	public int getAllCount() {
		int i = 0;
		for (WordPosition wp : this.positions) {
			i += wp.getSource().getCount();
		}

		return i;
	}

	public WordParents getParents() {
		return this.parents;
	}

	public String getWord() {
		return this.word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public int length() {
		return this.word.length();
	}

	public boolean contains(SingleWord word) {
		return this.word.contains(word.getWord());
	}

	public SingleWord clone() {
		return ((SingleWord) SerializeUtil.ByteToObject(SerializeUtil.ObjectToByte(this)));
	}

	public int hashCode() {
		int prime = 31;
		int result = 1;
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
		SingleWord other = (SingleWord) obj;
		if (this.word == null)
			if (other.word != null)
				return false;
			else if (!(this.word.equals(other.word)))
				return false;
		return true;
	}

	public String toString() {
		return this.word + ":" + getPrioritiedFrequency() + ":" + getFrequency();
	}

	public int compareTo(SingleWord sw) {
		if (this.positions.size() > sw.positions.size())
			return -1;
		if (this.positions.size() == sw.positions.size()) {
			if (this.word.length() < sw.word.length())
				return -1;
			if (this.word.length() == sw.word.length()) {
				return 0;
			}
			return 1;
		}

		return 1;
	}
}
