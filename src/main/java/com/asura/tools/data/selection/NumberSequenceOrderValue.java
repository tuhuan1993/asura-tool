package com.asura.tools.data.selection;

public class NumberSequenceOrderValue implements IOrderValue {
	private static final long serialVersionUID = -9136362527122430448L;
	private int min;
	private int max;
	private boolean desc;

	public NumberSequenceOrderValue() {
		this.max = 1000;
		this.min = 0;
		this.desc = false;
	}

	public NumberSequenceOrderValue(int min, int max) {
		this.max = max;
		this.min = min;
		this.desc = false;
	}

	public NumberSequenceOrderValue(int min, int max, boolean desc) {
		this.max = max;
		this.min = min;
		this.desc = desc;
	}

	public int getBlockIndex(String value) {
		try {
			int iv = Double.valueOf(value).intValue();
			if ((iv >= this.min) && (iv <= this.max)) {
				if (!(this.desc)) {
					return iv;
				}
				return (this.max - iv);
			}
		} catch (Exception localException) {
		}
		return -1;
	}

	public boolean contains(String value) {
		try {
			int iv = Double.valueOf(value).intValue();
			if ((iv >= this.min) && (iv <= this.max))
				return true;
		} catch (Exception localException) {
		}
		return false;
	}

	public int getMax() {
		return this.max;
	}

	public void setMax(int max) {
		this.max = max;
	}

	public boolean isDesc() {
		return this.desc;
	}

	public void setDesc(boolean desc) {
		this.desc = desc;
	}

	public int getMin() {
		return this.min;
	}

	public void setMin(int min) {
		this.min = min;
	}

	public String toString() {
		return "ns_" + this.min + "_" + this.max;
	}
}