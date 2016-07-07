package com.asura.tools.task;

import com.asura.tools.task.imp.DefaultDTaskMessager;

public abstract class AbstractDTask implements DTask{
	
	protected String name;
	protected String descriptor;
	
	protected boolean isDone;
	protected boolean isSuccess;
	protected boolean isError;
	
	protected String successMessage;
	protected String errorMessage;
	
	protected DTaskMessager messager;
	
	public AbstractDTask(String name, String descriptor) {
		this.name=name;
		this.descriptor=descriptor;
	}
	
	public AbstractDTask(String name) {
		this.name=name;
	}

	@Override
	public boolean isDone() {
		return isDone;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setDtaskMessager(DTaskMessager messager) {
		if(messager!=null){
			this.messager=messager;
		}else{
			this.messager=new DefaultDTaskMessager();
		}
	}

	@Override
	public String getSuccessMessage() {
		return successMessage;
	}

	@Override
	public String getErrorMessage() {
		return errorMessage;
	}

	@Override
	public String getDescripter() {
		return descriptor;
	}

	@Override
	public boolean isSuccess() {
		return isSuccess;
	}

	@Override
	public boolean isError() {
		return isError;
	}

}
