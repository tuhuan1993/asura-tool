package com.asura.tools.monitor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CategoryNode implements Serializable {
	private static final long serialVersionUID = 2376394410002566634L;
	private String name;
	private String value;
	private List<CategoryNode> children;
	private List<ICategoryResult> results;

	public CategoryNode() {
		this.children = new ArrayList();
		this.results = new ArrayList();
	}

	public CategoryNode(String name, String value) {
		this.name = name;
		this.value = value;
		this.children = new ArrayList();
		this.results = new ArrayList();
	}

	public void addCategoryResult(ICategoryResult result) {
		if (this.results == null) {
			this.results = new ArrayList();
		}
		this.results.add(result);
	}

	public List<ICategoryResult> getResults() {
		return this.results;
	}

	public static CategoryNode newUncategorizedInstance() {
		return new CategoryNode("未分类", "未分类");
	}

	public CategoryNode addChildNode(CategoryNode node) {
		if (this.children == null) {
			this.children = new ArrayList();
		}
		for (CategoryNode child : this.children) {
			if (child.getValue().equals(node.value)) {
				return child;
			}
		}

		this.children.add(node);
		return node;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public List<CategoryNode> getChildren() {
		return this.children;
	}

	public void setChildren(List<CategoryNode> children) {
		this.children = children;
	}

	public String toString() {
		return this.name + ":" + this.value;
	}

}
