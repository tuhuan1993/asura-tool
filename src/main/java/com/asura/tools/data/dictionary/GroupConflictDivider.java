package com.asura.tools.data.dictionary;

import java.util.ArrayList;
import java.util.List;

import com.asura.tools.util.Combination;
import com.asura.tools.util.collection.RecurseSameSet;

public class GroupConflictDivider implements IGroupDivider {
	public WordGroup[] divide(WordGroup group) {
		RecurseSameSet<SingleWord> rss = new RecurseSameSet();

		List<List<?>> llist = new Combination().getCombination((SingleWord[]) group.getWords().toArray(new SingleWord[0]), 2);
		for (List list : llist) {
			if (isConflict((SingleWord) list.get(0), (SingleWord) list.get(1))) {
				rss.addSame((SingleWord) list.get(0), (SingleWord) list.get(1));
			}
		}

		List list = new ArrayList();
		for (SingleWord sw : rss.getUniques()) {
			WordGroup wg = new WordGroup();
			wg.addSingleWord(sw);
			for (SingleWord same : rss.getSame(sw)) {
				wg.addSingleWord(same);
			}

			list.add(wg);
		}

		for (SingleWord sw : group.getWords()) {
			if (!(rss.contains(sw))) {
				WordGroup wg = new WordGroup();
				wg.addSingleWord(sw);
				list.add(wg);
			}
		}

		return ((WordGroup[]) list.toArray(new WordGroup[0]));
	}

	private void buildRss(RecurseSameSet<SingleWord> rss, WordGroup group) {
		for (SingleWord sw : group.getWords()) {
			boolean done = false;
			for (SingleWord uni : rss.getUniques()) {
				if (isConflict(sw, uni)) {
					rss.addSame(sw, uni);
					done = true;
					break;
				}
			}

			if (!(done))
				for (SingleWord word : group.getWords()) {
					if ((rss.contains(word)) || (!(isConflict(word, sw))))
						continue;
					rss.addSame(word, sw);
					break;
				}
		}
	}

	private boolean isConflict(SingleWord sw1, SingleWord sw2) {
		SingleWord clone = sw1.clone();
		clone.removeConflict(sw2);

		return (clone.getPositions().size() != sw1.getPositions().size());
	}
}