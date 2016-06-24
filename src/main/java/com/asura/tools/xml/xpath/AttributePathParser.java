package com.asura.tools.xml.xpath;

import com.asura.tools.xml.XmlNode;

public class AttributePathParser implements IPathParser {
	public XmlNode parser(IPathNode pathNode, String value, XmlNode parent) {
		if (pathNode instanceof AttributePathNode) {
			AttributePathNode attr = (AttributePathNode) pathNode;
			parent.addAttribute(attr.getAttribute(), value);

			return parent;
		}

		return null;
	}
}
