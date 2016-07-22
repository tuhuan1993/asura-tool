package com.asura.tools.task;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.asura.tools.task.DTask.STATE;
import com.asura.tools.task.exception.DependencyDoesNotExistException;
import com.asura.tools.task.imp.DefaultDTaskMessager;
import com.asura.tools.task.imp.DefaultDtaskGraphListener;

public class MultiThreadedDTaskExecutor implements DTaskExecutor {
	
	private final static Logger logger=LoggerFactory.getLogger(MultiThreadedDTaskExecutor.class);

	private final ExecutorService taskPool;

	private final ExecutorService managePool;

	private ConcurrentMap<DTaskGraph, Runner> map = new ConcurrentHashMap<>();

	public MultiThreadedDTaskExecutor() {
		taskPool = Executors.newCachedThreadPool();
		managePool = Executors.newCachedThreadPool();
	}

	public MultiThreadedDTaskExecutor(int maxNumWorkerThreads) {
		taskPool = Executors.newFixedThreadPool(maxNumWorkerThreads);
		managePool = Executors.newCachedThreadPool();
	}

	@Override
	public void submit(DTaskGraph taskGraph) throws InterruptedException, DependencyDoesNotExistException {
		submit(taskGraph, new DefaultDtaskGraphListener());
	}

	@Override
	public boolean awaitTermination(long timeout, TimeUnit units) throws InterruptedException {
		return managePool.awaitTermination(timeout, units);
	}

	@Override
	public void shutdown() {
		managePool.shutdown();
		taskPool.shutdown();
	}

	@Override
	public void shutdownNow() {
		managePool.shutdownNow();
		taskPool.shutdownNow();
	}

	@Override
	public boolean isShutdown() {
		return managePool.isShutdown();
	}

	@Override
	public boolean isTerminated() {
		return managePool.isTerminated();
	}

	@Override
	public void submit(DTaskGraph taskGraph, DTaskGraphListener listerner)
			throws InterruptedException, DependencyDoesNotExistException {
		taskGraph.verifyValidGraph();
		if (!map.containsKey(taskGraph)) {
			map.put(taskGraph, new Runner(taskGraph, listerner));
			managePool.execute(map.get(taskGraph));
		}
	}

	@Override
	public void waitDTaskGraphCompleted(DTaskGraph taskGraph) throws InterruptedException {
		if (map.containsKey(taskGraph)) {
			map.get(taskGraph).completed.await();
		} else {
			return;
		}
	}

	private class Runner implements Runnable {

		final DTaskGraph taskGraph;
		final CountDownLatch completed = new CountDownLatch(1);
		DTaskGraphListener listener = new DefaultDtaskGraphListener();

		public Runner(DTaskGraph taskGraph, DTaskGraphListener listener) {
			this.taskGraph = taskGraph;
			this.listener = listener;
		}

		@Override
		public void run() {
			try {
				ArrayBlockingQueue<DTaskWrapper> completionQueue = new ArrayBlockingQueue<>(taskGraph.numTasks());
				long currentlyExecuting = 0;
				listener.begin(taskGraph);
				while (true) {
					while (taskGraph.hasNextRunnalbeDTask()) {
						DTask t = taskGraph.nextRunnableDTask();
						DTaskWrapper wrapper = new DTaskWrapper(t, completionQueue);
						currentlyExecuting++;
						listener.traceStatus(taskGraph, t, STATE.START);
						taskPool.execute(wrapper);
					}
					if (currentlyExecuting > 0) {
						do {
							DTaskWrapper rw = completionQueue.take();
							currentlyExecuting--;
							listener.traceStatus(taskGraph, rw.innerTask, STATE.STOP);
							if (rw.getErr() == null) {
								taskGraph.notifyDone(rw.innerTask);
								listener.traceStatus(taskGraph, rw.innerTask, STATE.SUCCESS);
							} else {
								taskGraph.notifyError(rw.innerTask, rw.err);
								listener.traceStatus(taskGraph, rw.innerTask, STATE.ERROR);
							}
						} while (!completionQueue.isEmpty());
					}

					if (!taskGraph.hasNextRunnalbeDTask() && currentlyExecuting == 0) {
						return;
					}
				}

			} catch (InterruptedException e) {
				// do nothing
			} finally {
				listener.end(taskGraph);
				completed.countDown();
			}

		}

	}

	private class DTaskWrapper implements Runnable {

		private final DTask innerTask;
		private final ArrayBlockingQueue<DTaskWrapper> completionQueue;
		private Throwable err = null;

		DTaskWrapper(DTask t, ArrayBlockingQueue<DTaskWrapper> cq) {
			innerTask = t;
			completionQueue = cq;
		}

		@Override
		public void run() {
			try {
				innerTask.run();
			} catch (Throwable exception) {
				err = exception;
				logger.error("exception happened when run task "+innerTask.getGroup()+":"+innerTask.getName(), err);
			} finally {
				completionQueue.add(this);
			}
		}

		public Throwable getErr() {
			return err;
		}

	}
}
