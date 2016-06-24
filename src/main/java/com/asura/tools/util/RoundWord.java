package com.asura.tools.util;

import java.util.ArrayList;
import java.util.List;

public class RoundWord {
	private String word;
	private String left;
	private String right;
	private boolean used;

	public RoundWord(String word) {
		this.word = word;
	}

	public String getWord() {
		return this.word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public String getLeft() {
		return this.left;
	}

	public void setLeft(String left) {
		this.left = left;
	}

	public String getRight() {
		return this.right;
	}

	public void setRight(String right) {
		this.right = right;
	}

	public boolean isUsed() {
		return this.used;
	}

	public void setUsed(boolean used) {
		this.used = used;
	}

	public List<String> getWords() {
		List list = new ArrayList();
		if (!(StringUtil.isNullOrEmpty(this.left))) {
			list.add(this.left);
		}
		list.add(this.word);
		if (!(StringUtil.isNullOrEmpty(this.right))) {
			list.add(this.right);
		}
		return list;
	}
}
