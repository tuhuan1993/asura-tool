package com.asura.tools.data.dictionary;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.asura.tools.data.DataIterator;
import com.asura.tools.data.DataRecord;
import com.asura.tools.data.newmysql.ConnectionInformation;
import com.asura.tools.data.newmysql.MysqlHandler;
import com.asura.tools.sql.SelectSQL;
import com.asura.tools.util.CommonSpliter;
import com.asura.tools.util.FileUtil;
import com.asura.tools.util.StringUtil;

public class DictionaryGenerator {
	public static void main(String[] args) {
		ConnectionInformation ci = new ConnectionInformation();
		ci.setDbName("appsearch");
		ci.setHost("58.215.65.52");
		ci.setPassword("2010GetAFan");
		ci.setUserName("root");
		ci.setPort("3306");

		MysqlHandler handler = new MysqlHandler(ci);

		SelectSQL sql = new SelectSQL("app_info", new String[] { "appname" });

		DataIterator it = handler.select(sql);

		new DictionaryGenerator().generate(it, new String[] { "name" }, "D:/test.dic");
	}

	public void generate(DataIterator<DataRecord> it, String[] fields, String fileName) {
		WordMap map = new WordMap();

		int count = 0;

		while (it.hasNext()) {
			System.out.println("split:" + (count++));
			DataRecord dr = (DataRecord) it.next();
			String name = "";
			for (String s : fields) {
				name = name + " " + dr.getFieldValue(s);
			}

		}

		count = 0;
		List<WordResult> result = new ArrayList();
		while (map.hasNext()) {
			System.out.println("compute:" + (count++));
			WordSet[] sets = map.next();
			for (WordSet set : sets) {
				if (set.size() > 0) {
					result.add(new WordResult(set.getKey(), set.size()));
				}
			}
		}

		Collections.sort(result);
		List words = new ArrayList();
		for (WordResult wr : result) {
			words.add(wr.toString());
		}

		FileUtil.output((String[]) words.toArray(new String[0]), fileName, "UTF8");
	}

	private WordPosition[] getSingleWords(String string) {
		string = StringUtil.getStandardString(string);
		WordSource source = new WordSource(string);
		List list = new ArrayList();
		String[] strings = CommonSpliter.getChineseEnglishSeparatedString(string);
		for (String str : strings) {
			for (int i = 2; i <= Math.min(4, str.length()); ++i) {
				if (StringUtil.containsChinese(str)) {
					String[] ss = CommonSpliter.split(str, i);
					for (int j = 0; j < ss.length; ++j) {
						if (StringUtil.containsChinese(ss[j])) {
							WordPosition word = new WordPosition();
							word.setSource(source);
							word.setWord(ss[j]);
							word.setStart(j + 1);
							word.setEnd(j + i);

							list.add(word);
						}
					}
				}
			}
		}

		return ((WordPosition[]) list.toArray(new WordPosition[0]));
	}
}
