package com.asura.tools.debug;

import java.io.IOException;

import org.apache.log4j.FileAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.SimpleLayout;

public class LogOutputer implements IDebugOutputer {
	private Logger logger;

	public LogOutputer(String file) {
		try {
			this.logger = Logger.getLogger(LogOutputer.class);
			this.logger.addAppender(new FileAppender(new SimpleLayout(), file, false));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void output(String key, String detail, String value) {
		this.logger.info(value);
	}
}
