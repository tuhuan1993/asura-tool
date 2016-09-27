package com.asura.tools.data.selection.selector;

import com.asura.tools.data.selection.data.DataBlock;
import com.asura.tools.data.selection.data.DataBlocks;
import com.asura.tools.data.selection.data.IFeaturable;

public interface ISelector {
	public DataBlock select(DataBlocks paramDataBlocks, int paramInt);

	public IFeaturable selectOne(DataBlocks paramDataBlocks);
}
