package com.asura.tools.task;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

import com.asura.tools.task.exception.DependencyDoesNotExistException;
import com.google.common.util.concurrent.Uninterruptibles;

public class DTaskTest {
	
	List<String> result = Collections.synchronizedList(new ArrayList<String>());

	@Test
	public void testDTaskGraph() {
		DTaskGraph dg = new DTaskGraph();

		assertFalse(dg.hasTasks());

		MyTask t0 = new MyTask("t0", 5000);
		MyTask t1 = new MyTask("t1", 5000);
		MyTask t2 = new MyTask("t2", 5000);
		MyTask t3 = new MyTask("t3", 5000);

		dg.insert(t3);
		assertTrue(dg.hasTasks());

		dg.insert(t2, t3);
		dg.insert(t1, t2);
		dg.insert(t0, t1);
		
		assertNull(dg.getErrors());
		assertTrue(dg.hasNextRunnalbeDTask());
		assertEquals(t3, dg.nextRunnableDTask());
		
		t3.setDone(true);
		t3.setSuccess(true);
		
		dg.notifyDone(t3);
		
		assertTrue(dg.hasNextRunnalbeDTask());
		assertEquals(t2, dg.nextRunnableDTask());
		
		dg.notifyDone(t2);
		
		assertFalse(dg.hasNextRunnalbeDTask());
		assertEquals(null, dg.nextRunnableDTask());
		
		assertEquals(1, dg.getFailedTasks().size());
		assertEquals(2, dg.getZonbieTasks().size());

	}
	
	@Test
	public void testMultiThreadedExecutor() throws InterruptedException, DependencyDoesNotExistException{
		MultiThreadedDTaskExecutor executor=new MultiThreadedDTaskExecutor();
		DTaskGraph dg = new DTaskGraph();
		MyTask t0 = new MyTask("t0", 5000);
		MyTask t1 = new MyTask("t1", 5000);
		MyTask t2 = new MyTask("t2", 5000);
		MyTask t3 = new MyTask("t3", 5000);
		
		dg.insert(t3);
		dg.insert(t2, t3);
		dg.insert(t1, t2);
		dg.insert(t0, t1);
		dg.insert(t1, new AbstractDTask("exception task") {
			
			@Override
			public void run() {
				if(3/0==0){
					return;
				}
			}
		});
		
		executor.submit(dg);
		executor.waitDTaskGraphCompleted(dg);
	}

	public class MyTask extends AbstractDTask {

		private long sleepMillis;

		public MyTask(String name, long sleepMillis) {
			super(name);
			this.sleepMillis = sleepMillis;
		}

		@Override
		public void run() {
			Uninterruptibles.sleepUninterruptibly(sleepMillis, TimeUnit.MILLISECONDS);
			isDone = true;
			isSuccess = true;
			isError = false;
			result.add(getName());
		}
		
		public void setDone(boolean isDone){
			this.isDone=isDone;
		}
		
		public void setSuccess(boolean success){
			isSuccess=success;
		}
		
		public void setError(boolean error){
			isError=error;
		}

	}
}
