package com.asura.tools.data.selection.selectorder;

import java.io.Serializable;
import java.util.Set;

import com.asura.tools.condition.IClausable;
import com.asura.tools.data.selection.data.DataBlock;
import com.asura.tools.data.selection.data.DataBlocks;

public interface ISelectOrder extends Serializable, IClausable {
	public DataBlocks sort(DataBlock paramDataBlock);

	public Set<String> getAllFeatures();
}
