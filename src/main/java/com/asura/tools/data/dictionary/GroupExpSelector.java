package com.asura.tools.data.dictionary;

import com.asura.tools.data.selection.SelectMethod;
import com.asura.tools.data.selection.data.DataBlock;
import com.asura.tools.data.selection.data.DataBlocks;

public class GroupExpSelector implements IGroupSelector {
	private String exp;

	public GroupExpSelector(String exp) {
		this.exp = exp;
	}

	public WordGroup select(WordGroup[] groups) {
		if ((groups != null) && (groups.length > 0)) {
			DataBlock bl = new DataBlock();
			for (WordGroup wg : groups) {
				bl.addData(new WordGroupFeature(wg));
			}

			DataBlocks bls = new SelectMethod(this.exp).select(bl);
			if (bls.getAllDatas().size() > 0) {
				return ((WordGroupFeature) bls.getAllDatas().get(0)).getGroup();
			}

			return groups[0];
		}
		return null;
	}
}
