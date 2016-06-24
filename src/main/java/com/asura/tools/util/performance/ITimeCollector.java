package com.asura.tools.util.performance;

public interface ITimeCollector {
	public long getTime(String paramString);

	public PerformanceResult getResult();
}
