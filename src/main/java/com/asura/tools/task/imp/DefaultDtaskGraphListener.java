package com.asura.tools.task.imp;

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

}
