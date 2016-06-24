package com.asura.tools.string;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import com.asura.tools.util.CombinationUtil;
import com.asura.tools.util.CommonSpliter;
import com.asura.tools.util.StringUtil;

public class StringWordCondition implements IStringCondition {
	private static final long serialVersionUID = -1709326380126952262L;
	private String words;
	private static transient HashMap<String, HashSet<String>> wordMap = new HashMap();
	private boolean wholeWord;

	public boolean isWholeWord() {
		return this.wholeWord;
	}

	public void setWholeWord(boolean wholeWord) {
		this.wholeWord = wholeWord;
	}

	public boolean meet(String string) {
		initial(this.words);

		string = StringUtil.getStandardString(string);
		if (this.wholeWord) {
			String[] ss = StringUtil.split(string, "-");
			for (String s : ss)
				if (((HashSet) wordMap.get(this.words)).contains(s))
					return true;
		} else {
			for (int i = 1; i <= string.length(); ++i) {
				String[] ss = CommonSpliter.split(string, i);
				for (String s : ss) {
					if (((HashSet) wordMap.get(this.words)).contains(s)) {
						return true;
					}
				}
			}
		}

		return false;
	}

	private static void initial(String word) {
		if (!(wordMap.containsKey(word))) {
			wordMap.put(word, new HashSet());
			String[] ss = word.split(",");
			for (String s : ss) {
				((HashSet) wordMap.get(word)).add(StringUtil.getStandardString(s));
			}

			List slist = new ArrayList();
			for (String s : ss) {
				slist.add(StringUtil.getStandardString(s));
			}
			List<List> llist = CombinationUtil.getCombination(slist, 2);
			for (List list : llist)
				if (list.size() == 2) {
					String s1 = (String) list.get(0);
					String s2 = (String) list.get(1);

					((HashSet) wordMap.get(word)).add(s1 + s2);
					((HashSet) wordMap.get(word)).add(s2 + s1);
				}
		}
	}

	public String getWords() {
		return this.words;
	}

	public void setWords(String words) {
		this.words = words;
	}
}
