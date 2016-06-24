package com.asura.tools.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class WordSpliter implements ISpliter {
	private static Hashtable<String, WordSpliter> spliterTable = new Hashtable();
	public Map<String, Integer> dic = new HashMap();
	private static final String PATH = "song.dic";

	public WordSpliter() {
		intialDic("song.dic");
	}

	private WordSpliter(String fileName) {
		intialDic(fileName);
	}

	private void intialDic(String fileName) {
		try {
			InputStream stream = super.getClass().getClassLoader().getResourceAsStream(fileName);
			if (stream == null) {
				stream = new FileInputStream(fileName);
			}
			BufferedReader bfReader = new BufferedReader(new InputStreamReader(stream, "utf8"));
			while (bfReader.ready())
				try {
					String line = bfReader.readLine();
					if ((!(StringUtil.isNullOrEmpty(line))) && (line.contains(","))) {
						String[] names = line.split(",");
						if (!(names[0].trim().equals("")))
							if (this.dic.containsKey(names[0]))
								this.dic.put(names[0], Integer.valueOf(Integer.valueOf(names[1]).intValue()
										+ ((Integer) this.dic.get(names[0])).intValue()));
							else
								this.dic.put(names[0], Integer.valueOf(names[1]));
					}
				} catch (Exception localException) {
				}
		} catch (Exception localException1) {
		}
	}

	public static synchronized WordSpliter getInstance(String fileName) {
		if (StringUtil.isNullOrEmpty(fileName)) {
			fileName = "song.dic";
		}
		if (!(spliterTable.containsKey(fileName))) {
			spliterTable.put(fileName, new WordSpliter(fileName));
		}

		return ((WordSpliter) spliterTable.get(fileName));
	}

	public static synchronized WordSpliter getInstance() {
		return getInstance(null);
	}

	public String[] splitMore(String sentence) {
		String[] ss = split(sentence);
		List<RoundWord> rws = new ArrayList();
		for (String s : ss) {
			rws.add(new RoundWord(s));
		}
		for (int i = 0; i < ss.length; ++i) {
			if (ss[i].length() == 1) {
				if (i > 0) {
					String pre = ss[(i - 1)] + ss[i];
					if ((this.dic.containsKey(pre)) && (this.dic.containsKey(ss[(i - 1)]))
							&& (((Integer) this.dic.get(pre)).intValue() * 100 > ((Integer) this.dic.get(ss[(i - 1)]))
									.intValue())) {
						((RoundWord) rws.get(i - 1)).setRight(pre);
						((RoundWord) rws.get(i)).setUsed(true);
					}
				}

				if (i < ss.length - 1) {
					String suf = ss[i] + ss[(i + 1)];
					if ((!(this.dic.containsKey(suf))) || (!(this.dic.containsKey(ss[(i + 1)])))
							|| (((Integer) this.dic.get(suf)).intValue() * 100 <= ((Integer) this.dic.get(ss[(i + 1)]))
									.intValue()))
						continue;
					((RoundWord) rws.get(i + 1)).setLeft(suf);
					((RoundWord) rws.get(i)).setUsed(true);
				}

			}

		}

		List list = new ArrayList();
		for (RoundWord rw : rws) {
			if (!(rw.isUsed())) {
				list.addAll(rw.getWords());
			}
		}

		return ((String[]) list.toArray(new String[0]));
	}

	public String[] split(String sentence) {
		ArrayList list = new ArrayList();
		try {
			String[] subsens = CommonSpliter.filter(sentence);
			for (int i = 0; i < subsens.length; ++i)
				if (CommonSpliter.needSplit(subsens[i])) {
					String[] words = getFinalWords(subsens[i]);
					for (int j = 0; j < words.length; ++j)
						list.add(words[j]);
				} else {
					list.add(subsens[i]);
				}
		} catch (Exception localException) {
		}
		return ((String[]) list.toArray(new String[0]));
	}

	private String[] getFinalWords(String string) {
		Map<String, Integer> matchTables = getMatchWords(this.dic, string);

		Map posTable = new HashMap();
		List posList = new ArrayList();
		for (String key : matchTables.keySet()) {
			int[] ids = StringUtil.getAllIndex(string, key);
			for (int i = 0; i < ids.length; ++i) {
				Position pos = new Position(ids[i] + 1, ids[i] + key.length());
				posTable.put(pos, key);
				posList.add(pos);
			}
		}

		Collections.sort(posList, new PositionComparator());
		List<List> slist = new ArrayList();
		Object plist = new ArrayList();
		Position lastP = new Position();
		boolean toolong = false;
		for (int i = 0; i < posList.size(); ++i) {
			if (lastP.getEnd() < ((Position) posList.get(i)).getStart()) {
				if (((List) plist).size() > 0) {
					slist.add((List) plist);
				}
				plist = new ArrayList();
				toolong = false;
			}
			if ((((List) plist).size() > 50) || (getAllPosibility((List) plist).size() > 1000)) {
				if (((List) plist).size() > 0) {
					slist.add((List) plist);
				}
				plist = new ArrayList();
				toolong = true;
			}
			if (!(toolong)) {
				((List) plist).add((Position) posList.get(i));
				lastP = (Position) posList.get(i);
				if ((i != posList.size() - 1) || (((List) plist).size() <= 0))
					continue;
				slist.add((List) plist);
			}

		}

		List resultList = new ArrayList();
		for (List sectionList : slist) {
			resultList.addAll(getMaxList(getAllPosibility(sectionList), matchTables, posTable));
		}

		return (getMaxString(matchTables, resultList, posTable, string));
	}

	private List<Position> getMaxList(List<List<Position>> llist, Map<String, Integer> matchTables,
			Map<Position, String> posTable) {
		int max = 0;
		List maxList = new ArrayList();
		for (List<Position> list : llist) {
			int value = 0;
			for (Position pos : list) {
				value += ((Integer) matchTables.get(posTable.get(pos))).intValue();
			}
			if (value > max) {
				max = value;
				maxList = list;
			}
		}

		return maxList;
	}

	private String[] getMaxString(Map<String, Integer> matchTables, List<Position> maxList,
			Map<Position, String> posTable, String string) {
		if (maxList.size() > 0) {
			List newList = new ArrayList();
			newList.add(new Position(0, ((Position) maxList.get(0)).getStart() - 1));
			for (Position pos : maxList) {
				newList.add(new Position(pos.getStart() - 1, pos.getEnd()));
			}
			if (((Position) maxList.get(maxList.size() - 1)).getEnd() < string.length()) {
				newList.add(new Position(((Position) maxList.get(maxList.size() - 1)).getEnd(), string.length()));
			}
			List result = new ArrayList();
			for (int i = 0; i < newList.size(); ++i) {
				String value = string.substring(((Position) newList.get(i)).getStart(),
						((Position) newList.get(i)).getEnd());
				if (!(StringUtil.isNullOrEmpty(value))) {
					result.add(value);
				}
				if ((i == newList.size() - 1)
						|| (((Position) newList.get(i + 1)).getStart() <= ((Position) newList.get(i)).getEnd()))
					continue;
				result.add(string.substring(((Position) newList.get(i)).getEnd(),
						((Position) newList.get(i + 1)).getStart()));
			}

			Object sresult = new ArrayList();
			Iterator localIterator2 = result.iterator();
			while (true) {
				String s = (String) localIterator2.next();
				((List) sresult).addAll(Arrays.asList(spitNotWord(s, matchTables)));

				if (!(localIterator2.hasNext())) {
					return ((String[]) ((List) sresult).toArray(new String[0]));
				}
			}
		}
		return (new String[] { string });
	}

	private String[] spitNotWord(String s, Map<String, Integer> matchTables) {
		List result = new ArrayList();
		if ((!(matchTables.containsKey(s))) && (StringUtil.isAllChineseCharacter(s))) {
			for (char c : s.toCharArray())
				result.add(String.valueOf(c));
		} else {
			result.add(s);
		}

		return ((String[]) result.toArray(new String[0]));
	}

	private List<List<Position>> getAllPosibility(List<Position> posList) {
		List llist = new ArrayList();
		for (Position pos : posList) {
			boolean added = false;
			for (int i = 0; i < llist.size(); ++i) {
				List<Position> list = (List) llist.get(i);
				if ((list.size() <= 0) || (pos.isInclusivePosition((Position) list.get(list.size() - 1))))
					continue;
				List newlist = new ArrayList();
				for (Position pp : list) {
					newlist.add(pp);
				}
				newlist.add(pos);
				llist.add(newlist);
				added = true;
			}

			if (!(added)) {
				List newlist = new ArrayList();
				newlist.add(pos);
				llist.add(newlist);
			}
		}

		return llist;
	}

	private Map<String, Integer> getMatchWords(Map<String, Integer> dic, String sentence) {
		Hashtable table = new Hashtable();
		for (int i = 1; i < 5; ++i) {
			String[] words = Spliter.split(sentence, i);
			for (int j = 0; j < words.length; ++j) {
				if (dic.containsKey(words[j])) {
					table.put(words[j], (Integer) dic.get(words[j]));
				}
			}
		}

		return table;
	}
}
