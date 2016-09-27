package com.asura.tools.data.selection.data;

public class MarkedData {
	private IFeaturable fe;
	private int index;

	public MarkedData(IFeaturable fe, int index) {
		this.fe = fe;
		this.index = index;
	}

	public IFeaturable getFe() {
		return this.fe;
	}

	public void setFe(IFeaturable fe) {
		this.fe = fe;
	}

	public int getIndex() {
		return this.index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String toString() {
		return this.index + ":" + this.fe.toString();
	}
}
