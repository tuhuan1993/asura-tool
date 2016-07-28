package com.asura.tools.task.imp;

import org.slf4j.LoggerFactory;

import com.asura.tools.log.MongoLogFactory;
import com.asura.tools.task.DTask;
import com.asura.tools.task.DTask.STATE;
import com.asura.tools.task.DTaskGraph;
import com.asura.tools.task.DTaskGraphListener;

public class MongoDtaskGraphListener implements DTaskGraphListener {

	@Override
	public void end(DTaskGraph taskGraph) {
		MongoLogFactory.getLogger(taskGraph.getName()).info("task graph job finished. \n" + taskGraph);
		LoggerFactory.getLogger(taskGraph.getName()).info("task graph job finished. \n" + taskGraph);
	}

	@Override
	public void begin(DTaskGraph taskGraph) {
		MongoLogFactory.getLogger(taskGraph.getName()).info("task graph job started. \n" + taskGraph);
		LoggerFactory.getLogger(taskGraph.getName()).info("task graph job started. \n" + taskGraph);

	}

	@Override
	public void traceStatus(DTaskGraph taskGraph, DTask t, STATE state) {
		MongoLogFactory.getLogger(taskGraph.getName()).info("task " + t.getName() + " with state " + state.toString());
		LoggerFactory.getLogger(taskGraph.getName()).info("task " + t.getName() + " with state " + state.toString());
	}

}
