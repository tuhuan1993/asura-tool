package com.asura.tools.data.selection;

public class BlockRandomSelector implements ISelector {
	public DataBlock select(DataBlocks blocks, int count) {
		DataBlock db = new DataBlock();
		int currentSize = 0;
		for (DataBlock block : blocks.getBlocks()) {
			if (count - currentSize > block.size()) {
				db = db.orDataBlock(block);
			} else {
				DataBlocks dbs = new DataBlocks();
				dbs.addDataBlock(block);
				DataBlock result = new RandomSelector().select(dbs, count - currentSize);
				db = db.orDataBlock(result);

				break;
			}

			currentSize = db.size();
		}

		return db;
	}

	public IFeaturable selectOne(DataBlocks blocks) {
		DataBlock db = select(blocks, 1);
		if (db.size() > 0) {
			return ((IFeaturable) db.getDataList().get(0));
		}

		return null;
	}
}