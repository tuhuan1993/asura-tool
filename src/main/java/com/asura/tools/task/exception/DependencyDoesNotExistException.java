package com.asura.tools.task.exception;

import com.asura.tools.task.DTask;

public class DependencyDoesNotExistException extends Exception {
	
	private static final long serialVersionUID = -6530875406569030159L;

	public DependencyDoesNotExistException(DTask task) {
		super("Task " + task.getName() + " has not yet been added to the Task Graph.");
	}
}
