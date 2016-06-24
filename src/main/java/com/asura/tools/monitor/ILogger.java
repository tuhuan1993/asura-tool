package com.asura.tools.monitor;

public interface ILogger {
	public void info(String paramString);

	public void error(String paramString);

	public void debug(String paramString);

	public void warn(String paramString);

	public void close();
}
