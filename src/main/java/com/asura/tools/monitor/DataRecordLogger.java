package com.asura.tools.monitor;

import java.io.File;
import java.util.HashMap;

import com.asura.tools.data.DataRecord;

public class DataRecordLogger {
	private MultiLogger logger;
	private long id;
	private boolean valid;
	private static HashMap<Long, DataRecordLogger> map = new HashMap();

	private DataRecordLogger(long id) {
		this.id = id;
		try {
			initial();
			this.valid = true;
		} catch (Exception e) {
			this.valid = false;
		}
	}

	public static synchronized DataRecordLogger getLogger(long id) {
		if (!(map.containsKey(Long.valueOf(id)))) {
			map.put(Long.valueOf(id), new DataRecordLogger(id));
		}

		return ((DataRecordLogger) map.get(Long.valueOf(id)));
	}

	public static synchronized DataRecordLogger getLogger(String key) {
		long id = EventLogger.getCurrentEventId(key);
		if (!(map.containsKey(Long.valueOf(id)))) {
			map.put(Long.valueOf(id), new DataRecordLogger(id));
		}

		return ((DataRecordLogger) map.get(Long.valueOf(id)));
	}

	private void initial() throws Exception {
		File file = new File(FileHierarchy.getMachineRecordsFolder());
		if (!(file.exists())) {
			file.mkdirs();
		}
		file = new File(FileHierarchy.getManualRecordsFolder());
		if (!(file.exists())) {
			file.mkdirs();
		}
		this.logger = new MultiLogger(
				new FileLogger(FileHierarchy.getManualRecordsFolder() + String.valueOf(this.id) + ".log"),
				new FileLogger(FileHierarchy.getMachineRecordsFolder() + String.valueOf(this.id) + ".log"));
	}

	public void recordError(DataRecord dr) {
		if (this.valid) {
			MonitedValue value = new MonitedValue();
			for (String field : dr.getAllFields()) {
				String fValue = dr.getFieldValue(field);
				fValue = fValue.replace("\n", "");
				fValue = fValue.replace("\r", "");
				if (fValue.length() > 20) {
					fValue = fValue.substring(0, 20) + "...";
				}
				value.addFieldValue(field, dr.getFieldValue(field));
			}
			value.setLevel(MonitedValue.MonitorLevel.error);
			value.setEventId(this.id);

			this.logger.error(value.toManualString() + "\n", value.toMachineString() + "\n");
		}
	}

	public void recordInfo(DataRecord dr) {
		if (this.valid) {
			MonitedValue value = new MonitedValue();
			for (String field : dr.getAllFields()) {
				String fValue = dr.getFieldValue(field);
				fValue = fValue.replace("\n", "");
				fValue = fValue.replace("\r", "");
				if (fValue.length() > 20) {
					fValue = fValue.substring(0, 20) + "...";
				}
				value.addFieldValue(field, dr.getFieldValue(field));
			}
			value.setLevel(MonitedValue.MonitorLevel.info);
			value.setEventId(this.id);

			this.logger.info(value.toManualString() + "\n", value.toMachineString() + "\n");
		}
	}

	public void endRecord() {
		if (this.logger != null) {
			this.logger.close();
			map.remove(Long.valueOf(this.id));
		}
	}
}
