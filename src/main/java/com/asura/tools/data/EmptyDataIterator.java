package com.asura.tools.data;

public class EmptyDataIterator<T> implements DataIterator<T> {
	public void close() {
	}

	public boolean hasNext() {
		return false;
	}

	public T next() {
		return null;
	}

	public void reset() {
	}
}
