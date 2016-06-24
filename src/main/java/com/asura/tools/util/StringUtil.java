package com.asura.tools.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import com.asura.tools.cjf.CJFBeanFactory;
import com.asura.tools.cjf.ChineseJF;
import com.asura.tools.string.IStringCondition;
import com.asura.tools.util.cache.SimpleCache;

public class StringUtil {
	public static final String SPLITER_RECORD = "원";
	public static final String SPLITER_FIELD = "빈";
	public static final String UNIQUE_STRING = "어";
	private static final Pattern IP_PATTERN = Pattern.compile("\\d+\\.\\d+\\.\\d+\\.\\d+");

	private static SimpleCache<String, String> standardCache = new SimpleCache(1000000);
	private static Pattern chinesePattern = Pattern.compile("[一-龥]");

	public static String getStandardString(String string) {
		if (isNullOrEmpty(string)) {
			return "";
		}

		if (!(standardCache.iscached(string))) {
			standardCache.cache(string, chineseFJChange(qBchange(string.toLowerCase())).trim());
		}

		return ((String) standardCache.get(string));
	}

	public static String unicode10ToWord(String unicode) {
		try {
			if (unicode.contains("&#")) {
				String[] ss = unicode.split("&#");
				String newString = "";
				String[] arrayOfString1;
				int j = (arrayOfString1 = ss).length;
				int i = 0;
				if (j == 0) {
					return newString;
				}
				while (true) {
					String s = arrayOfString1[i];
					if ((containsNumber(s)) && (s.contains(";"))) {
						int index = s.indexOf(";");
						String number = s.substring(0, index);
						String s1 = "";
						int a = Integer.parseInt(number, 10);
						s1 = s1 + (char) a;
						newString = newString + s1 + s.substring(index + 1);
					} else {
						newString = newString + s;
					}
					++i;
					if (i >= j) {
						return newString;
					}
				}
			}
			return unicode;
		} catch (Exception e) {
		}
		return unicode;
	}

	public static boolean standardEquals(String s1, String s2) {
		return getStandardString(s1).equals(getStandardString(s2));
	}

	public static boolean containsNumber(String source) {
		for (char i = '0'; i < ':'; i = (char) (i + '\1')) {
			if (source.contains(String.valueOf(i))) {
				return true;
			}
		}
		return false;
	}

	public static String getPascalString(String string) {
		return string.substring(0, 1).toUpperCase() + string.substring(1);
	}

	public static String[] splitRemainRex(String string, String[] rexs) {
		List list = new ArrayList();
		HashSet set = new HashSet();
		for (String rex : rexs) {
			set.add(rex);
		}
		list.add(string);
		for (int i = 0; i < rexs.length; ++i) {
			Object newList = new ArrayList();
			for (Iterator it = list.iterator(); it.hasNext();) {
				String s = (String) it.next();
				if (set.contains(s))
					((List) newList).add(s);
				else {
					((List) newList).addAll(Arrays.asList(splitRemainRex(s, rexs[i])));
				}
			}

			list = (List) newList;
		}

		return ((String[]) list.toArray(new String[0]));
	}

	public static String[] splitRemainRex(String source, String rex) {
		String[] ss = split(source, rex);

		List list = new ArrayList();
		for (int i = 0; i < ss.length; ++i) {
			if (ss[i].length() > 0) {
				list.add(ss[i].trim());
			}
			if (i < ss.length - 1) {
				list.add(rex);
			}
		}

		return ((String[]) list.toArray(new String[0]));
	}

	public static String[] split(String string, String rex) {
		int[] indexs = getAllIndex(string, rex);

		List list = new ArrayList();
		for (int i = 0; i < indexs.length; ++i) {
			if (i == 0) {
				list.add(string.substring(0, indexs[i]));
			}

			if (i < indexs.length - 1)
				list.add(string.substring(indexs[i] + rex.length(), indexs[(i + 1)]));
			else if (i == indexs.length - 1) {
				list.add(string.substring(indexs[i] + rex.length()));
			}
		}

		if (indexs.length == 0) {
			list.add(string);
		}

		return ((String[]) list.toArray(new String[0]));
	}

