package com.asura.tools.monitor;

import java.util.ArrayList;
import java.util.List;

public class CategoryTree {
	private CategoryNode root;
	private CategorySequence sequence;
	private List<Category> categories;

	public CategoryTree() {
		this.root = new CategoryNode("root", "root");
		this.sequence = new CategorySequence();
		this.categories = new ArrayList();
	}

	public void setName(String name) {
		this.root.setName(name);
	}

	public void addCategory(Category category) {
		this.categories.add(category);
		this.sequence.mergeCategory(category);
	}

	public void buildTree() {
		buildTree(this.sequence);
	}

	public CategoryNode getRootNode() {
		return this.root;
	}

	public void buildTree(CategorySequence sequence) {
		for (Category category : this.categories) {
			CategoryNode parent = this.root;
			for (String name : sequence.getSequence()) {
				if (category.containsCategory(name)) {
					parent = parent.addChildNode(category.getCategoryNode(name));
				} else {
					CategoryNode child = CategoryNode.newUncategorizedInstance();
					parent = parent.addChildNode(child);
				}

				if (category.getResult() != null)
					parent.addCategoryResult(category.getResult());
			}
		}
	}
}
