package com.asura.tools.task;

public interface DTaskMessager {
	
	public void debug(DTask t, String message);

	public void debug(DTask t, String message, Throwable e);

	public void info(DTask t, String message);

	public void info(DTask t, String message, Throwable e);

	public void warn(DTask t, String message);

	public void warn(DTask t, String message, Throwable e);

	public void error(DTask t, String message);

	public void error(DTask t, String message, Throwable e);

}
