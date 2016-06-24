package com.asura.tools.data.mongo;

import java.util.Iterator;

import com.mongodb.DBObject;

public class EmptyDbIterator implements Iterator<DBObject> {
	public boolean hasNext() {
		return false;
	}

	public DBObject next() {
		return null;
	}

	public void remove() {
	}
}