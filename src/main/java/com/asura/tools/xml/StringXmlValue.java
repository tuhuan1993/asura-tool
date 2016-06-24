package com.asura.tools.xml;

import com.asura.tools.xml.xpath.IPathNode;
import com.asura.tools.xml.xpath.LeveledXmlSettings;
import com.asura.tools.xml.xpath.PathParsers;

public class StringXmlValue implements IXmlValue {
	private String value;

	public StringXmlValue() {
	}

	public StringXmlValue(String value) {
		this.value = value;
	}

	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public void build(LeveledXmlSettings settings, XmlNode parent, String parentKey) {
		IPathNode node = settings.getPathNode(parentKey);
		PathParsers.getInstance().parse(node, this.value, parent);
	}
}