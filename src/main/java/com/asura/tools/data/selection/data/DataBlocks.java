package com.asura.tools.data.selection.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DataBlocks implements Serializable {
	private static final long serialVersionUID = -8932003463854348556L;
	private List<DataBlock> blocks;

	public DataBlocks() {
		this.blocks = new ArrayList<>();
	}

	public void addDataBlock(DataBlock block) {
		this.blocks.add(block);
	}

	public void addDataAtBlock(MarkedData data, int block) {
		while (this.blocks.size() <= block) {
			this.blocks.add(new DataBlock());
		}

		((DataBlock) this.blocks.get(block)).addMarkedData(data);
	}

	public void removeEmptyBlokcs() {
		List<DataBlock> temp = new ArrayList<>();
		for (DataBlock db : this.blocks) {
			if (db.count() != 0) {
				temp.add(db);
			}
		}

		this.blocks = temp;
	}

	public DataBlocks andMerge(DataBlocks anotherBlocks) {
		DataBlocks result = new DataBlocks();
		for (DataBlock block : this.blocks) {
			for (DataBlock ab : anotherBlocks.getBlocks()) {
				result.addDataBlock(block.andDataBlock(ab));
			}
		}

		return result;
	}

	public List<IFeaturable> getAllDatas() {
		List<IFeaturable> list = new ArrayList<>();
		for (DataBlock bl : this.blocks) {
			list.addAll(bl.getDataList());
		}

		return list;
	}

	DataBlock getMarkedDatas() {
		DataBlock block = new DataBlock();
		for (DataBlock bl : this.blocks) {
			block.addBlock(bl);
		}

		return block;
	}

	public DataBlocks appendMerge(DataBlocks anotherBlocks) {
		DataBlocks result = new DataBlocks();
		for (DataBlock block : this.blocks) {
			result.addDataBlock(block);
		}

		DataBlock bl = result.getMarkedDatas();

		for (DataBlock block : anotherBlocks.getBlocks()) {
			block.removeBlock(bl);
			result.addDataBlock(block);
		}

		return result;
	}

	public DataBlocks orMerge(DataBlocks anotherBlocks) {
		DataBlocks result = new DataBlocks();

		DataBlock db = new DataBlock();
		for (DataBlock block : this.blocks) {
			db = db.orDataBlock(block);
		}

		for (DataBlock ab : anotherBlocks.getBlocks()) {
			db = db.orDataBlock(ab);
		}

		result.addDataBlock(db);

		return result;
	}

	public void addDataBlocks(DataBlocks blocks) {
		this.blocks.addAll(blocks.getBlocks());
	}

	public List<DataBlock> getBlocks() {
		return this.blocks;
	}

	public void setBlocks(List<DataBlock> blocks) {
		this.blocks = blocks;
	}

	public void clear() {
		this.blocks.clear();
	}

	public String toString() {
		return super.getClass().getSimpleName() + "[" + this.blocks + "]";
	}
}