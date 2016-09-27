package com.asura.tools.data.selection.ordervalue;

import java.util.ArrayList;
import java.util.List;

import com.asura.tools.util.StringUtil;

public class SequenceOrderValue implements IOrderValue {
	private static final long serialVersionUID = -767628029318396417L;
	private List<String> values;
	private String stringValues;

	public SequenceOrderValue() {
	}

	public SequenceOrderValue(String[] values) {
		this.stringValues = StringUtil.getStringFromStrings(values, ",");
	}

	public String getStringValues() {
		return this.stringValues;
	}

	public void setStringValues(String stringValues) {
		this.stringValues = stringValues;
	}

	public int getBlockIndex(String value) {
		prepare();
		if ((this.values.size() == 1) && (((String) this.values.get(0)).equals("true"))) {
			if ((!(StringUtil.isNullOrEmpty(value))) && (!(value.equals("null"))) && (!(value.equals("false"))))
				return 0;
		} else {
			for (int i = 0; i < this.values.size(); ++i) {
				String cv = (String) this.values.get(i);
				if (cv.equals("null")) {
					if ((value.equals("null")) || (value.equals("")))
						return i;
				} else if (cv.equals("notnull")) {
					if ((!(StringUtil.isNullOrEmpty(value))) && (!(value.equals("null")))) {
						return i;
					}
				} else if (((String) this.values.get(i)).equals(value)) {
					return i;
				}
			}

		}

		return -1;
	}

	public boolean contains(String value) {
		prepare();
		if ((this.values.size() == 1) && (((String) this.values.get(0)).equals("true"))) {
			if ((!(StringUtil.isNullOrEmpty(value))) && (!(value.equals("null"))) && (!(value.equals("false"))))
				return true;
		} else {
			for (int i = 0; i < this.values.size(); ++i) {
				String cv = (String) this.values.get(i);
				if (cv.equals("null")) {
					if ((value.equals("null")) || (value.equals("")))
						return true;
				} else if (cv.equals("notnull")) {
					if ((!(StringUtil.isNullOrEmpty(value))) && (!(value.equals("null")))) {
						return true;
					}
				} else if (((String) this.values.get(i)).equals(value)) {
					return true;
				}
			}

		}

		return false;
	}

	private void prepare() {
		if ((this.values == null) || (this.values.size() == 0)) {
			this.values = new ArrayList<>();
			if (!(StringUtil.isNullOrEmpty(this.stringValues))) {
				String[] ss = StringUtil.split(this.stringValues, ",");
				for (String s : ss)
					this.values.add(s.trim());
			}
		}
	}

	public List<String> getValues() {
		return this.values;
	}

	public void setValues(List<String> values) {
		this.values = values;
	}

	public String toString() {
		return "ss_" + StringUtil.getStringFromStrings(this.values, "_");
	}
}
