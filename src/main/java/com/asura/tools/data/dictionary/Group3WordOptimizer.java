package com.asura.tools.data.dictionary;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Group3WordOptimizer implements IGroupOptimizer {
	public void optimize(WordGroup group) {
		Set twoWords = new HashSet();

		group.sort("count:ns_1000000_1");

		List list = new ArrayList();
		list.addAll(group.getWords());

		for (int i = 0; i < list.size(); ++i)
			;
	}
}