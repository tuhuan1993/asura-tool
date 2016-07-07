package com.asura.tools.classses;

import java.lang.annotation.Annotation;
import java.util.Set;

public class ClassScanner {
	public static Set<Class<?>> scan(String basePackage) throws Exception {
        return scan(basePackage, null, null);
    }

    public static Set<Class<?>> scan(String basePackage, Class<? extends Annotation>... annotation) throws Exception {
        return scan(basePackage, annotation, null);
    }

    public static Set<Class<?>> scan(String basePackage, String... className) throws Exception {
        return scan(basePackage, null, className);
    }

    public static Set<Class<?>> scan(String basePackage, Class<? extends Annotation>[] annotations, String[] classNames) throws Exception {
        return new ClassScanExecutor(basePackage, annotations, classNames).execute();
    }
}