	public static String[] splitWithoutBlank(String string, String rex) {
		int[] indexs = getAllIndex(string, rex);

		List list = new ArrayList();
		for (int i = 0; i < indexs.length; ++i) {
			if ((i == 0) && (!(isNullOrEmpty(string.substring(0, indexs[i]))))) {
				list.add(string.substring(0, indexs[i]));
			}

			if (i < indexs.length - 1) {
				if (!(isNullOrEmpty(string.substring(indexs[i] + rex.length(), indexs[(i + 1)]))))
					list.add(string.substring(indexs[i] + rex.length(), indexs[(i + 1)]));
			} else {
				if ((i != indexs.length - 1) || (isNullOrEmpty(string.substring(indexs[i] + rex.length()))))
					continue;
				list.add(string.substring(indexs[i] + rex.length()));
			}

		}

		if ((indexs.length == 0) && (!(isNullOrEmpty(string)))) {
			list.add(string);
		}

		return ((String[]) list.toArray(new String[0]));
	}

	public static String[] split(String string, String[] rexs) {
		List<String> list = new ArrayList<String>();

		list.add(string);
		for (int i = 0; i < rexs.length; ++i) {
			List newList = new ArrayList();
			for (String s : list) {
				newList.addAll(Arrays.asList(split(s, rexs[i])));
			}

			list = newList;
		}

		return ((String[]) list.toArray(new String[0]));
	}

	public static boolean isChineseCharacter(char ch) {
		String string = String.valueOf(ch);
		return chinesePattern.matcher(string).find();
	}

	public static String getStringFromPascal(String string) {
		return string.substring(0, 1).toLowerCase() + string.substring(1);
	}

	public static String getStringFromStrings(String[] strings) {
		StringBuffer buf = new StringBuffer();
		for (String string : strings) {
			buf.append(string);
			buf.append(" ");
		}

		return buf.toString().trim();
	}

	public static String getStringFromStringsWithUnique(String[] strings) {
		StringBuffer buf = new StringBuffer();
		if (strings.length > 0) {
			for (int i = 0; i < strings.length - 1; ++i) {
				buf.append(strings[i]);
				buf.append("어");
			}
			buf.append(strings[(strings.length - 1)]);
		}

		return buf.toString().trim();
	}

	public static String getStringFromStrings(List<String> list, String spliter) {
		return getStringFromStrings((String[]) list.toArray(new String[0]), spliter);
	}

	public static String getStringFromStrings(String[] strings, String spliter) {
		if ((strings == null) || (strings.length == 0)) {
			return "";
		}
		if (spliter == null) {
			spliter = "";
		}
		StringBuffer buf = new StringBuffer();
		for (String string : strings) {
			buf.append(string);
			buf.append(spliter);
		}

		return buf.toString().substring(0, buf.toString().length() - spliter.length());
	}

	public static String[] getStringsFromString(String stirng, String spliter) {
		return stirng.split(spliter);
	}

	public static boolean isNullOrEmpty(String string) {
		return ((string == null) || (string.trim().length() == 0));
	}

	public static String removeWhiteSpace(String string) {
		if (isNullOrEmpty(string)) {
			return "";
		}
		string = string.replace(" ", "");
		string = string.replace("\t", "");

		return string;
	}

	public static boolean isCharOrNumberString(String string) {
		char[] cs = string.toCharArray();
		for (char c : cs) {
			if ((!(Character.isDigit(c))) && (!(isEnglishCharacter(c)))) {
				return false;
			}
		}

		return true;
	}

	public static boolean isNumberString(String string) {
		char[] cs = string.toCharArray();
		for (char c : cs) {
			if ((!(Character.isDigit(c))) && (c != '.')) {
				return false;
			}
		}

		return true;
	}

