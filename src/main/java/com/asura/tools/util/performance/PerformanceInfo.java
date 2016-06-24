package com.asura.tools.util.performance;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class PerformanceInfo {
	private int threadCount;
	private String path;
	private String resultPath;
	private int exeTime;
	private int printCount;
	private List<Long> times;
	private ITimeCollector collector;

	public ITimeCollector getCollector() {
		return this.collector;
	}

	public void setCollector(ITimeCollector collector) {
		this.collector = collector;
	}

	public PerformanceInfo() {
		this.times = new ArrayList();
	}

	public String getResultPath() {
		return this.resultPath;
	}

	public void setResultPath(String resultPath) {
		this.resultPath = resultPath;
	}

	public void addTime(long time) {
		this.times.add(Long.valueOf(time));
	}

	public List<Long> getTimes() {
		return this.times;
	}

	public void setTimes(List<Long> times) {
		this.times = times;
	}

	public int getThreadCount() {
		return this.threadCount;
	}

	public void setThreadCount(int threadCount) {
		this.threadCount = threadCount;
	}

	public int getExeTime() {
		return this.exeTime;
	}

	public void setExeTime(int exeTime) {
		this.exeTime = exeTime;
	}

	public String getPath() {
		return this.path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public int getPrintCount() {
		return this.printCount;
	}

	public void setPrintCount(int printCount) {
		this.printCount = printCount;
	}

	public static PerformanceInfo fromFile(String fileName) {
		XStream xs = getXStream();
		try {
			return ((PerformanceInfo) xs.fromXML(new FileInputStream(new File(fileName))));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static String toXML(PerformanceInfo info) {
		return getXStream().toXML(info);
	}

	private static XStream getXStream() {
		XStream xs = new XStream(new DomDriver());
		xs.alias("performance", PerformanceInfo.class);
		xs.aliasAttribute(PerformanceInfo.class, "exeTime", "exe-time");
		xs.aliasAttribute(PerformanceInfo.class, "threadCount", "thread-count");
		xs.aliasAttribute(PerformanceInfo.class, "printCount", "print-count");
		xs.aliasAttribute(PerformanceInfo.class, "path", "path");
		xs.aliasAttribute(PerformanceInfo.class, "resultPath", "result-path");

		xs.alias("search-time-collector", SearchTimeCollector.class);
		return xs;
	}
}
