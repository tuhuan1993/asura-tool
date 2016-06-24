package com.asura.tools.data;

public interface DataIterator<T> {
	public abstract boolean hasNext();

	public abstract T next();

	public abstract void close();

	public abstract void reset();
}
