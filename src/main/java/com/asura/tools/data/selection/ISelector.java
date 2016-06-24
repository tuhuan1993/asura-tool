package com.asura.tools.data.selection;

public interface ISelector {
	public DataBlock select(DataBlocks paramDataBlocks, int paramInt);

	public IFeaturable selectOne(DataBlocks paramDataBlocks);
}
