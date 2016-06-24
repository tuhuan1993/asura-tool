package com.asura.tools.data.selection;

import java.util.ArrayList;
import java.util.List;

import com.asura.tools.util.math.RandomUtil;

public class RandomSelector implements ISelector {
	public DataBlock select(DataBlocks blocks, int count) {
		List result = new ArrayList();
		List list = blocks.getAllDatas();
		if ((list != null) && (list.size() > 0)) {
			while (result.size() < Math.min(count, list.size())) {
				int index = RandomUtil.random(0, list.size() - 1);
				IFeaturable f = (IFeaturable) list.get(index);
				if (!(result.contains(f))) {
					result.add(f);
				}
			}
		}

		DataBlock db = new DataBlock();
		db.addDatas(result);

		return db;
	}

	public IFeaturable selectOne(DataBlocks blocks) {
		List list = blocks.getAllDatas();

		if ((list != null) && (list.size() > 0)) {
			int index = RandomUtil.random(0, list.size() - 1);
			return ((IFeaturable) list.get(index));
		}

		return null;
	}
}