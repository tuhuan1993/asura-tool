package com.asura.tools.data.selection;

import java.io.Serializable;

public interface IOrderValue extends Serializable {
	public static final String NULL = "null";
	public static final String NOT_NULL = "notnull";

	public int getBlockIndex(String paramString);

	public boolean contains(String paramString);
}
