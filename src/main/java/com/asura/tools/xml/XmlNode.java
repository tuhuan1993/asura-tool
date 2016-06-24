package com.asura.tools.xml;

import java.util.Iterator;

import org.jdom2.Attribute;
import org.jdom2.Element;

import com.asura.tools.util.StringUtil;

public class XmlNode implements IXmlNode {
	private Element element;

	public XmlNode(String name) {
		this.element = new Element(name);
	}

	public void setText(String text) {
		this.element.setText(text);
	}

	public void addChild(XmlNode node) {
		this.element.addContent(node.element);
	}

	public void addAttribute(String name, String value) {
		this.element.setAttribute(name, value);
	}

	public String toXml() {
		return getElementString(this.element);
	}

	private String getElementString(Element element) {
		String xml = "<";
		xml = xml + element.getName();

		for (Iterator localIterator = element.getAttributes().iterator(); localIterator.hasNext();) {
			Object content = localIterator.next();
			Attribute at = (Attribute) content;
			xml = xml + new AttributeXmlNode(at).toXml();
		}

		xml = xml + ">";

		if (!(StringUtil.isNullOrEmpty(element.getText()))) {
			xml = xml + "<![CDATA[" + element.getText() + "]]>";
		}

		for (Iterator localIterator = element.getChildren().iterator(); localIterator.hasNext();) {
			Object obj = localIterator.next();
			xml = xml + getElementString((Element) obj);
		}

		xml = xml + "</" + element.getName() + ">";

		return xml;
	}

}
