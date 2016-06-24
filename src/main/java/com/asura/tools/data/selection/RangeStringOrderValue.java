package com.asura.tools.data.selection;

import java.util.ArrayList;
import java.util.List;

import com.asura.tools.util.StringUtil;

public class RangeStringOrderValue implements IOrderValue {
	private static final long serialVersionUID = -7377061476226919247L;
	private List<String> values;
	private String stringValues;
	private boolean notContains;

	public RangeStringOrderValue() {
		this.notContains = false;
	}

	public RangeStringOrderValue(String[] values) {
		this.stringValues = StringUtil.getStringFromStrings(values, ",");
		this.notContains = false;
	}

	public RangeStringOrderValue(String[] values, boolean notContains) {
		this(values);
		this.notContains = notContains;
	}

	public boolean isNotContains() {
		return this.notContains;
	}

	public void setNotContains(boolean notContains) {
		this.notContains = notContains;
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
						return 0;
				} else if (cv.equals("notnull")) {
					if ((!(StringUtil.isNullOrEmpty(value))) && (!(value.equals("null"))))
						return 0;
				} else if (((String) this.values.get(i)).equals(value)) {
					return 0;
				}
			}
		}

		return -1;
	}

	private void prepare() {
		if ((this.values == null) || (this.values.size() == 0)) {
			this.values = new ArrayList();
			if (!(StringUtil.isNullOrEmpty(this.stringValues))) {
				String[] ss = this.stringValues.split(",");
				for (String s : ss)
					this.values.add(s.trim());
			}
		}
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

	public List<String> getValues() {
		return this.values;
	}

	public void setValues(List<String> values) {
		this.values = values;
	}

	public String toString() {
		return "sr_" + StringUtil.getStringFromStrings(this.values, "_");
	}
}
