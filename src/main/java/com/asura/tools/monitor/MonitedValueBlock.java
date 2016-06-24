package com.asura.tools.monitor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.asura.tools.util.SerializeUtil;

public class MonitedValueBlock implements Serializable {
	private static final long serialVersionUID = 1217747893438529901L;
	private static final String BLOCK_START = "blockstart";
	private static final String BLOCK_END = "blockend";
	private List<MonitedValue> list;

	public MonitedValueBlock() {
		this.list = new ArrayList();
	}

	public void addMonitedValue(MonitedValue value) {
		this.list.add(value);
	}

	public void addMonitedValueAtBeginning(MonitedValue value) {
		this.list.add(0, value);
	}

	public List<MonitedValue> getList() {
		return this.list;
	}

	public StandardDate getStartDate() {
		if (this.list.size() > 0) {
			return new StandardDate(((MonitedValue) this.list.get(0)).getDate());
		}

		return new StandardDate();
	}

	public MonitedValue.MonitorLevel getLowestLevel() {
		MonitedValue.MonitorLevel level = MonitedValue.MonitorLevel.info;
		if (this.list.size() > 0) {
			for (MonitedValue mv : this.list) {
				if (mv.getLevel() == MonitedValue.MonitorLevel.error) {
					level = MonitedValue.MonitorLevel.error;
				} else {
					if ((mv.getLevel() != MonitedValue.MonitorLevel.warn) || (level == MonitedValue.MonitorLevel.error))
						continue;
					level = MonitedValue.MonitorLevel.warn;
				}
			}
		}

		return level;
	}

	public String getKey() {
		if (this.list.size() > 0) {
			return ((MonitedValue) this.list.get(0)).getKey();
		}

		return "no key";
	}

	public long getEventId() {
		if (this.list.size() > 0) {
			return ((MonitedValue) this.list.get(0)).getEventId();
		}

		return -1L;
	}

	public StandardDate getEndDate() {
		if (this.list.size() > 0) {
			return new StandardDate(((MonitedValue) this.list.get(this.list.size() - 1)).getDate());
		}

		return new StandardDate();
	}

	public String toLogString() {
		return "blockstart" + SerializeUtil.ObjectToByteString(this) + "blockend";
	}

	public String toManualString() {
		StringBuffer sb = new StringBuffer();
		for (MonitedValue mv : this.list) {
			if (mv.isMessage())
				sb.append(
						new StandardDate(mv.getDate()).toDateString() + "  " + mv.getLevel() + "  " + mv.getMessage());
			else {
				sb.append(mv.toManualString());
			}
			sb.append("\n");
		}

		return sb.toString().trim();
	}

	public static MonitedValueBlock fromLogString(String logString) {
		if ((logString.contains("blockstart")) && (logString.contains("blockend"))) {
			String value = logString.substring(logString.indexOf("blockstart"), logString.indexOf("blockend"))
					.replace("blockstart", "").replace("blockend", "");
			return ((MonitedValueBlock) SerializeUtil.ByteStringToObject(value));
		}

		return null;
	}

}
