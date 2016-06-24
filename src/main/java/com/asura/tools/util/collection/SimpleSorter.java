package com.asura.tools.util.collection;

import java.util.ArrayList;
import java.util.List;

import com.asura.tools.data.selection.ClauseSelectOrder;
import com.asura.tools.data.selection.DataBlock;
import com.asura.tools.data.selection.DataBlocks;
import com.asura.tools.data.selection.SelectMethod;

public class SimpleSorter<T> {
	public List<T> sort(List<ISimpleSortable<T>> vs, String exp, int max) {
		DataBlock bl = new DataBlock();
		for (ISimpleSortable s : vs) {
			bl.addData(s);
		}
		DataBlocks bls = new SelectMethod(ClauseSelectOrder.fromExpression(exp)).select(bl);

		Object list = new ArrayList();
		for (int i = 0; i < Math.min(max, bls.getAllDatas().size()); ++i) {
			((List) list).add(((ISimpleSortable) bls.getAllDatas().get(i)).get());
		}

		return ((List<T>) list);
	}
}
