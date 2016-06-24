package com.asura.tools.condition;

public interface IExpParser<T extends IClausable> {
	public T parse(String paramString);
}