	public static boolean isIpAddress(String str) {
		Matcher matcher = IP_PATTERN.matcher(str);

		return (matcher.matches());
	}

	public static boolean isEnglishCharacter(char ch) {
		String a = String.valueOf(ch).toLowerCase();
		return ((a.charAt(0) >= 'a') && (a.charAt(0) <= 'z'));
	}

	public static boolean isEnglishString(String string) {
		string = getStandardString(string);
		char[] cs = string.toCharArray();
		for (char c : cs) {
			if ((c < 'a') || (c > 'z')) {
				return false;
			}
		}

		return true;
	}

	public static boolean isEnglishOrNumberCharacter(char ch) {
		return ((isEnglishCharacter(ch)) || (Character.isDigit(ch)));
	}

	public static boolean isNumberCharacter(char ch) {
		return Character.isDigit(ch);
	}

	public static boolean containsChinese(String word) {
		if (!(isNullOrEmpty(word))) {
			int i = 0;
			while (true) {
				if (isChineseCharacter(word.charAt(i))) {
					return true;
				}
				++i;
				if (i >= word.length()) {
					return false;
				}
			}
		}
		return false;
	}

	public static String removeParenthesis(String source) {
		return removeParenthesis(source, new String[] { "(" }, new String[] { ")" });
	}

	public static String removeParenthesis(String source, String[] starts, String[] ends) {
		for (int i = 0; i < starts.length; ++i) {
			source = source.replace(starts[i], "(");
			source = source.replace(ends[i], ")");
		}
		int[] si = getAllIndex(source, "(");
		int[] ei = getAllIndex(source, ")");
		String ns = "";
		if (si.length > 0) {
			int es = 0;
			boolean find = false;
			for (int e : ei) {
				if (e > si[0]) {
					find = true;
					break;
				}
				++es;
			}

			int lastposition = 0;

			if (find) {
				for (int j1 = 0; (j1 < si.length) && (es < ei.length); ++j1) {
					if (si[j1] >= lastposition) {
						ns = ns + " " + source.substring(lastposition, si[j1]);
						lastposition = ei[es] + 1;
						++es;
					}
				}
			}

			ns = ns + " " + source.substring(lastposition);
			return ns.trim();
		}

		return source;
	}

	public static boolean containsEnglishOrNumber(String word) {
		if (!(isNullOrEmpty(word))) {
			int i = 0;
			while (true) {
				if (isEnglishOrNumberCharacter(word.charAt(i))) {
					return true;
				}
				++i;
				if (i >= word.length()) {
					return false;
				}
			}
		}
		return false;
	}

	public static boolean containsEnglish(String word) {
		if (!(isNullOrEmpty(word))) {
			int i = 0;
			while (true) {
				if (isEnglishCharacter(word.charAt(i)))
					return true;
				++i;
				if (i >= word.length()) {
					return false;
				}
			}
		}
		return false;
	}

	public static boolean isAllChineseCharacter(String word) {
		if (!(isNullOrEmpty(word))) {
			int i = 0;
			while (true) {
				if (!(isChineseCharacter(word.charAt(i))))
					return false;
				++i;
				if (i >= word.length()) {
					return true;
				}
			}
		}
		return false;
	}

	public static int[] getAllInDependentIndex(String source, String rex) {
		ArrayList list = new ArrayList();
		if (isCharOrNumberString(rex)) {
			int position = 0;
			while (position < source.length()) {
				int index = source.indexOf(rex, position);
				if (index <= -1)
					break;
				if ((index > 0) && (index + rex.length() < source.length())) {
					if ((!(isEnglishOrNumberCharacter(source.charAt(index - 1))))
							&& (!(isEnglishOrNumberCharacter(source.charAt(index + rex.length())))))
						list.add(Integer.valueOf(source.indexOf(rex, position)));
				} else if (index > 0) {
					if (!(isEnglishOrNumberCharacter(source.charAt(index - 1))))
						list.add(Integer.valueOf(source.indexOf(rex, position)));
				} else if (index + rex.length() < source.length()) {
					list.add(Integer.valueOf(source.indexOf(rex, position)));
				} else if (index + rex.length() == source.length()) {
					list.add(Integer.valueOf(source.indexOf(rex, position)));
				}
				position = index + 1;
			}

		} else {
			return getAllIndex(source, rex);
		}

		int[] ins = new int[list.size()];
		for (int i = 0; i < ins.length; ++i) {
			ins[i] = ((Integer) list.get(i)).intValue();
		}

		return ins;
	}

