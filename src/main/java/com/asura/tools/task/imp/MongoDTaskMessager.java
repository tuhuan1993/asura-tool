package com.asura.tools.task.imp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.asura.tools.log.MongoLogFactory;
import com.asura.tools.task.DTask;
import com.asura.tools.task.DTaskMessager;

public class MongoDTaskMessager implements DTaskMessager {

	@Override
	public void debug(DTask t, String message) {
		MongoLogFactory.getLogger(t.getGroup() + "@@@" + t.getName()).debug(message);
		LoggerFactory.getLogger(t.getGroup() + "@@@" + t.getName()).debug(message);
	}

	@Override
	public void debug(DTask t, String message, Throwable e) {
		MongoLogFactory.getLogger(t.getGroup() + "@@@" + t.getName()).debug(message, e);
		LoggerFactory.getLogger(t.getGroup() + "@@@" + t.getName()).debug(message, e);
	}

	@Override
	public void info(DTask t, String message) {
		MongoLogFactory.getLogger(t.getGroup() + "@@@" + t.getName()).info(message);
		LoggerFactory.getLogger(t.getGroup() + "@@@" + t.getName()).info(message);
	}

	@Override
	public void info(DTask t, String message, Throwable e) {
		MongoLogFactory.getLogger(t.getGroup() + "@@@" + t.getName()).info(message, e);
		LoggerFactory.getLogger(t.getGroup() + "@@@" + t.getName()).info(message, e);
	}

	@Override
	public void warn(DTask t, String message) {
		MongoLogFactory.getLogger(t.getGroup() + "@@@" + t.getName()).warn(message);
		LoggerFactory.getLogger(t.getGroup() + "@@@" + t.getName()).warn(message);
	}

	@Override
	public void warn(DTask t, String message, Throwable e) {
		MongoLogFactory.getLogger(t.getGroup() + "@@@" + t.getName()).warn(message, e);
		LoggerFactory.getLogger(t.getGroup() + "@@@" + t.getName()).warn(message, e);
	}

	@Override
	public void error(DTask t, String message) {
		MongoLogFactory.getLogger(t.getGroup() + "@@@" + t.getName()).error(message);
		LoggerFactory.getLogger(t.getGroup() + "@@@" + t.getName()).error(message);
	}

	@Override
	public void error(DTask t, String message, Throwable e) {
		MongoLogFactory.getLogger(t.getGroup() + "@@@" + t.getName()).error(message, e);
		LoggerFactory.getLogger(t.getGroup() + "@@@" + t.getName()).error(message, e);
	}

}
