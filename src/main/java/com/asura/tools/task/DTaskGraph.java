package com.asura.tools.task;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.asura.tools.task.exception.DependencyDoesNotExistException;
import com.google.common.collect.ArrayListMultimap;

public class DTaskGraph {

	private final Set<DTask> tasks = new HashSet<>();

	private final ArrayListMultimap<DTask, DTask> dependencies = ArrayListMultimap.create();
	
	private final ArrayListMultimap<DTask, DTask> revertDependencies=ArrayListMultimap.create();
	
	private final Set<DTask> failedTasks=new HashSet<>();
	
	private final Set<DTask> zombieTasks=new HashSet<>();

	private Map<DTask, Throwable> errors = null;

	public enum Status {
		COMPLTED_ALL_TASKS, ERRORS, INVALID_DEPENDENCIES
	}

	public synchronized Status status() {
		if (tasks.size() == 0) {
			return Status.COMPLTED_ALL_TASKS;
		}

		if (errors != null) {
			return Status.ERRORS;
		}

		if (tasks.size() > 0) {
			return Status.INVALID_DEPENDENCIES;
		}

		throw new RuntimeException("unknown state");
	}

	public synchronized Map<DTask, Throwable> getErrors() {
		return errors;
	}

	private synchronized DTask peekNextRunnableDTask() {
		for (DTask t : tasks) {
			if (dependencies.containsKey(t)) {
				List<DTask> v = dependencies.get(t);
				if (v.isEmpty()) {
					return t;
				}
			} else {
				return t;
			}
		}
		return null;
	}

	public synchronized boolean hasNextRunnalbeDTask() {
		return peekNextRunnableDTask() != null;
	}

	public synchronized boolean hasTasks() {
		return tasks.size() > 0;
	}

	public synchronized void insert(DTask task) {
		if(task==null){
			throw new RuntimeException("task should not be null");
		}
		tasks.add(task);
	}

	public synchronized void insert(DTask task, DTask dependency) {
		if(task==null){
			throw new RuntimeException("task should not be null");
		}
		if(dependency==null){
			throw new RuntimeException("dependency task should not be null");
		}
		tasks.add(task);
		tasks.add(dependency);
		dependencies.put(task, dependency);
		revertDependencies.put(dependency, task);
	}

	public synchronized void insert(DTask task, Set<DTask> dependencies) {
		tasks.add(task);
		tasks.addAll(dependencies);
		this.dependencies.putAll(task, dependencies);
		for(DTask t:dependencies){
			this.revertDependencies.put(t, task);
		}
	}
	
	public synchronized List<DTask> getDependency(DTask task){
		return dependencies.get(task);
	}
	
	public synchronized List<DTask> getRevertDependencies(DTask task){
		return revertDependencies.get(task);
	}
	
	public synchronized Set<DTask> getFailedTasks(){
		return failedTasks;
	}
	
	public synchronized Set<DTask> getZonbieTasks(){
		return zombieTasks;
	}

	public synchronized DTask nextRunnableDTask() {
		DTask t = peekNextRunnableDTask();
		tasks.remove(t);
		return t;
	}

	public synchronized void notifyDone(DTask task) {
		if (task.isDone() && task.isSuccess()) {
			while(dependencies.values().contains(task)){
				dependencies.values().remove(task);
			}
		}else{
			failedTasks.add(task);
			calculateZombieTasks(task);
		}
	}
	
	private synchronized void calculateZombieTasks(DTask task){
		if(revertDependencies.get(task).size()==0){
			return;
		}else{
			for(DTask t:revertDependencies.get(task)){
				zombieTasks.add(t);
				calculateZombieTasks(t);
			}
		}
	}
	
	public synchronized void notifyError(DTask t, Throwable error){
		if(errors==null){
			errors=new HashMap<>();
		}
		errors.put(t, error);
		failedTasks.add(t);
		calculateZombieTasks(t);
	}
	
	public synchronized int numTasks(){
		return tasks.size();
	}
	
	public synchronized int numZombieTasks(){
		return zombieTasks.size();
	}
	
	public synchronized int numFailedTasks(){
		return failedTasks.size();
	}
	
	public void verifyValidGraph() throws DependencyDoesNotExistException{
		for(DTask t:dependencies.values()){
			if(!tasks.contains(t)){
				throw new DependencyDoesNotExistException(t);
			}
		}
		
		for(DTask t:revertDependencies.values()){
			if(!tasks.contains(t)){
				throw new DependencyDoesNotExistException(t);
			}
		}
	}

}
