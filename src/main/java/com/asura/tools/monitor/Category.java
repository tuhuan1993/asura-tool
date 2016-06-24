package com.asura.tools.monitor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Category implements Serializable {
	private static final long serialVersionUID = 8231289865632498830L;
	private ICategoryResult result;
	private List<CategoryNode> catetegyList;

	public Category() {
		this.catetegyList = new ArrayList();
	}

	public void addCategeryNode(CategoryNode node) {
		this.catetegyList.add(node);
	}

	public List<CategoryNode> getCatetegyList() {
		return this.catetegyList;
	}

	public CategoryNode getCategoryNode(String name) {
		for (CategoryNode node : this.catetegyList) {
			if (node.getName().equals(name)) {
				return node;
			}
		}

		return null;
	}

	public boolean containsCategory(String name) {
		for (CategoryNode node : this.catetegyList) {
			if (node.getName().equals(name)) {
				return true;
			}
		}

		return false;
	}

	public void setCatetegyList(List<CategoryNode> catetegyList) {
		this.catetegyList = catetegyList;
	}

	public ICategoryResult getResult() {
		return this.result;
	}

	public void setResult(ICategoryResult result) {
		this.result = result;
	}
}