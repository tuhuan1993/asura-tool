package com.asura.tools.data.dictionary;

import java.util.ArrayList;
import java.util.List;

import com.asura.tools.util.FileUtil;

public class DictionrayPrinter {
	public static void main(String[] args) {
		String[] lines = FileUtil.getContentByLine("C:/Users/Administrator/Desktop/1.txt");
		List list = new ArrayList();
		for (String line : lines) {
			String[] ss = line.split(",");
			for (int i = 0; i < ss.length - 1; i += 2) {
				String word = ss[i].trim() + "," + ss[(i + 1)].trim();
				System.out.println(word);
				list.add(word);
			}
		}

		FileUtil.output((String[]) list.toArray(new String[0]), "C:/Users/Administrator/Desktop/2.txt", "GBK");
	}
}
