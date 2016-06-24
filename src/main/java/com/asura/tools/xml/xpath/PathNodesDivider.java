package com.asura.tools.xml.xpath;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PathNodesDivider {
	public static PathNodes[] divider(PathNodes nodes) {
		List list = new ArrayList();
		HashMap groupMap = new HashMap();
		HashMap splitedMap = new HashMap();
		for (IPathNode node : nodes.getPathNodes()) {
			if (node instanceof GroupElementPathNode) {
				handleGroupNode((GroupElementPathNode) node, groupMap);
			} else if (node instanceof SplitedGroupPathNode) {
				handleSplitedNode((SplitedGroupPathNode) node, splitedMap);
			} else {
				PathNodes ns = new PathNodes();
				ns.addPathNode(node);
				list.add(ns);
			}
		}

		return ((PathNodes[]) list.toArray(new PathNodes[0]));
	}

	private static void handleGroupNode(GroupElementPathNode node, HashMap<String, PathNodes> groupMap) {
		String key = node.getGroup().toPathString();
		if (groupMap.containsKey(key)) {
			groupMap.put(key, new PathNodes());
		}

		((PathNodes) groupMap.get(key)).addPathNode(node);
	}

	private static void handleSplitedNode(SplitedGroupPathNode node, HashMap<String, PathNodes> splitedMap) {
		String key = node.getGroup().toPathString() + ":" + node.getInnerGroup().toPathString();
		if (splitedMap.containsKey(key)) {
			splitedMap.put(key, new PathNodes());
		}

		((PathNodes) splitedMap.get(key)).addPathNode(node);
	}
}
