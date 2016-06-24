package com.asura.tools.util.performance;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import com.asura.tools.util.FileUtil;
import com.asura.tools.util.collection.Accumulator;

public class PerformanceResult {
	private Accumulator<TimeScope> acc;
	private Accumulator<TimeScope> detailAcc;
	private HashMap<Integer, TimeRecord> records;
	private long bytes;
	private Long[] times;
	private int printCount;
	private long startTime;
	private PerformanceInfo info;
	private HashSet<String> timeOuts;
	private int last;

	public Long[] getTimes() {
		return this.times;
	}

	public void setTimes(Long[] times) {
		this.times = times;
	}

	public int getPrintCount() {
		return this.printCount;
	}

	public void setPrintCount(int printCount) {
		if (this.acc == null) {
			inital();
		}
		this.printCount = printCount;
	}

	private void inital() {
		this.acc = new Accumulator();
		this.detailAcc = new Accumulator();
		this.records = new HashMap();
		this.timeOuts = new HashSet();
	}

	public PerformanceResult(Long[] times, int printCount, PerformanceInfo info) {
		this.printCount = printCount;
		this.times = times;
		this.info = info;
		inital();
	}

	public synchronized void record(String url, long time, int status, long bytes, String content) {
		if (this.startTime == 0L) {
			this.startTime = System.currentTimeMillis();
		}
		this.bytes += bytes;
		this.acc.addKey(getTimeScope(time));
		this.detailAcc.addKey(getDetailTimeScope(time));

		if (!(this.records.containsKey(Integer.valueOf(status)))) {
			this.records.put(Integer.valueOf(status), new TimeRecord());
		}

		if (time > 50L) {
			this.timeOuts.add(time + " " + url);
		}

		((TimeRecord) this.records.get(Integer.valueOf(status))).addTime(time);

		String folder = this.info.getResultPath() + "/" + this.info.getThreadCount() + "/";

		if (this.timeOuts.size() - this.last > 500) {
			FileUtil.output((String[]) this.timeOuts.toArray(new String[0]), folder + "timeouts", "utf8");
			this.last = this.timeOuts.size();
		}

		FileUtil.createFolder(folder);
		try {
			FileUtil.output(new String[] { content }, folder + url.substring(url.lastIndexOf("?") + 1).replace("&", ""),
					"utf8");
		} catch (Exception localException) {
		}
	}

	public long getCount() {
		int count = this.acc.getAllCount();
		if (count % this.printCount == 0) {
			System.out.println(count + " requests completed.");
			System.out.println(getReport());
			System.out.println("\n\n\n");
		}
		return count;
	}

	private TimeScope getDetailTimeScope(long time) {
		int pos = -1;
		for (int i = 0; i < 1000; ++i) {
			if (time < i * 10) {
				pos = i;
				break;
			}
		}

		if (pos == 0)
			return new TimeScope(0L, 10L);
		if (pos == -1) {
			return new TimeScope(10000L, 1000000L);
		}
		return new TimeScope((pos - 1) * 10, pos * 10);
	}

	private TimeScope getTimeScope(long time) {
		int pos = -1;
		for (int i = 0; i < this.times.length; ++i) {
			if (time < this.times[i].longValue()) {
				pos = i;
				break;
			}
		}

		if (pos == 0)
			return new TimeScope(0L, this.times[0].longValue());
		if (pos == -1) {
			return new TimeScope(this.times[(this.times.length - 1)].longValue(), 1000000L);
		}
		return new TimeScope(this.times[(pos - 1)].longValue(), this.times[pos].longValue());
	}

	public String getReport() {
		StringBuffer sb = new StringBuffer();
		sb.append("Performance Test Report:\n\n");
		try {
			sb.append("响应时间列表:\n");

			TimeScope[] tslist = (TimeScope[]) this.acc.getKeys().toArray(new TimeScope[0]);

			Arrays.sort(tslist);
			for (TimeScope ts : tslist) {
				sb.append(ts.getLow() + "-" + ts.getUp() + ": " + this.acc.getCount(ts));
				sb.append("\n");
			}

			sb.append("\n响应时间百分比:\n");
			tslist = (TimeScope[]) this.detailAcc.getKeys().toArray(new TimeScope[0]);
			Arrays.sort(tslist);
			int total = 0;
			int j = 0;
			for (TimeScope ts : tslist) {
				total += this.detailAcc.getCount(ts);
				if ((total * 100 / this.detailAcc.getAllCount() >= 50) && (j < 50)) {
					sb.append("50%: " + ts.getUp() + "ms\n");
					j = 50;
				} else if ((total * 100 / this.detailAcc.getAllCount() >= 66) && (j < 66)) {
					sb.append("66%: " + ts.getUp() + "ms\n");
					j = 66;
				} else if ((total * 100 / this.detailAcc.getAllCount() >= 75) && (j < 75)) {
					sb.append("76%: " + ts.getUp() + "ms\n");
					j = 75;
				} else if ((total * 100 / this.detailAcc.getAllCount() >= 80) && (j < 80)) {
					sb.append("80%: " + ts.getUp() + "ms\n");
					j = 80;
				} else if ((total * 100 / this.detailAcc.getAllCount() >= 90) && (j < 90)) {
					sb.append("90%: " + ts.getUp() + "ms\n");
					j = 90;
				} else if ((total * 100 / this.detailAcc.getAllCount() >= 95) && (j < 95)) {
					sb.append("95%: " + ts.getUp() + "ms\n");
					j = 95;
				} else if ((total * 100 / this.detailAcc.getAllCount() >= 98) && (j < 98)) {
					sb.append("98%: " + ts.getUp() + "ms\n");
					j = 98;
				} else if ((total * 100 / this.detailAcc.getAllCount() >= 99) && (j < 99)) {
					sb.append("99%: " + ts.getUp() + "ms\n");
					j = 99;
				} else {
					if ((total * 100 / this.detailAcc.getAllCount() < 100) || (j >= 100))
						continue;
					sb.append("100%: " + ts.getUp() + "ms\n");
					j = 100;
				}
			}

			sb.append("\n平均响应速度:\n");
			for (Integer status : this.records.keySet()) {
				sb.append(status + ": " + ((TimeRecord) this.records.get(status)).getAverage() + "  total:"
						+ ((TimeRecord) this.records.get(status)).getTotal() + "  count:"
						+ ((TimeRecord) this.records.get(status)).getCount());
				sb.append("\n");
			}
			sb.append("\n");

			sb.append("每秒事务数量: ");
			long allt = 0L;
			long allc = 0L;
			for (Integer status : this.records.keySet()) {
				allt += ((TimeRecord) this.records.get(status)).getTotal();
				allc += ((TimeRecord) this.records.get(status)).getCount();
			}

			double time = Double.valueOf(System.currentTimeMillis() - this.startTime).doubleValue();
			sb.append(Double.valueOf(1000L * allc).doubleValue() / time);
			sb.append("\n\n");

			sb.append("每秒网络传输：" + (Double.valueOf(this.bytes).doubleValue() / Double.valueOf(8.0D).doubleValue() / time)
					+ "(KByte).");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString().trim();
	}
}
