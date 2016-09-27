package com.asura.tools.util;

public class WordUtil {
	public static int getPositionDistance(String s1, String s2, String sentence) {
		int[] ids1 = StringUtil.getAllIndex(sentence, s1);
		int[] ids2 = StringUtil.getAllIndex(sentence, s2);
		if ((ids1.length > 0) && (ids2.length > 0)) {
			WordDistance wd = getMinDistanceAscenOrdered(ids1, ids2);
			wd.setS1(s1);
			wd.setS2(s2);
			wd.setSentence(sentence);

			return (wd.getDistance() - wd.getFrontWord().length());
		}
		return -1;
	}

	public static boolean isWithinDistance(String s1, String s2, String sentence, int distance) {
		return (getPositionDistance(s1, s2, sentence) <= distance);
	}

	private static WordDistance getMinDistanceAscenOrdered(int[] c1s, int[] c2s) {
		WordDistance wd = new WordDistance();
		int result = 2147483647;
		int p1 = 0;
		int p2 = 0;
		while ((p1 < c1s.length) && (p2 < c2s.length)) {
			if (c1s[p1] >= c2s[p2]) {
				int value = c1s[p1] - c2s[p2];
				if (value < result) {
					result = value;
					wd.setPosition1(c1s[p1]);
					wd.setPosition2(c2s[p2]);
					wd.setDistance(result);
				}
				++p2;
			} else {
				int value = c2s[p2] - c1s[p1];
				if (value < result) {
					result = value;
					wd.setPosition1(c1s[p1]);
					wd.setPosition2(c2s[p2]);
					wd.setDistance(result);
				}
				++p1;
			}
		}

		return wd;
	}
	
	public static boolean isOnlyEnglish(String word){
		StringBuilder sb = new StringBuilder();
		if(word != null){
			for(int i = 0;i< word.length();i++){
				char c = word.charAt(i);
				boolean isEnglishLetter = (c >='A'&& c <= 'Z') || ( c >= 'a' && c <= 'z');
				if(!isEnglishLetter){
					return false;
				}
			}
		}
		
		return true;
	}
}
