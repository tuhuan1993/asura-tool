package com.asura.tools.monitor;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class EventParser {
	public static List<MonitedValueBlock> parseEvent(EventRequest request) {
		return parseEvent(request.getPaths(), request.getFilter());
	}

	public static List<MonitedValueBlock> parseEvent(String[] paths, MonitorFilter filter) {
		for (int i = 0; i < paths.length; ++i) {
			paths[i] = FileHierarchy.getMachineEventsFolder(paths[i]);
		}

		Iterator valueList = getValueListInScope(paths, filter);

		List result = new ArrayList();

		int count = 0;
		long currentId = -1L;
		MonitedValueBlock block = null;
		while (valueList.hasNext()) {
			MonitedValue mv = (MonitedValue) valueList.next();
			if (count > filter.getLatestCount())
				break;
			if (mv.getEventId() != currentId) {
				if (block != null) {
					result.add(block);
				}
				block = new MonitedValueBlock();
				block.addMonitedValueAtBeginning(mv);
				currentId = mv.getEventId();
				++count;
			} else {
				block.addMonitedValueAtBeginning(mv);
			}

		}

		if ((result.size() < filter.getLatestCount()) && (block != null)) {
			result.add(block);
		}

		return result;
	}

	private static Iterator<MonitedValue> getValueListInScope(String[] paths, MonitorFilter filter) {
		List fileList = new ArrayList();
		for (String path : paths) {
			File file = new File(path + filter.getKey());
			File[] files = file.listFiles();
			if (files != null) {
				for (File f : files) {
					fileList.add(f);
				}
			}
		}

		File[] files = (File[]) fileList.toArray(new File[0]);
		Arrays.sort(files, new FileComparator());

		File[] neededFiles = getNeededFiles(files, filter.getDateFilter());
		System.out.println("扫描日志文件的个数：" + neededFiles.length);
		return getValueList(neededFiles, filter);
	}

	private static Iterator<MonitedValue> getValueList(final File[] files, final MonitorFilter filter) {
		return new Iterator<MonitedValue>() {
			Iterator<File> fileIt;
			Iterator<MonitedValue> valueIt;
			{
				fileIt=Arrays.asList(files).iterator();
			}
			
			public boolean hasNext() {
				if(valueIt==null||!valueIt.hasNext()){
					valueIt=EventParser.getValues(fileIt.next(), filter).iterator();
					if(valueIt.hasNext()){
						return true;
					}
				}else{
					return valueIt.hasNext();
				}
				return false;
			}

			public MonitedValue next() {
				return ((MonitedValue) this.valueIt.next());
			}

			public void remove() {
			}
		};
	}

	private static List<MonitedValue> getValues(File file, MonitorFilter filter) {
		List result = new ArrayList();
		try {
			FileInputStream inputStream = new FileInputStream(file);
			LineNumberReader bfReader = new LineNumberReader(new InputStreamReader(inputStream, "UTF8"));

			while (bfReader.ready()) {
				MonitedValue mv = MonitedValue.fromLogString(bfReader.readLine().trim());
				if (mv != null) {
					if (filter.getFieldFilter() != null)
						if (!(filter.getFieldFilter().meet(mv)))
							continue;
					if (filter.getDateFilter() != null) {
						if (!(filter.getDateFilter().concludeDate(new StandardDate(mv.getDate()))))
							continue;
					}
					if (filter.getKey() != null)
						if (!(filter.getKey().equals(mv.getKey())))
							continue;
					if (filter.getLevel() != null)
						if (filter.getLevel() != mv.getLevel())
							continue;
					result.add(0, mv);
				}

			}

			inputStream.close();
			bfReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return result;
	}

	private static File[] getNeededFiles(File[] files, DateFilter dateFilter) {
		if (dateFilter == null) {
			return files;
		}

		List neededFiles = new ArrayList();

		File lastAferEnd = null;
		for (int i = 0; i < files.length; ++i) {
			StandardDate fileDate = new StandardDate(files[i].lastModified());
			if (((fileDate.before(dateFilter.getEnd())) && (fileDate.after(dateFilter.getStart())))
					|| (fileDate.isSame(dateFilter.getEnd())) || (fileDate.isSame(dateFilter.getStart()))) {
				if ((files[i].isFile()) && (files[i].getName().contains(".log")))
					neededFiles.add(files[i]);
			} else if (fileDate.after(dateFilter.getEnd())) {
				lastAferEnd = files[i];
			} else {
				if ((!(fileDate.before(dateFilter.getStart()))) || (!(files[i].isFile()))
						|| (!(files[i].getName().contains(".log"))))
					continue;
				neededFiles.add(files[i]);
				break;
			}

		}

		if ((lastAferEnd != null) && (lastAferEnd.isFile()) && (lastAferEnd.getName().contains(".log"))) {
			neededFiles.add(lastAferEnd);
		}

		return ((File[]) neededFiles.toArray(new File[0]));
	}
}
