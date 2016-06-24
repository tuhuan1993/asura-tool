package com.asura.tools.data.dictionary;

import com.asura.tools.batch.IBatchTask;

public class ConfictJob implements IBatchTask {
	private WordGroup latest;
	private WordGroup current;

	public ConfictJob(WordGroup latest, WordGroup wg) {
		this.latest = latest;
		this.current = wg;
	}

	public String getKey() {
		SingleWord sw = (SingleWord) this.current.getWords().iterator().next();
		return sw.getAllCount() + " " + sw.length();
	}

	public void process() {
		WordGroup[] newwgs = new GroupConflictDivider().divide(this.current);
		SingleWord sw = (SingleWord) this.current.getWords().iterator().next();
		System.out.println(sw.length() + " " + sw.getFrequency() + " conflict groups " + newwgs.length);
		for (WordGroup newwg : newwgs) {
			this.latest.appendGroup(newwg);
		}
	}
}
