package com.asura.tools.monitor;

import java.io.InputStream;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class MonitorXMLConverter {
	public static MonitorDefinition getMonitorDefinition(InputStream stream) throws ConfigurationParseException {
		try {
			return ((MonitorDefinition) getXStream().fromXML(stream));
		} catch (Exception e) {
			throw new ConfigurationParseException(e);
		}
	}

	private static XStream getXStream() {
		XStream xs = new XStream(new DomDriver());
		xs.alias("monitor-definition", MonitorDefinition.class);
		xs.aliasField("categories", MonitorDefinition.class, "categories");
		xs.aliasAttribute(MonitorDefinition.class, "monitorUrl", "monitor-url");
		xs.aliasAttribute(MonitorDefinition.class, "registerInterval", "register-interval");

		xs.alias("category", Category.class);
		xs.aliasField("category-nodes", Category.class, "catetegyList");
		xs.aliasField("result", Category.class, "result");
		xs.alias("monitor-info", MonitorInfo.class);
		xs.aliasAttribute(MonitorInfo.class, "host", "host");
		xs.aliasAttribute(MonitorInfo.class, "path", "path");
		xs.aliasAttribute(MonitorInfo.class, "key", "key");

		xs.alias("category-node", CategoryNode.class);
		xs.aliasAttribute(CategoryNode.class, "name", "name");
		xs.aliasAttribute(CategoryNode.class, "value", "node-value");

		return xs;
	}
}
