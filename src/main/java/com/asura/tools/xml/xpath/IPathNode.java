package com.asura.tools.xml.xpath;

public interface IPathNode {
	public IPathNode fromPathString(String paramString) throws XmlPathException;

	public String toPathString();

	public PathNodes[] divider(PathNodes paramPathNodes);
}
