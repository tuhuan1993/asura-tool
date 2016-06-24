package com.asura.tools.monitor;

import java.util.Date;
import java.util.HashMap;

import com.asura.tools.util.StringUtil;

public class MonitedValueUtil {
	private static final String OBJECT_FIELD_SPLITER = "Й";
	private static final String MAP_FIELD_SPLITER = "К";
	private static final String LINE_REPLACEMENT = "Л";
	private static HashMap<MonitedValue.MonitorLevel, Integer> levelValuemap = new HashMap();
	private static HashMap<Integer, MonitedValue.MonitorLevel> valueLevelmap = new HashMap();

	static {
		levelValuemap.put(MonitedValue.MonitorLevel.all, Integer.valueOf(1));
		valueLevelmap.put(Integer.valueOf(1), MonitedValue.MonitorLevel.all);

		levelValuemap.put(MonitedValue.MonitorLevel.debug, Integer.valueOf(2));
		valueLevelmap.put(Integer.valueOf(2), MonitedValue.MonitorLevel.debug);

		levelValuemap.put(MonitedValue.MonitorLevel.info, Integer.valueOf(3));
		valueLevelmap.put(Integer.valueOf(3), MonitedValue.MonitorLevel.info);

		levelValuemap.put(MonitedValue.MonitorLevel.warn, Integer.valueOf(4));
		valueLevelmap.put(Integer.valueOf(4), MonitedValue.MonitorLevel.warn);

		levelValuemap.put(MonitedValue.MonitorLevel.error, Integer.valueOf(5));
		valueLevelmap.put(Integer.valueOf(5), MonitedValue.MonitorLevel.error);
	}

	public static String toLogString(MonitedValue value) {
		if (value != null) {
			StringBuffer sb = new StringBuffer();
			sb.append(value.getEventId());
			sb.append("Й");
			sb.append(getLineReplaced(value.getKey()));
			sb.append("Й");
			sb.append(levelValuemap.get(value.getLevel()));
			sb.append("Й");
			sb.append(value.getDate().getTime());
			sb.append("Й");
			String[] fields = value.getFields();
			int i=0;
			if (fields.length > 0) {
				sb.append(fields[0]);
				sb.append("К");
				sb.append(getLineReplaced(value.getFieldValue(fields[0])));
				i = 1;
			}
			while (true) {
				sb.append("К");
				sb.append(getLineReplaced(fields[i]));
				sb.append("К");
				sb.append(getLineReplaced(value.getFieldValue(fields[i])));

				++i;
				if (i >= fields.length) {
					return sb.toString().trim();
				}
			}
		}
		return "";
	}

	private static String getLineReplaced(String string) {
		if (StringUtil.isNullOrEmpty(string)) {
			return "";
		}
		return string.replace("\n", "Л");
	}

	private static String getOrignalString(String string) {
		if (StringUtil.isNullOrEmpty(string)) {
			return "";
		}
		return string.replace("Л", "\n");
	}

	public static MonitedValue fromLogString(String string) {
		if (StringUtil.isNullOrEmpty(string)) {
			return null;
		}
		String[] objFs = string.split("Й");
		if (objFs.length == 5)
			try {
				MonitedValue mv = new MonitedValue();
				mv.setEventId(Long.valueOf(objFs[0]).longValue());
				mv.setKey(getOrignalString(objFs[1]));
				mv.setLevel((MonitedValue.MonitorLevel) valueLevelmap.get(Integer.valueOf(objFs[2])));
				mv.setDate(new Date(Long.valueOf(objFs[3]).longValue()));
				String[] mapFs = objFs[4].split("К");
				if (mapFs.length % 2 == 0) {
					for (int i = 0; i < mapFs.length; i += 2) {
						mv.addFieldValue(getOrignalString(mapFs[i]), getOrignalString(mapFs[(i + 1)]));
					}
				}

				return mv;
			} catch (Exception e) {
				e.printStackTrace();

				return null;
			}
		return null;
	}
}
