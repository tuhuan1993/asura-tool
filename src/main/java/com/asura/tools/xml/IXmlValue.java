package com.asura.tools.xml;

import com.asura.tools.xml.xpath.LeveledXmlSettings;

public interface IXmlValue {
	public static final String KEY_JOIN = "-";

	public void build(LeveledXmlSettings paramLeveledXmlSettings, XmlNode paramXmlNode, String paramString);
}
