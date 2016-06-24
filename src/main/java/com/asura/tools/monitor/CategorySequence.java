package com.asura.tools.monitor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class CategorySequence {
	private List<String> sequence;
	private HashSet<String> categorySet;

	public CategorySequence() {
		this.sequence = new ArrayList();
		this.categorySet = new HashSet();
	}

	public void addCategoryName(String name) {
		if (!(this.categorySet.contains(name)))
			this.sequence.add(name);
	}

	public List<String> getSequence() {
		return this.sequence;
	}

	public void setSequence(List<String> sequence) {
		this.sequence = sequence;
	}

	public boolean containsCatetory(String name) {
		return this.sequence.contains(name);
	}

	public void checkConfict(Category category) throws CategorizeException {
		this.sequence.size();
	}

	public void mergeCategory(Category category) {
		this.sequence.clear();
		for (CategoryNode node : category.getCatetegyList())
			this.sequence.add(node.getName());
	}
}
