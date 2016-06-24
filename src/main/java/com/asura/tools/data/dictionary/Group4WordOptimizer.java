package com.asura.tools.data.dictionary;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.asura.tools.debug.DebugPrinter;

public class Group4WordOptimizer implements IGroupOptimizer {
	public void optimize(WordGroup group) {
		Set twoWords = new HashSet();

		group.sort("count:ns_1000000_1");

		List list = new ArrayList();
		list.addAll(group.getWords());

		for (int i = 0; i < list.size(); ++i) {
			SingleWord current = (SingleWord) list.get(i);
			if (current.getWord().length() == 2) {
				twoWords.add(current.getWord());
			}

			if (current.length() == 4) {
				String f = current.getWord().substring(0, 2);
				String b = current.getWord().substring(2);
				if ((twoWords.contains(f)) && (twoWords.contains(b))) {
					DebugPrinter.getInstance("dic")
							.print(current.getWord() + " is removed by " + super.getClass().getSimpleName());
					group.removeWord(current.getWord());
				}
			}
		}
	}
}