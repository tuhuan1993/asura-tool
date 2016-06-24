package com.asura.tools.util.performance;

import com.asura.tools.util.RegularExpressionUtil;

public class SearchTimeCollector implements ITimeCollector {
	private PerformanceResult result;

	public PerformanceResult getResult() {
		if (this.result == null) {
			this.result = new PerformanceResult(new Long[] { Long.valueOf(10L), Long.valueOf(20L) }, 10, null);
		}

		return this.result;
	}

	public long getTime(String content) {
		String[] ss = RegularExpressionUtil.getPatternValue(content, "耗时：(\\d*)");
		if (ss.length > 0) {
			content = ss[0].replace("耗时：", "");
			return Long.valueOf(content).longValue();
		}
		return -1L;
	}
}