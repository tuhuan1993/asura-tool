package com.asura.tools.xml.xpath;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.asura.tools.xml.XmlNode;

public class SplitedMultiPathParser implements IMultiPathParser {
	public void parse(PathValuePair[] pairs, XmlNode parent)
	  {
	    if ((pairs == null) || (pairs.length <= 0) || 
	      (!(pairs[0].getPathNode() instanceof SplitedGroupPathNode))) return;
	    SplitedGroupPathNode node = (SplitedGroupPathNode)pairs[0]
	      .getPathNode();
	    XmlNode group = PathParsers.getInstance().parse(
	      node.getGroup(), null, parent);

	    HashMap map = new HashMap();
	    int minSize = 1000000;
	    for (PathValuePair pair : pairs) {
	      List values = new ArrayList();
	      if (pair.getValue() != null) {
	        values.addAll(Arrays.asList(pair.getValue().split(
	          node.getSpliter())));
	      }
	      if (values.size() < minSize) {
	        minSize = values.size();
	      }
	      map.put(((SplitedGroupPathNode)pair.getPathNode())
	        .getPathNode(), values);
	    }

	    for (int i = 0; i < minSize; ++i) {
	      XmlNode innerGroup = PathParsers.getInstance().parse(
	        node.getInnerGroup(), null, group);
	      for (Iterator it = map.keySet().iterator(); it.hasNext(); ) { 
	    	  IPathNode pn = (IPathNode)it.next();
	        PathParsers.getInstance().parse(pn, (String)((List)map.get(pn)).get(i), 
	          innerGroup);
	      }
	    }
	  }
	}
