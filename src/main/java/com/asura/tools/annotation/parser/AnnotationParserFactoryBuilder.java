package com.asura.tools.annotation.parser;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

import com.asura.tools.annotation.handler.rchandler.RCHandler;
import com.asura.tools.classses.ClassScanner;

public class AnnotationParserFactoryBuilder {

	public static class Builder<F extends AnnotationParserFactory> {

		private String[] packages;

		public Builder(String... packages) {
			this.packages = packages;
		}

		public F build(Class<? extends Annotation> atype, F factory) throws Exception {
			Set<Class<?>> results = new HashSet<>();
			for (String pkg : packages) {
				Set<Class<?>> rs = ClassScanner.scan(pkg, atype);
				if (rs != null && !rs.isEmpty()) {
					results.addAll(rs);
				}
			}

			factory.init(packages, results);

			return factory;
		}

	}
}
