package com.asura.tools.data.selection.expression;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.asura.tools.data.selection.ClauseSelectOrder;
import com.asura.tools.data.selection.DataBlock;
import com.asura.tools.data.selection.DataBlocks;
import com.asura.tools.data.selection.FeatureSelectOrder;
import com.asura.tools.data.selection.IOrderValue;
import com.asura.tools.data.selection.ISelectOrder;
import com.asura.tools.data.selection.SelectMethod;
import com.asura.tools.util.StringUtil;

public class SimpleSelectOrder implements ISelectOrder {
	private static final long serialVersionUID = -5787950387292672718L;
	private static final String SPLITER_FEATURE = ",";
	private static final String SPLITER_TYPE = ":";
	private String andFeatures;
	private String orFeatures;
	private static List<IExpressionParser> parsers = new ArrayList();

	static {
		parsers.add(new BooleanExpressionParser());
		parsers.add(new NumberSequenceExpressionParser());
		parsers.add(new NumberRangeExpressionParser());
		parsers.add(new StringSequenceExpressionParser());
		parsers.add(new StringRangeExpressionParser());
	}

	public SimpleSelectOrder() {
	}

	public SimpleSelectOrder(String andFeatures, String orFeatures) {
		this.andFeatures = andFeatures;
		this.orFeatures = orFeatures;
	}

	public DataBlocks sort(DataBlock block) {
		ClauseSelectOrder clause = new ClauseSelectOrder();

		List<FeatureSelectOrder> andOrders = buildOrder(this.andFeatures);
		for (FeatureSelectOrder order : andOrders) {
			clause.addAndSelectOrder(order);
		}

		List<FeatureSelectOrder> orOrders = buildOrder(this.orFeatures);
		for (FeatureSelectOrder order : orOrders) {
			clause.addOrSelectOrder(order);
		}

		return clause.sort(block);
	}

	public String getAndFeatures() {
		return this.andFeatures;
	}

	public void setAndFeatures(String andFeatures) {
		this.andFeatures = andFeatures;
	}

	public String getOrFeatures() {
		return this.orFeatures;
	}

	public void setOrFeatures(String orFeatures) {
		this.orFeatures = orFeatures;
	}

	private List<FeatureSelectOrder> buildOrder(String features) {
		List orders = new ArrayList();
		if (!(StringUtil.isNullOrEmpty(features))) {
			for (String feature : features.split(",")) {
				String[] ss = feature.split(":");
				if (ss.length == 2) {
					FeatureSelectOrder featureOrder = new FeatureSelectOrder();
					featureOrder.setFeature(ss[0].trim());

					featureOrder.setValue(getOrderValue(ss[1]));

					orders.add(featureOrder);
				}
			}
		}

		return orders;
	}

	private IOrderValue getOrderValue(String type) {
		for (IExpressionParser parser : parsers) {
			if (parser.canParse(type)) {
				return parser.parse(type);
			}
		}

		throw new RuntimeException("can not parse type:" + type);
	}

	public Set<String> getAllFeatures() {
		HashSet set = new HashSet();
		List<FeatureSelectOrder> andOrders = buildOrder(this.andFeatures);
		List<FeatureSelectOrder> orOrders = buildOrder(this.orFeatures);

		for (ISelectOrder order : orOrders) {
			set.addAll(order.getAllFeatures());
		}

		for (ISelectOrder order : andOrders) {
			set.addAll(order.getAllFeatures());
		}

		return set;
	}

	public String toString() {
		return SelectMethod.getXStream().toXML(this);
	}

}
