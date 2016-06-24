package com.asura.tools.word;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import com.asura.tools.util.StringUtil;

public class WordSpliter {
	private HashSet<String> wordSet;
	private static HashMap<String[], WordSpliter> map = new HashMap();

	private WordSpliter(String[] words) {
		this.wordSet = new HashSet();
		for (String word : words)
			this.wordSet.add(word);
	}

	public static synchronized WordSpliter getInstance(String[] words) {
		if (!(map.containsKey(words))) {
			map.put(words, new WordSpliter(words));
		}

		return ((WordSpliter) map.get(words));
	}

	public String[] splitWithoutPriority(String string) {
		return split(string, new SameWordsScorer());
	}

	public String[] split(String string, IWordsScorer scorer) {
		String[] words = CommonSpliter.getSeparatedString(string);
		List result = new ArrayList();
		for (String word : words) {
			int i = 0;
			while (i * 30 <= word.length()) {
				List llist = getAllPosibility(word.substring(i * 30, Math.min(30 * (i + 1), word.length())));
				result.addAll(getResult(llist, scorer));

				++i;
			}

		}

		return ((String[]) result.toArray(new String[0]));
	}

	private List<String> getResult(List<List<String>> llist, IWordsScorer scorer) {
		List result = new ArrayList();

		if (llist.size() == 1) {
			result = (List) llist.get(0);
		} else {
			double max = -1.0D;
			for (List list : llist) {
				double score = scorer.score(list);
				if (score > max) {
					max = score;
					result = list;
				}
			}
		}

		return result;
	}

	public List<List<String>> getAllPosibility(String string) {
		HashSet<String> words = getMatchWords(string);
		List posList = new ArrayList();
		int i;
		for (String key : words) {
			int[] ids = StringUtil.getAllIndex(string, key);
			for (i = 0; i < ids.length; ++i) {
				WordPosition pos = new WordPosition(ids[i] + 1, ids[i] + key.length());
				pos.setWord(key);
				pos.setSource(new InformationSource(string));
				posList.add(pos);
			}
		}

		Collections.sort(posList, new WordPositionComparator());

		List<List<WordPosition>> slist = getAllPosibility(posList);

		Object results = new ArrayList();
		for (List list : slist) {
			List result = splitWord(list, string, words);
			((List) results).add(result);
		}

		return ((List<List<String>>) results);
	}

	private List<String> splitWord(List<WordPosition> list, String string, HashSet<String> set) {
		List result = new ArrayList();
		int last = 0;
		for (int i = 0; i < list.size(); ++i) {
			WordPosition wp = (WordPosition) list.get(i);
			if (wp.getStart() - 1 > last) {
				result.addAll(getString(string.substring(last, wp.getStart() - 1), set));
			}
			String value = string.substring(((WordPosition) list.get(i)).getStart() - 1,
					((WordPosition) list.get(i)).getEnd());
			if (!(StringUtil.isNullOrEmpty(value))) {
				result.addAll(getString(value, set));
			}
			last = wp.getEnd();
		}

		if (last < string.length()) {
			result.addAll(getString(string.substring(last), set));
		}

		return result;
	}

	private List<String> getString(String s, HashSet<String> set) {
		List result = new ArrayList();
		if (!(set.contains(s))) {
			String[] words = CommonSpliter.getChineseEnglishSeparatedString(s);
			for (String word : words)
				if (StringUtil.isAllChineseCharacter(word)) {
					for (char c : word.toCharArray())
						result.add(String.valueOf(c));
				} else
					result.add(word);
		} else {
			result.add(s);
		}

		return result;
	}

	private List<List<WordPosition>> getAllPosibility(List<WordPosition> posList) {
		List llist = new ArrayList();
		for (WordPosition pos : posList) {
			boolean added = false;

			for (int i = 0; i < llist.size(); ++i) {
				List<WordPosition> list = (List) llist.get(i);
				if ((list.size() <= 0) || (pos.isInclusive((WordPosition) list.get(list.size() - 1))))
					continue;
				List newlist = new ArrayList();
				for (WordPosition pp : list) {
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

		for (int i = llist.size() - 1; i > 0; --i) {
			for (int j = i - 1; j >= 0; --j) {
				if (isContains((List) llist.get(j), (List) llist.get(i))) {
					llist.remove(j);
					--i;
				}
			}
		}

		return llist;
	}

	private boolean isContains(List<WordPosition> shortList, List<WordPosition> longList) {
		HashSet ssset = new HashSet();
		HashSet seset = new HashSet();
		HashSet svalueSet = new HashSet();
		for (WordPosition wp : shortList) {
			ssset.add(Integer.valueOf(wp.getStart()));
			seset.add(Integer.valueOf(wp.getEnd()));
			svalueSet.add(wp.getWord());
		}

		HashSet lsset = new HashSet();
		HashSet leset = new HashSet();
		HashSet lvalueSet = new HashSet();
		for (WordPosition wp : longList) {
			lsset.add(Integer.valueOf(wp.getStart()));
			leset.add(Integer.valueOf(wp.getEnd()));
			lvalueSet.add(wp.getWord());
		}

		return ((lsset.containsAll(ssset)) && (leset.containsAll(seset)) && (lvalueSet.containsAll(svalueSet)));
	}

	private HashSet<String> getMatchWords(String sentence) {
		HashSet table = new HashSet();
		for (int i = 1; i < 5; ++i) {
			String[] words = Spliter.split(sentence, i);
			for (int j = 0; j < words.length; ++j) {
				if ((!(this.wordSet.contains(words[j]))) || (words[j].length() <= 1))
					continue;
				table.add(words[j]);
			}

		}

		return table;
	}
}
