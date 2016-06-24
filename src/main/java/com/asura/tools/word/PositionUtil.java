package com.asura.tools.word;

import java.util.HashMap;
import java.util.LinkedHashMap;

import com.asura.tools.util.RegularExpressionUtil;

public class PositionUtil {
	public static void computeStartEnd(WordPosition position, String source, String word, int index) {
		if ((source == null) || (source.length() == 0)) {
			position.setEnd(-1);
			position.setStart(-1);
		} else {
			position.setStart(index + 1);
			position.setEnd(position.getStart() + word.length() - 1);
		}
	}

	public static HashMap<Integer, WordPosition> computeSegment(String source) {
		HashMap map = new LinkedHashMap();
		int segment = 1;
		int sentence = 1;
		int phrase = 1;

		if (source != null) {
			for (int i = 0; i < source.length(); ++i) {
				WordPosition position = new WordPosition();
				String c = String.valueOf(source.charAt(i));

				if (RegularExpressionUtil.matches(c, RegularExpressionUtil.getScopedExpression("\r\n"))) {
					++segment;
					sentence = 1;
					phrase = 1;
				} else if (RegularExpressionUtil.matches(c, RegularExpressionUtil.getScopedExpression(".?!;。？！；:： "))) {
					++sentence;
					phrase = 1;
				} else if (RegularExpressionUtil.matches(c, RegularExpressionUtil.getScopedExpression("，,"))) {
					++phrase;
				}
				position.setPhrase(phrase);
				position.setSegment(segment);
				position.setSentence(sentence);
				map.put(Integer.valueOf(i), position);
			}
		}

		return map;
	}
}
