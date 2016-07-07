package com.asura.tools.task;

public interface DTask extends Runnable{
		
	public boolean isDone();
	
	public String getName();
	
	public void setDtaskMessager(DTaskMessager messager);
	
	public String getSuccessMessage();
	
	public String getErrorMessage();
	
	public String getDescripter();
	
	public boolean isSuccess();
	
	public boolean isError();
	
}
