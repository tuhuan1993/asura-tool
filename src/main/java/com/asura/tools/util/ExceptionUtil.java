package com.asura.tools.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

public class ExceptionUtil {
	public static String getExceptionContent(Exception e) {
		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw));
		String content = sw.toString();
		try {
			sw.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		return content;
	}
}
