package com.asura.tools.task;

public interface DTaskGraphListener {

	public void end(DTaskGraph taskGraph);

	public void begin(DTaskGraph taskGraph);

	public void traceStatus(DTaskGraph taskGraph, DTask t, DTask.STATE state);

}
