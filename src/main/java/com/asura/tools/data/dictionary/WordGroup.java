package com.asura.tools.data.dictionary;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.asura.tools.data.selection.DataBlock;
import com.asura.tools.data.selection.DataBlocks;
import com.asura.tools.data.selection.IFeaturable;
import com.asura.tools.data.selection.SelectMethod;
import com.asura.tools.debug.DebugPrinter;
import com.asura.tools.util.CombinationUtil;
import com.asura.tools.util.FileUtil;
import com.asura.tools.util.StringUtil;
import com.asura.tools.util.collection.Accumulator;
import com.asura.tools.util.math.NumberUtil;
import com.asura.tools.word.CommonSpliter;

public class WordGroup {
	private HashMap<String, SingleWord> group;

	public WordGroup() {
		this.group = new LinkedHashMap();
	}

	public void addSingleWord(SingleWord sw) {
		for (WordPosition wp : sw.getPositions())
			addWordPostion(wp);
	}

	public void removeWord(String word) {
		this.group.remove(word);
	}

	public void appendGroup(WordGroup group) {
		for (SingleWord sw : group.group.values())
			addSingleWord(sw);
	}

	public SingleWord get(String word) {
		return ((SingleWord) this.group.get(word));
	}

	public void addWordPostion(WordPosition wp) {
		if (!(this.group.containsKey(wp.getWord()))) {
			this.group.put(wp.getWord(), new SingleWord(wp.getWord()));
		}

		((SingleWord) this.group.get(wp.getWord())).addPosition(wp);
	}

	public void removeConflict(String exp, int seq) {
		DebugPrinter printer = DebugPrinter.getInstance("dic");

		long s = System.currentTimeMillis();

		int count = 0;
		int changeCount = 0;
		System.out.println("round " + seq);
		sort(exp);
		List<SingleWord> list = new ArrayList();
		list.addAll(this.group.values());
		for (SingleWord sw : list) {
			sw.setModified(false);
		}

		Map<Character, List<Integer>>  cMap = getCharacterMap(list);
		for (int i = 1; i < list.size(); ++i) {
			SingleWord current = (SingleWord) list.get(i);
			HashSet done = new HashSet();
			for (char c : current.getWord().toCharArray()) {
				for (Integer j : cMap.get(Character.valueOf(c))) {
					if (j.intValue() >= i) {
						break;
					}
					if (!(done.contains(j))) {
						SingleWord sw = (SingleWord) list.get(j.intValue());
						if (!(sw.isModified())) {
							int oldCount = current.getFrequency();
							done.add(j);
							int re = current.removeConflict((SingleWord) list.get(j.intValue()));
							if (re > 0) {
								++changeCount;
								current.setModified(true);
								if (count++ < 100) {
									printer.print(current.getWord() + " is removed by "
											+ ((SingleWord) list.get(j.intValue())).getWord() + ", big "
											+ sw.getFrequency() + " small " + oldCount + " conflict " + re);
								}
							}
						}
					}
				}
			}

			if (i % 10000 == 0) {
				System.out.println(seq + " conflict " + i + " of " + list.size() + " time:"
						+ ((System.currentTimeMillis() - s) / 1000L));
				s = System.currentTimeMillis();
			}
		}

		System.out.println("change " + changeCount + " words");

		if (changeCount > 0)
			removeConflict(exp, seq + 1);
	}

	private Map<Character, List<Integer>> getCharacterMap(List<SingleWord> words) {
		Map map = new HashMap();
		for (int i = 0; i < words.size(); ++i) {
			char[] cs = ((SingleWord) words.get(i)).getWord().toCharArray();
			for (char c : cs) {
				if (!(map.containsKey(Character.valueOf(c)))) {
					map.put(Character.valueOf(c), new ArrayList());
				}
				((List) map.get(Character.valueOf(c))).add(Integer.valueOf(i));
			}
		}

		return map;
	}

	private boolean hasSameWord(String s1, String s2) {
		char[] cs1 = s1.toCharArray();
		char[] cs2 = s2.toCharArray();
		for (int i = 0; i < cs1.length; ++i) {
			for (int j = 0; j < cs2.length; ++j) {
				if (cs1[i] == cs2[j]) {
					return true;
				}
			}
		}

		return false;
	}

