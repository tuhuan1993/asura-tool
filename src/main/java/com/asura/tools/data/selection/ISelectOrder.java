package com.asura.tools.data.selection;

import java.io.Serializable;
import java.util.Set;

import com.asura.tools.condition.IClausable;

public interface ISelectOrder extends Serializable, IClausable {
	public DataBlocks sort(DataBlock paramDataBlock);

	public Set<String> getAllFeatures();
}
