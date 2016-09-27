package com.asura.tools.data.selection.selectorder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.asura.tools.data.selection.SelectMethod;
import com.asura.tools.data.selection.data.DataBlock;
import com.asura.tools.data.selection.data.DataBlocks;

public class SortedSelectOrder implements ISelectOrder {
	private static final long serialVersionUID = 9094133092796713995L;
	private List<ISelectOrder> orders;

	public SortedSelectOrder() {
		this.orders = new ArrayList<>();
	}

	public void addSelectOrder(ISelectOrder order) {
		this.orders.add(order);
	}

	public DataBlocks sort(DataBlock block) {
		DataBlocks result = new DataBlocks();

		for (ISelectOrder order : this.orders) {
			DataBlocks dbs = order.sort(block);
			result = result.appendMerge(dbs);
		}

		result.removeEmptyBlokcs();

		return result;
	}

	public Set<String> getAllFeatures() {
		HashSet<String> set = new HashSet<>();
		for (ISelectOrder order : this.orders) {
			set.addAll(order.getAllFeatures());
		}
		return set;
	}

	public List<ISelectOrder> getOrders() {
		return this.orders;
	}

	public void setOrders(List<ISelectOrder> orders) {
		this.orders = orders;
	}

	public String toString() {
		return SelectMethod.getXStream().toXML(this);
	}
}