	public static int[] getAllIndex(String source, String rex) {
		ArrayList list = new ArrayList();
		int position = 0;
		while (position < source.length()) {
			int index = source.indexOf(rex, position);
			if (index <= -1)
				break;
			list.add(Integer.valueOf(source.indexOf(rex, position)));
			position = index + 1;
		}

		int[] ins = new int[list.size()];
		for (int i = 0; i < ins.length; ++i) {
			ins[i] = ((Integer) list.get(i)).intValue();
		}

		return ins;
	}

	public static int[] getAllIndex(String source, String[] rexs) {
		ArrayList list = new ArrayList();
		for (String s : rexs) {
			int[] indexs = getAllIndex(source, s);
			for (int index : indexs) {
				list.add(Integer.valueOf(index));
			}
		}

		int[] ins = new int[list.size()];
		for (int j = 0; j < ins.length; ++j) {
			ins[j] = ((Integer) list.get(j)).intValue();
		}

		return ins;
	}

	public static String reverseString(String string) {
		if (isNullOrEmpty(string)) {
			return "";
		}
		StringBuffer sb = new StringBuffer();
		for (int i = 1; i <= string.length(); ++i) {
			sb.append(string.charAt(string.length() - i));
		}

		return sb.toString();
	}

	public static String getNotNullValue(String string) {
		if (string == null) {
			string = "";
		}

		return string;
	}

	public static String qBchange(String QJstr) {
		if (isNullOrEmpty(QJstr)) {
			return "";
		}

		char[] c = QJstr.toCharArray();
		for (int i = 0; i < c.length; ++i) {
			if (c[i] == 12288) {
				c[i] = ' ';
			} else if ((c[i] > 65280) && (c[i] < 65375))
				c[i] = (char) (c[i] - 65248);
		}
		return new String(c);
	}

	public static String chineseFJChange(String fanString) {
		if (isNullOrEmpty(fanString)) {
			return "";
		}
		ChineseJF chinesdJF = CJFBeanFactory.getChineseJF();
		String janText = chinesdJF.chineseFan2Jan(fanString);
		return janText;
	}

	public static String removeSpecialChars(String str) {
		String regEx = "[`~!@#$%^&* ()_+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
		Matcher m = null;
		try {
			Pattern p = Pattern.compile(regEx);
			m = p.matcher(str);
		} catch (PatternSyntaxException p) {
			p.printStackTrace();
		}
		return m.replaceAll("").trim();
	}

	public static String getTimeString(String time, int length) {
		if (time.contains(".")) {
			int dl = time.length() - time.indexOf(".") - 1;
			if (dl > length) {
				time = time.substring(0, time.length() - dl + length);
			}
		}

		return time;
	}

	public static boolean parseBoolean(String bool) {
		try {
			return Boolean.parseBoolean(bool);
		} catch (Exception e) {
		}
		return false;
	}

	public static int getChineseLength(String string) {
		if (string == null) {
			return 0;
		}
		int length = 0;
		for (char c : string.toCharArray()) {
			if (isChineseCharacter(c)) {
				++length;
			}
		}

		return length;
	}

	public static int getEnglishLength(String string) {
		if (string == null) {
			return 0;
		}
		int length = 0;
		for (char c : string.toCharArray()) {
			if (isEnglishCharacter(c)) {
				++length;
			}
		}

		return length;
	}

