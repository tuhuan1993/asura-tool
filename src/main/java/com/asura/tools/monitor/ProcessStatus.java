package com.asura.tools.monitor;

public class ProcessStatus {
	private double totalMemory;
	private double maxMemory;
	private double freeMemory;

	private ProcessStatus() {
		this.totalMemory = (Runtime.getRuntime().totalMemory() / 1024L / 1024L);
		this.maxMemory = (Runtime.getRuntime().maxMemory() / 1024L / 1024L);
		this.freeMemory = (Runtime.getRuntime().freeMemory() / 1024L / 1024L);
	}

	public static ProcessStatus newProcessStatus() {
		return new ProcessStatus();
	}

	public double getMaxMemory() {
		return this.maxMemory;
	}

	public double getFreeMemory() {
		return this.freeMemory;
	}

	public double getTotalMemory() {
		return this.totalMemory;
	}
}
