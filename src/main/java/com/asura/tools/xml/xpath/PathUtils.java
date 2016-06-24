package com.asura.tools.xml.xpath;

import java.util.HashSet;

import com.asura.tools.util.StringUtil;

public class PathUtils {
	private static HashSet<Character> validCharacters = new HashSet();

	static {
		for (char c = 'a'; c <= 'z'; c = (char) (c + '\1')) {
			validCharacters.add(Character.valueOf(c));
		}

		for (char c = 'A'; c <= 'Z'; c = (char) (c + '\1')) {
			validCharacters.add(Character.valueOf(c));
		}

		validCharacters.add(Character.valueOf('-'));
		validCharacters.add(Character.valueOf('_'));

		for (char c = '0'; c <= '9'; c = (char) (c + '\1'))
			validCharacters.add(Character.valueOf(c));
	}

	private static void addCharacters(String string) {
		if (string != null)
			for (char c : string.toCharArray())
				validCharacters.add(Character.valueOf(c));
	}

	public static void checkPathName(String name) throws XmlPathException {
		if (!(StringUtil.isNullOrEmpty(name))) {
			char[] cs = name.toCharArray();
			for (char c : cs)
				if (!(validCharacters.contains(Character.valueOf(c))))
					throw new XmlPathException("path contains invalid character '" + c + "'");
		} else {
			throw new XmlPathException("path can not be empty.");
		}
	}
}
