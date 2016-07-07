package com.asura.tools.annotation.parser;

import java.util.Set;

public interface AnnotationParserFactory {
	
	public void init(String[] packages, Set<Class<?>> classes) throws Exception;

}
