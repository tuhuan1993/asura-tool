package com.asura.tools.annotation.task;

import static org.junit.Assert.*;

import org.junit.Test;

import com.asura.tools.annotation.parser.AnnotationParserFactoryBuilder;
import com.asura.tools.task.AbstractDTask;
import com.asura.tools.task.DTaskContext;
import com.asura.tools.task.DTaskGraph;
import com.asura.tools.task.MultiThreadedDTaskExecutor;
import com.asura.tools.task.imp.DefaultDTaskMessager;

public class TestTaskAnnotation {

	@Test
	public void test() throws Exception {
		TaskAnnotationParserFactory factory=new AnnotationParserFactoryBuilder.Builder<TaskAnnotationParserFactory>("com.asura.tools.annotation.task").build(Task.class, new TaskAnnotationParserFactory());
		DTaskGraph graph=factory.getTaskGraph("test", new DefaultDTaskMessager(), new DTaskContext());
		MultiThreadedDTaskExecutor executor = new MultiThreadedDTaskExecutor();
		executor.submit(graph);
		executor.waitDTaskGraphCompleted(graph);
		System.out.println(graph.getZonbieTasks().size());
		System.out.println(graph.getFailedTasks().size());
		System.out.println(graph.numTasks());
	}
	
	@Task(name="t1",group="test")
	public static class Task1 extends AbstractDTask{

		@Override
		public void run() {
			System.out.println("task begin"+getName());
			if(getDependency()!=null){
				for(String depend:getDependency()){
					System.out.println(depend);
				}
			}
			System.out.println("task end"+getName());
			isDone=true;
			isSuccess=true;
		}
		
	}

	@Task(name="t2",group="test",dependency={})
	public static class Task2 extends AbstractDTask{

		@Override
		public void run() {
			System.out.println("task begin"+getName());
			if(getDependency()!=null){
				for(String depend:getDependency()){
					System.out.println(depend);
				}
			}
			System.out.println("task end"+getName());
			isDone=true;
			isSuccess=true;
		}
		
	}
	
	@Task(name="t3",group="test",dependency={""})
	public static class Task3 extends AbstractDTask{

		@Override
		public void run() {
			System.out.println("task begin"+getName());
			if(getDependency()!=null){
				for(String depend:getDependency()){
					System.out.println(depend);
				}
			}
			System.out.println("task end"+getName());
			isDone=true;
			isSuccess=true;
		}
		
	}
	
	@Task(name="t4",group="test",dependency={"t1","t2"})
	public static class Task4 extends AbstractDTask{

		@Override
		public void run() {
			System.out.println("task begin"+getName());
			if(getDependency()!=null){
				for(String depend:getDependency()){
					System.out.println(depend);
				}
			}
			System.out.println("task end"+getName());
			isDone=true;
			isSuccess=true;
		}
		
	}
	
	@Task(name="t5",group="test",dependency={"t1","t4"})
	public static class Task5 extends AbstractDTask{

		@Override
		public void run() {
			System.out.println("task begin"+getName());
			if(getDependency()!=null){
				for(String depend:getDependency()){
					System.out.println(depend);
				}
			}
			System.out.println("task end"+getName());
			isDone=true;
			isSuccess=true;
		}
		
	}
}
