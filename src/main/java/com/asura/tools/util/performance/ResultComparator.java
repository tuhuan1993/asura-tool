package com.asura.tools.util.performance;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import com.asura.tools.util.Combination;
import com.asura.tools.util.FileUtil;

public class ResultComparator {
	public static void main(String[] args) {
		PerformanceInfo info = PerformanceInfo
				.fromFile(PerformanceInfo.class.getClassLoader().getResource("performance.xml").getPath());

		HashMap map = new HashMap();
		for (String f : FileUtil.getAllFolderPath(info.getResultPath())) {
			map.put(f, new HashSet());
			for (String file : FileUtil.getAllFileNames(f)) {
				((HashSet) map.get(f)).add(file.replace(f, ""));
			}
		}

		List<List> llist = new Combination().getCombination((String[]) map.keySet().toArray(new String[0]), 2);
		for (List list : llist) {
			System.out.println("compare " + list);
			HashSet both = new HashSet();
			for (Iterator it = ((HashSet) map.get(list.get(0))).iterator(); it.hasNext();) {
				String s = (String) it.next();
				if (((HashSet) map.get(list.get(1))).contains(s)) {
					both.add(s);
				}
			}
			System.out.println("common:" + both.size());
			checkSame(both, (String) list.get(0), (String) list.get(1));
			System.out.println("compare " + list + " complete");
		}
	}

	private static void checkSame(HashSet<String> set, String f1, String f2) {
		for (String s : set) {
			String file1 = f1 + s;
			String file2 = f2 + s;

			if (FileUtil.getContent(file1, "utf8").equals(FileUtil.getContent(file2, "utf8")))
				System.out.println("diff:" + s + " at " + f1 + "  " + f2);
		}
	}
}
