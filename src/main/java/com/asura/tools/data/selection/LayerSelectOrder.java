package com.asura.tools.data.selection;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LayerSelectOrder implements ISelectOrder {
	private static final long serialVersionUID = 720907101916391579L;
	private List<ISelectOrder> orders;

	public LayerSelectOrder() {
		this.orders = new ArrayList();
	}

	public void addSelectOrder(ISelectOrder order) {
		this.orders.add(order);
	}

	public List<ISelectOrder> getOrders() {
		return this.orders;
	}

	public void setOrders(List<ISelectOrder> orders) {
		this.orders = orders;
	}

	public DataBlocks sort(DataBlock block) {
		DataBlocks result = new DataBlocks();

		result.addDataBlock(block);
		DataBlocks temp = new DataBlocks();
		for (int i = 0; i < this.orders.size(); ++i) {
			temp.clear();
			for (DataBlock b : result.getBlocks()) {
				temp.addDataBlocks(((ISelectOrder) this.orders.get(i)).sort(b));
			}

			result.clear();
			for (DataBlock db : temp.getBlocks()) {
				result.addDataBlock(db);
			}
		}

		result.removeEmptyBlokcs();

		return result;
	}

	public Set<String> getAllFeatures() {
		HashSet set = new HashSet();
		for (ISelectOrder order : this.orders) {
			set.addAll(order.getAllFeatures());
		}

		return set;
	}
}
