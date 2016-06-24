package com.asura.tools.util;

public class Entry {
	private Object object;
	private int position;

	public Entry(Object object, int position) {
		this.object = object;
		this.position = position;
	}

	public Object getObject() {
		return this.object;
	}

	public void setObject(Object object) {
		this.object = object;
	}

	public int getPosition() {
		return this.position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public boolean eqauls(Object obj) {
		Entry entry = (Entry) obj;
		return ((entry.getObject().equals(this.object)) && (entry.position == this.position));
	}

	public int hashCode() {
		return this.position;
	}

	public String toString() {
		return "[" + this.object + ", " + this.position + "]";
	}
}
