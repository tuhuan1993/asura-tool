package com.asura.tools.monitor;

public class MultiLogger {
	private ILogger manualLogger;
	private ILogger machineLogger;

	public MultiLogger(ILogger manual, ILogger machine) {
		this.machineLogger = machine;
		this.manualLogger = manual;
	}

	public void close() {
		this.machineLogger.close();
		this.manualLogger.close();
	}

	public void debug(String man, String machine) {
		this.machineLogger.debug(machine);
		this.manualLogger.debug(man);
	}

	public void error(String man, String machine) {
		this.machineLogger.error(machine);
		this.manualLogger.error(man);
	}

	public void info(String man, String machine) {
		this.machineLogger.info(machine);
		this.manualLogger.info(man);
	}

	public void warn(String man, String machine) {
		this.machineLogger.warn(machine);
		this.manualLogger.warn(man);
	}
}
