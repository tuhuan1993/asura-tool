package com.asura.tools.handler.rchandler;

import com.asura.tools.data.DataRecord;

public abstract class DefaultResChainHandler extends ResChainHandler{
	
	protected boolean isContinue=true;

	public boolean isContinue() {
		return isContinue;
	}

	public void setContinue(boolean isContinue) {
		this.isContinue = isContinue;
	}

	@Override
	public DataRecord process(DataRecord record, ResChainHandlerLog log) {
		DataRecord rd=handle(record, log);
		if(isContinue()&&getSuccesor()!=null){
			return getSuccesor().process(rd, log);
		}
		return rd;
	}
	
	public abstract DataRecord handle(DataRecord record, ResChainHandlerLog log);

}