	public static String getBlankString(int len) {
		String s = "";
		for (int i = 0; i < len; ++i) {
			s = s + " ";
		}

		return s;
	}

	public static String getOriginalStringFromStandardString(String originalString, String standardString) {
		standardString = standardString.trim();

		if (sameTypeCharacter(originalString, standardString)) {
			String ss = getStandardString(originalString);
			String[] ws = CommonSpliter.split(ss, standardString.length());

			for (String w : ws) {
				if (w.equals(standardString)) {
					return originalString.substring(ss.indexOf(w), ss.indexOf(w) + standardString.length());
				}

			}

			for (String w : ws) {
				if (pinyinEquals(w, standardString)) {
					return originalString.substring(ss.indexOf(w), ss.indexOf(w) + standardString.length());
				}
			}
		}

		return "";
	}

	public static boolean sameTypeCharacter(String a, String b) {
		if ((a == null) || (b == null)) {
			return false;
		}
		if ((containsChinese(a)) && (containsChinese(b))) {
			return true;
		}

		if ((containsEnglish(a)) && (containsEnglish(b))) {
			return true;
		}

		return ((containsNumber(a)) && (containsNumber(b)));
	}

	public static boolean pinyinEquals(String a, String b) {
		if ((a == null) || (b == null)) {
			return false;
		}
		a = getStandardString(a);
		b = getStandardString(b);
		return PinyinUtil.getPinyinString(a).equals(PinyinUtil.getPinyinString(b));
	}

	public static boolean stringEquals(String a, String b) {
		if ((a == null) && (b == null))
			return true;
		if ((a != null) && (b != null)) {
			return a.equals(b);
		}
		return false;
	}

	public static String replaceFirst(String string, String source, String target) {
		int[] ids = getAllIndex(string, source);
		if (ids.length > 0) {
			return string.substring(0, ids[0]) + target + string.substring(ids[0] + source.length());
		}

		return string;
	}

	public static String replaceOnce(String source, String rex) {
    if (isNullOrEmpty(rex)) {
      return source;
    }
    if (!(getStandardString(source).contains(getStandardString(rex)))) {
      return source;
    }

    boolean replaced = false;
    boolean loop = true;
    while (loop) {
      loop = false;
      int[] ids = getAllIndex(source, rex);
      for (int i : ids) {
        if ((!(isOneSideAbsoluteWord(source, rex.charAt(0), i - 1))) || 
          (!(isOneSideAbsoluteWord(source, rex.charAt(rex
          .length() - 1), i + rex.length())))) continue;
        source = source.substring(0, i) + 
          " " + 
          source.substring(i + rex.length(), source
          .length());
        replaced = true;
        loop = true;
        break;
      }

    }

    for (int i : getAllIndex(source, rex)) {
      if ((((isOneSideAbsoluteWord(source, rex.charAt(0), i - 1)) || 
        (isOneSideAbsoluteWord(source, rex
        .charAt(rex.length() - 1), i + rex.length())))) && 
        (!(replaced))) {
        return source.substring(0, i) + 
          " " + 
          source.substring(i + rex.length(), source
          .length());
      }

    }

    return source;
  }

	private static boolean isOneSideAbsoluteWord(String source, char rex, int i) {
		return ((i < 0) || (i >= source.length()) || ((!(isEnglishCharacter(rex))) && (!(isNumberCharacter(rex))))
				|| ((!(isEnglishCharacter(source.charAt(i)))) && (!(isNumberCharacter(source.charAt(i))))));
	}

