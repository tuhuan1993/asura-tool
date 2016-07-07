package com.asura.tools.task.imp;

import com.asura.tools.task.DTask;
import com.asura.tools.task.DTaskExecutorTracer;

public class DefaultDtaskExecutorTracer implements DTaskExecutorTracer{

	@Override
	public void trace(DTask t, String infor) {
		System.out.println(t.getName()+":"+infor);
	}

}
