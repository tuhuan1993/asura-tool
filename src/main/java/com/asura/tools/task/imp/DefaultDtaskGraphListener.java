package com.asura.tools.task.imp;

import com.asura.tools.task.DTask;
import com.asura.tools.task.DTask.STATE;
import com.asura.tools.task.DTaskGraph;
import com.asura.tools.task.DTaskGraphListener;

public class DefaultDtaskGraphListener implements DTaskGraphListener{

	@Override
	public void end(DTaskGraph taskGraph) {
		System.out.println("job ended");
	}

	@Override
	public void begin(DTaskGraph taskGraph) {
		System.out.println("job started");
	}

	@Override
	public void traceStatus(DTaskGraph taskGraph, DTask t, STATE state) {
		System.out.println(t.getGroup()+"@@@"+t.getName()+"\t"+state.toString());
	}
	

}
