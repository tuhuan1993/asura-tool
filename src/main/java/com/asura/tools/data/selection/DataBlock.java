package com.asura.tools.data.selection;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataBlock implements Serializable {
	private static final long serialVersionUID = -3061238264871813866L;
	private List<MarkedData> dataList;
	private List<IFeaturable> feList;
	private HashMap<Integer, Integer> indexMap;

	public DataBlock() {
		this.dataList = new ArrayList();
		this.feList = new ArrayList();
		this.indexMap = new HashMap();
	}

	public void addData(IFeaturable data) {
		int index = this.indexMap.size();
		MarkedData md = new MarkedData(data, index);
		this.dataList.add(md);
		this.feList.add(data);

		this.indexMap.put(Integer.valueOf(index), Integer.valueOf(index));
	}

	void addMarkedData(MarkedData data) {
		if (!(this.indexMap.containsKey(Integer.valueOf(data.getIndex())))) {
			this.indexMap.put(Integer.valueOf(data.getIndex()), Integer.valueOf(this.indexMap.size()));
			this.dataList.add(data);
			this.feList.add(data.getFe());
		}
	}

	void deleteMarkedData(MarkedData data) {
		if (this.indexMap.containsKey(Integer.valueOf(data.getIndex()))) {
			this.dataList.set(((Integer) this.indexMap.get(Integer.valueOf(data.getIndex()))).intValue(), null);
			this.feList.set(((Integer) this.indexMap.get(Integer.valueOf(data.getIndex()))).intValue(), null);
			this.indexMap.remove(Integer.valueOf(data.getIndex()));
		}
	}

	void addBlock(DataBlock block) {
		for (MarkedData md : block.dataList)
			addMarkedData(md);
	}

	void removeBlock(DataBlock bl) {
		for (MarkedData md : bl.dataList)
			deleteMarkedData(md);
	}

	public void addDataIngoreCheck(IFeaturable data) {
		addData(data);
	}

	public void addDatas(List<IFeaturable> list) {
		for (IFeaturable fe : list)
			addData(fe);
	}

	public int count() {
		return this.indexMap.size();
	}

	List<MarkedData> getMarkedDataList() {
		return this.dataList;
	}

	public List<IFeaturable> getDataList() {
		return this.feList;
	}

	public int size() {
		return this.indexMap.size();
	}

	public DataBlock andDataBlock(DataBlock block) {
		DataBlock result = new DataBlock();
		for (MarkedData md : this.dataList) {
			if (this.indexMap.containsKey(Integer.valueOf(md.getIndex()))) {
				result.addMarkedData(md);
			}
		}

		return result;
	}

	public DataBlock orDataBlock(DataBlock block) {
		DataBlock result = new DataBlock();

		for (MarkedData md : this.dataList) {
			result.addMarkedData(md);
		}

		for (MarkedData md : block.dataList) {
			result.addMarkedData(md);
		}

		return result;
	}

	public String toString() {
		return super.getClass().getSimpleName() + "[" + "dataList=" + this.dataList + "]";
	}

}