	public static String getTableField(String objectField) {
		if (isNullOrEmpty(objectField)) {
			throw new RuntimeException("empty string is not allowed");
		}
		try {
			char[] chars = objectField.toCharArray();

			List ids = new ArrayList();
			for (int i = 0; i < chars.length; ++i) {
				if ((chars[i] >= 'A') && (chars[i] <= 'Z')) {
					ids.add(Integer.valueOf(i));
				}
			}

			if (ids.size() == 0) {
				return objectField;
			}
			StringBuffer sb = new StringBuffer();
			sb.append(objectField.substring(0, ((Integer) ids.get(0)).intValue()));
			for (int i = 0; i < ids.size() - 1; ++i) {
				sb.append("_" + objectField
						.substring(((Integer) ids.get(i)).intValue(), ((Integer) ids.get(i + 1)).intValue())
						.toLowerCase());
			}
			sb.append("_" + objectField.substring(((Integer) ids.get(ids.size() - 1)).intValue(), objectField.length())
					.toLowerCase());
			return sb.toString();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static String[] getSeparatedString(String s, boolean keepMark) {
		List list = new ArrayList();

		char[] cs = s.toCharArray();
		int lastType = -1;
		String currentString = "";
		for (int i = 0; i < cs.length; ++i) {
			if (i == 0) {
				currentString = String.valueOf(cs[i]);
				lastType = getType(cs[i]);
			} else {
				int type = getType(cs[i]);
				if (type == lastType) {
					currentString = currentString + cs[i];
				} else {
					if ((((lastType > 0) || (keepMark))) && (!(isNullOrEmpty(currentString)))) {
						list.add(currentString);
					}

					currentString = String.valueOf(cs[i]);
					lastType = type;
				}
			}

			if ((i != cs.length - 1) || ((lastType <= 0) && (!(keepMark))) || (isNullOrEmpty(currentString)))
				continue;
			list.add(currentString);
		}

		return ((String[]) list.toArray(new String[0]));
	}

	private static int getType(char c) {
		if (isEnglishCharacter(c))
			return 1;
		if ((isNumberCharacter(c)) || (c == '.'))
			return 2;
		if (isChineseCharacter(c)) {
			return 3;
		}

		return -1;
	}

	public static int getDependency(String source, String word, int start) {
		int dependency = 0;
		if (word.length() > 0) {
			if (start == 0) {
				++dependency;
			} else {
				String left = String.valueOf(source.charAt(start - 1));
				String left2 = source.substring(Math.min(0, start - 2), start);
				if (!(isSameType(word.substring(0, 1), left))) {
					if ((containsEnglishOrNumber(word)) && (left.equals("-")) && (containsEnglishOrNumber(left2)))
						dependency += 0;
					else {
						++dependency;
					}
				}
			}

			if (start + word.length() == source.length()) {
				++dependency;
			} else {
				String right = source.substring(start + word.length(), start + word.length() + 1);
				String right2 = source.substring(start + word.length(),
						Math.min(source.length(), start + word.length() + 2));
				if (!(isSameType(word.substring(word.length() - 1), right))) {
					if ((containsEnglishOrNumber(word)) && (right.equals("-")) && (containsEnglishOrNumber(right2)))
						dependency += 0;
					else {
						++dependency;
					}
				}
			}

			return dependency;
		}

		return -1;
	}

	private static boolean isSameType(String s1, String s2) {
		return (((isCharOrNumberString(s1)) && (isCharOrNumberString(s2)))
				|| ((isAllChineseCharacter(s1)) && (isAllChineseCharacter(s2))));
	}

	public static String getMeanfullString(String s) {
		if (isNullOrEmpty(s)) {
			return "";
		}
		List list = new ArrayList();
		for (char c : s.toCharArray()) {
			if ((isChineseCharacter(c)) || (isEnglishOrNumberCharacter(c))) {
				list.add(String.valueOf(c));
			}
		}

		return getStringFromStrings(list, "");
	}

	public static boolean isMeanfullString(String s) {
		for (char c : s.toCharArray()) {
			if ((isChineseCharacter(c)) || (isEnglishOrNumberCharacter(c))) {
				return true;
			}
		}

		return false;
	}

	public static String replace(String temple, HashMap<String, String> map) {
		HashMap indexMap = new HashMap();
		HashSet set = new HashSet();
		for (String key : map.keySet()) {
			int index = temple.indexOf(key);
			if (index >= 0) {
				indexMap.put(Integer.valueOf(index), key);
				for (int i = index; i < index + key.length(); ++i) {
					set.add(Integer.valueOf(i));
				}
			}
		}
		String s = "";
		for (int i = 0; i < temple.length(); ++i) {
			if (!(set.contains(Integer.valueOf(i)))) {
				s = s + temple.charAt(i);
			} else if (indexMap.containsKey(Integer.valueOf(i))) {
				s = s + ((String) map.get(indexMap.get(Integer.valueOf(i))));
			}

		}

		return s;
	}

	public static String replaceAll(String temple, HashMap<String, String> map) {
		String t = temple;
		for (String key : map.keySet()) {
			t = t.replace(key, (CharSequence) map.get(key));
		}

		return t;
	}

	public static String[] getSameTypeString(String source, IStringCondition condition) {
		String s = "";
		for (int i = 0; i < source.length(); ++i) {
			char c = source.charAt(i);
			if (condition.meet(String.valueOf(c)))
				s = s + c;
			else {
				s = s + " ";
			}
		}

		String[] ss = split(s, " ");
		List list = new ArrayList();
		for (String vs : ss) {
			if (!(isNullOrEmpty(vs))) {
				list.add(vs);
			}
		}

		return ((String[]) list.toArray(new String[0]));
	}

	public static boolean contains(String big, String small, String spliter) {
		String[] ss = big.split(spliter);

		for (String s : ss) {
			if (s.trim().toLowerCase().equals(small.trim().toLowerCase())) {
				return true;
			}
		}

		return false;
	}

	public static String[] split(String source, String front, String back) {
		int[] fs = getAllIndex(source, front);
		int[] bs = getAllIndex(source, back);

		List list = new ArrayList();
		for (int i = 0; i < fs.length; ++i) {
			int f1 = fs[i];
			for (int j = i + 1; j <= fs.length; ++j) {
				int end = tryNext(source, f1, i, fs, bs, list, j);
				if (end >= 0) {
					if (i == 0)
						list.add(0, source.substring(0, fs[i]));
					else if (end == bs.length - 1)
						list.add(source.substring(bs[end] + 1, source.length()));
					else if (end == bs.length) {
						list.add(source.substring(bs[(end - 1)] + 1, source.length()));
					}

					i = j - 1;

					break;
				}
			}
		}

		return ((String[]) list.toArray(new String[0]));
	}

	private static int tryNext(String source, int f1, int i, int[] fs, int[] bs, List<String> list, int end) {
		int f2 = source.length();
		if (end < fs.length) {
			f2 = fs[end];
		}
		for (int j = bs.length - 1; j >= 0; --j) {
			int b = bs[j];
			if ((f1 < b) && (f2 > b)) {
				list.add(source.substring(f1 + 1, b));

				return j;
			}
		}

		return -1;
	}

	public static String[] splitStrict(String source, String front, String back) {
		int[] fs = getAllIndex(source, front);
		int[] bs = getAllIndex(source, back);

		if (fs.length == bs.length) {
			for (int i = 0; i < bs.length; ++i) {
				if (fs[i] > bs[i]) {
					return new String[0];
				}
			}

			List list = new ArrayList();
			for (int i = 0; i < fs.length; ++i) {
				String head = "";
				if (i == 0)
					head = source.substring(0, fs[i]);
				else {
					head = source.substring(bs[(i - 1)] + 1, fs[i]);
				}
				if (isMeanfullString(head)) {
					list.add(head);
				}
				list.add(source.substring(fs[i] + 1, bs[i]));
			}

			if (bs[(bs.length - 1)] < source.length()) {
				list.add(source.substring(bs[(bs.length - 1)] + 1));
			}

			return ((String[]) list.toArray(new String[0]));
		}

		return new String[0];
	}
}
