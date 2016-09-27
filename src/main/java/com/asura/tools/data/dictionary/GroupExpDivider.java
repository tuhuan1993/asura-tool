package com.asura.tools.data.dictionary;

import java.util.ArrayList;
import java.util.List;

import com.asura.tools.data.selection.SelectMethod;
import com.asura.tools.data.selection.data.DataBlock;
import com.asura.tools.data.selection.data.DataBlocks;
import com.asura.tools.data.selection.data.IFeaturable;

public class GroupExpDivider implements IGroupDivider {
	private String exp;

	public GroupExpDivider() {
	}

	public GroupExpDivider(String exp) {
		this.exp = exp;
	}

	public WordGroup[] divide(WordGroup group) {
		List groups = new ArrayList();

		DataBlock bl = group.getBlock();
		DataBlocks bls = new SelectMethod(this.exp).select(bl);
		for (DataBlock b : bls.getBlocks()) {
			WordGroup newGroup = new WordGroup();
			for (IFeaturable fe : b.getDataList()) {
				newGroup.addSingleWord(((WordFeature) fe).getWord());
			}
			groups.add(newGroup);
		}

		return ((WordGroup[]) groups.toArray(new WordGroup[0]));
	}
}
