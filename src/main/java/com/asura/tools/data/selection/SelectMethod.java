package com.asura.tools.data.selection;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.asura.tools.data.selection.data.DataBlock;
import com.asura.tools.data.selection.data.DataBlocks;
import com.asura.tools.data.selection.data.IFeaturable;
import com.asura.tools.data.selection.ordervalue.NumberSequenceOrderValue;
import com.asura.tools.data.selection.ordervalue.RangeNumberOrderValue;
import com.asura.tools.data.selection.ordervalue.RangeStringOrderValue;
import com.asura.tools.data.selection.ordervalue.SequenceOrderValue;
import com.asura.tools.data.selection.selectorder.ClauseSelectOrder;
import com.asura.tools.data.selection.selectorder.FeatureSelectOrder;
import com.asura.tools.data.selection.selectorder.ISelectOrder;
import com.asura.tools.data.selection.selectorder.LayerSelectOrder;
import com.asura.tools.data.selection.selectorder.SimpleSelectOrder;
import com.asura.tools.data.selection.selectorder.SortedSelectOrder;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class SelectMethod {
	private List<OrderSequence> sequences;

	public SelectMethod() {
		this.sequences = new ArrayList<>();
	}

	public SelectMethod(ISelectOrder order) {
		this.sequences = new ArrayList<>();
		OrderSequence os = new OrderSequence();
		os.addSelectOrder(order);
		this.sequences.add(os);
	}

	public SelectMethod(String exp) {
		this(ClauseSelectOrder.fromExpression(exp));
	}

	public void addOrderSequence(OrderSequence sequence) {
		this.sequences.add(sequence);
	}

	public List<OrderSequence> getSequences() {
		return this.sequences;
	}

	public void setSequences(List<OrderSequence> sequences) {
		this.sequences = sequences;
	}

	public Set<String> getAllFeatures() {
		HashSet<String> set = new HashSet<>();
		for (OrderSequence os : this.sequences) {
			set.addAll(os.getAllFeatures());
		}

		return set;
	}

	public boolean meet(IFeaturable fe) {
		DataBlock bl = new DataBlock();
		bl.addData(fe);

		return (select(bl).getAllDatas().size() == 1);
	}

	public DataBlocks select(DataBlock block) {
		DataBlocks result = new DataBlocks();
		if (this.sequences.size() > 0)
			for (OrderSequence sequence : this.sequences) {
				DataBlocks bd = sequence.sort(block);
				result = result.appendMerge(bd);
			}
		else {
			result.addDataBlock(block);
		}

		result.removeEmptyBlokcs();

		return result;
	}

	public static XStream getXStream() {
		XStream xs = new XStream(new DomDriver());
		xs.alias("select-method", SelectMethod.class);
		xs.alias("order-sequence", OrderSequence.class);
		xs.alias("clause-order", ClauseSelectOrder.class);
		xs.alias("sorted-order", SortedSelectOrder.class);
		xs.alias("layer-order", LayerSelectOrder.class);
		xs.alias("feature-order", FeatureSelectOrder.class);
		xs.alias("clause-order", ClauseSelectOrder.class);
		xs.alias("number-range", RangeNumberOrderValue.class);
		xs.alias("string-range", RangeStringOrderValue.class);
		xs.alias("sequence-value", SequenceOrderValue.class);
		xs.alias("number-sequence", NumberSequenceOrderValue.class);
		xs.alias("simple-order", SimpleSelectOrder.class);

		xs.aliasAttribute(RangeNumberOrderValue.class, "start", "start");
		xs.aliasAttribute(RangeNumberOrderValue.class, "end", "end");
		xs.aliasAttribute(RangeNumberOrderValue.class, "containsStart", "contains-start");
		xs.aliasAttribute(RangeNumberOrderValue.class, "containsEnd", "contains-end");

		xs.aliasAttribute(NumberSequenceOrderValue.class, "min", "min");
		xs.aliasAttribute(NumberSequenceOrderValue.class, "max", "max");
		xs.aliasAttribute(NumberSequenceOrderValue.class, "desc", "desc");

		xs.aliasField("or-orders", ClauseSelectOrder.class, "orList");
		xs.aliasField("and-orders", ClauseSelectOrder.class, "andList");
		xs.aliasField("append-orders", ClauseSelectOrder.class, "appendList");

		xs.aliasField("sub-orders", SortedSelectOrder.class, "orders");

		xs.aliasAttribute(RangeStringOrderValue.class, "stringValues", "string-values");
		xs.aliasAttribute(SequenceOrderValue.class, "stringValues", "string-values");
		xs.aliasAttribute(RangeStringOrderValue.class, "notContains", "not-contains");

		xs.aliasAttribute(FeatureSelectOrder.class, "feature", "feature");
		xs.aliasAttribute(SimpleSelectOrder.class, "andFeatures", "and-features");
		xs.aliasAttribute(SimpleSelectOrder.class, "orFeatures", "or-features");

		return xs;
	}

	public static SelectMethod fromXML(String xml) {
		return ((SelectMethod) getXStream().fromXML(xml));
	}

	public String toString() {
		return getXStream().toXML(this);
	}
}
