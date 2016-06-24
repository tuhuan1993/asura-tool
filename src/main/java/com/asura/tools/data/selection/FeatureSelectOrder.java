package com.asura.tools.data.selection;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.asura.tools.debug.DebugPrinter;

public class FeatureSelectOrder implements ISelectOrder {
	private static final long serialVersionUID = 1119076988934964658L;
	private String feature;
	private IOrderValue value;

	public FeatureSelectOrder() {
	}

	public FeatureSelectOrder(String feature, IOrderValue value) {
		this.feature = feature;
		this.value = value;
	}

	public String getFeature() {
		return this.feature;
	}

	public void setFeature(String feature) {
		this.feature = feature;
	}

	public IOrderValue getValue() {
		return this.value;
	}

	public void setValue(IOrderValue value) {
		this.value = value;
	}

	public DataBlocks sort(DataBlock block) {
		DebugPrinter printer = DebugPrinter.getInstance("feature-order");

		DataBlocks result = new DataBlocks();
		if (this.value instanceof CharOrderValue) {
			((CharOrderValue) this.value).reset();
		}
		List<MarkedData> list = block.getMarkedDataList();
		for (MarkedData mdata : list) {
			if (mdata != null) {
				IFeaturable data = mdata.getFe();
				String fv = data.getFeatureValue(this.feature);
				if (list.size() < 1000) {
					printer.print("token: " + data.toString());
					printer.print("feature值: " + this.feature + ":" + fv);
				}
				if (this.value.contains(fv)) {
					result.addDataAtBlock(mdata, this.value.getBlockIndex(fv));
				} else if (list.size() < 1000) {
					printer.print("不满足Feature: failed, expect=" + this.value.toString());
				}
			}

		}

		result.removeEmptyBlokcs();

		return result;
	}

	public Set<String> getAllFeatures() {
		HashSet set = new HashSet();
		set.add(this.feature);

		return set;
	}
}
