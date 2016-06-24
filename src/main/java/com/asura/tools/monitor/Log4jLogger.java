package com.asura.tools.monitor;

import org.apache.log4j.Appender;
import org.apache.log4j.Logger;

public class Log4jLogger implements ILogger {
	private Logger logger;

	public Log4jLogger(String name) {
		this.logger = Logger.getLogger(name);
	}

	public void addFileAppender(Appender appender) {
		this.logger.addAppender(appender);
	}

	public void removeAppend(Appender appender) {
		this.logger.removeAppender(appender);
		appender.close();
	}

	public void debug(String message) {
		this.logger.debug(message);
	}

	public void error(String message) {
		this.logger.error(message);
	}

	public void info(String message) {
		this.logger.info(message);
	}

	public void warn(String message) {
		this.logger.warn(message);
	}

	public void close() {
	}
}
