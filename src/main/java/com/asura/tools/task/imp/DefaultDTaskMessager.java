package com.asura.tools.task.imp;

import com.asura.tools.task.DTask;
import com.asura.tools.task.DTaskMessager;

public class DefaultDTaskMessager implements DTaskMessager {

	@Override
	public void output(DTask t, String message) {
		System.out.println(t.getName()+":"+message);
	}

}
