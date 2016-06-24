package com.asura.tools.xml.xpath;

import com.asura.tools.xml.XmlNode;

public interface IPathParser {
	public XmlNode parser(IPathNode paramIPathNode, String paramString, XmlNode paramXmlNode);
}