	public void removeStopwords(String[] stopwords) {
		List<String> removes = new ArrayList();
		for (String key : this.group.keySet()) {
			String temp = key;
			for (String st : stopwords) {
				if (temp.startsWith(st)) {
					temp = temp.replaceFirst(temp, st);
					if ((temp.length() == 1) || (this.group.containsKey(temp))) {
						removes.add(key);
						break;
					}
				}

				if (temp.endsWith(st)) {
					temp = temp.replaceFirst(temp, st);
					if ((temp.length() == 1) || (this.group.containsKey(temp))) {
						removes.add(key);
					}
				}
			}
		}

		System.out.println("remove stop words:" + removes);
		for (String re : removes)
			this.group.remove(re);
	}

	public int count() {
		return this.group.size();
	}

	public int emptyCount() {
		int i = 0;
		for (SingleWord sw : this.group.values()) {
			if (sw.getPositions().size() == 0) {
				++i;
			}
		}
		return i;
	}

	public void addWordPostion(WordPosition[] wps) {
		for (WordPosition wp : wps)
			addWordPostion(wp);
	}

	public void sort(String exp) {
		DataBlock bl = getBlock();
		DataBlocks bls = new SelectMethod(exp).select(bl);
		HashMap newGroup = new LinkedHashMap();
		for (IFeaturable fe : bls.getAllDatas()) {
			SingleWord sw = ((WordFeature) fe).getWord();
			newGroup.put(sw.getWord(), sw);
		}

		this.group = newGroup;
	}

	public WordGroup[] getPermutation() {
		List result = new ArrayList();

		List list = new ArrayList();
		list.addAll(this.group.values());
		List<List<?>> llist = CombinationUtil.getPermutation(list);
		for (List l : llist) {
			WordGroup wg = new WordGroup();
			for (Iterator localIterator2 = l.iterator(); localIterator2.hasNext();) {
				Object o = localIterator2.next();
				wg.addSingleWord((SingleWord) o);
			}
			result.add(wg);
		}

		return ((WordGroup[]) result.toArray(new WordGroup[0]));
	}

	public DataBlock getBlock() {
		DataBlock bl = new DataBlock();
		for (SingleWord sw : this.group.values()) {
			bl.addData(new WordFeature(sw));
		}

		return bl;
	}

	public void computeParents() {
		for (SingleWord sw1 : this.group.values())
			for (SingleWord sw2 : this.group.values())
				if (sw1 != sw2)
					sw1.addParent(sw2);
	}

	public Collection<SingleWord> getWords(int min) {
		List list = new ArrayList();
		for (SingleWord sw : this.group.values()) {
			if (sw.getPositions().size() >= min) {
				list.add(sw);
			}
		}

		return list;
	}

	public Collection<String> getWordStrings(int min) {
		List list = new ArrayList();
		for (SingleWord sw : this.group.values()) {
			if (sw.getPositions().size() >= min) {
				list.add(sw.toString());
			}
		}

		return list;
	}

	public Collection<SingleWord> getWords() {
		return this.group.values();
	}

	public static WordGroup fromFime(String file) {
		WordGroup wg = new WordGroup();
		String[] lines = FileUtil.getContentByLine(file, "utf8");
		int l = 0;
		for (String line : lines) {
			if (l++ % 10000 == 0) {
				System.out.println("line " + l + " of " + lines.length);
			}
			if (line.contains(",")) {
				String s1 = line.substring(0, line.indexOf(","));
				String s2 = line.substring(line.indexOf(",") + 1);

				if ((StringUtil.isNullOrEmpty(s2)) || (!(StringUtil.containsChinese(s2)))
						|| (!(StringUtil.isNumberString(s1))))
					continue;
				s2 = s2.trim();
				WordSource ws = new WordSource(s2, NumberUtil.getInt(s1));

				for (int i = 1; i < 5; ++i) {
					Accumulator acc = new Accumulator();
					String[] ss = CommonSpliter.split(s2, i, true);
					for (int j = 0; j < ss.length; ++j) {
						if (StringUtil.isAllChineseCharacter(ss[j])) {
							int index = 0;
							int c = acc.getCount(ss[j]);
							index = s2.indexOf(ss[j], c);
							acc.addKey(ss[j], c + 1);
							WordPosition wp = new WordPosition(ss[j], ws, index, index + ss[j].length());
							wg.addWordPostion(wp);
						}
					}
				}

			}

		}

		return wg;
	}

	public String toString() {
		List list = new ArrayList();
		List<SingleWord> sws = new ArrayList();
		sws.addAll(this.group.values());
		Collections.sort(sws);
		for (SingleWord sw : sws) {
			list.add(" " + sw.toString() + " h:" + sw.getHeaders().getDistribution() + "  t:"
					+ sw.getTails().getDistribution());
		}

		return StringUtil.getStringFromStrings(list, "\n");
	}
}
