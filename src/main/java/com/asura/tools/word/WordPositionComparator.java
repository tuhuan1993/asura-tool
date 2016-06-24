package com.asura.tools.word;

import java.util.Comparator;

public class WordPositionComparator implements Comparator<WordPosition> {
	public int compare(WordPosition o1, WordPosition o2) {
		if (o1.getStart() > o2.getStart())
			return 1;
		if (o1.getStart() == o2.getStart()) {
			if (o1.getEnd() > o2.getEnd())
				return 1;
			if (o1.getEnd() == o2.getEnd()) {
				return 0;
			}
			return -1;
		}

		return -1;
	}
}