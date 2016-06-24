package com.asura.tools.monitor;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import com.asura.tools.data.DataRecord;

public class DataRecordParser {
	public static boolean hasRecord(String path, long eventId) {
		if (!(path.endsWith("/"))) {
			path = path + "/";
		}
		path = FileHierarchy.getMachineRecordsFolder(path);

		File file = new File(path);
		File[] files = file.listFiles();
		if (files != null) {
			HashSet logs = new HashSet();
			File[] arrayOfFile1;
			int j = (arrayOfFile1 = files).length;
			int i = 0;
			while (true) {
				File f = arrayOfFile1[i];
				logs.add(f.getName());

				++i;
				if (i >= j) {
					return logs.contains(eventId + ".log");
				}
			}
		}
		return false;
	}

	public static List<DataRecord> parseRecord(String path, long eventId, int page, int countPerPage) {
		if (!(path.endsWith("/"))) {
			path = path + "/";
		}
		path = FileHierarchy.getMachineRecordsFolder(path);

		File file = new File(path);
		File[] files = file.listFiles();
		Arrays.sort(files, new FileComparator());

		File[] neededFiles = new File[1];
		for (File f : files) {
			if ((!(f.getName().contains(String.valueOf(eventId)))) || (!(f.isFile()))
					|| (!(f.getName().endsWith(".log"))))
				continue;
			neededFiles[0] = f;
			break;
		}

		List result = getValueList(neededFiles, page, countPerPage);

		Object datas = new ArrayList();
		for (Iterator it = result.iterator(); it.hasNext();) {
			MonitedValue mv = (MonitedValue) it.next();
			DataRecord dr = new DataRecord();
			for (String field : mv.getFields()) {
				dr.AddField(field, mv.getFieldValue(field));
			}
			((List) datas).add(dr);
		}

		return ((List<DataRecord>) (List<DataRecord>) datas);
	}

	private static List<MonitedValue> getValueList(File[] files, int page, int countPerPage) {
		List values = new ArrayList();
		for (File f : files) {
			try {
				FileInputStream inputStream = new FileInputStream(f);
				LineNumberReader bfReader = new LineNumberReader(new InputStreamReader(inputStream, "UTF8"));

				int start = (page - 1) * countPerPage;
				bfReader.setLineNumber(start);
				while (bfReader.ready()) {
					MonitedValue mv = MonitedValue.fromLogString(bfReader.readLine().trim());
					if (mv != null) {
						values.add(mv);
					}
					if (values.size() == countPerPage) {
						break;
					}
				}

				bfReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return values;
	}
}
