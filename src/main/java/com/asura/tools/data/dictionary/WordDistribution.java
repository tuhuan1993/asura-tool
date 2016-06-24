package com.asura.tools.data.dictionary;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.asura.tools.data.dictionary.WordDistribution.CharToken;
import com.asura.tools.data.selection.DataBlock;
import com.asura.tools.data.selection.IFeaturable;
import com.asura.tools.data.selection.SelectMethod;
import com.asura.tools.util.StringUtil;
import com.asura.tools.util.collection.Accumulator;

public class WordDistribution implements Serializable {
	private static final long serialVersionUID = 4208692631361342479L;
	private Accumulator<String> acc;

	public WordDistribution() {
		this.acc = new Accumulator<String>();
	}

	public void add(String word) {
		this.acc.addKey(word.intern());
	}

	public void delete(String word) {
		this.acc.minusKey(word);
	}

	public boolean isUnique() {
		return (this.acc.getKeys().size() == 1);
	}

	public int count() {
		return this.acc.keyCount();
	}

	public int allCount() {
		return this.acc.getAllCount();
	}

	public String[] select(String exp) {
		SelectMethod sm = new SelectMethod(exp);
		DataBlock bl = new DataBlock();
		for (String key : this.acc.getKeys()) {
			bl.addData(new CharToken(this.acc, key));
		}

		List list = new ArrayList();
		for (IFeaturable fe : sm.select(bl).getAllDatas()) {
			CharToken ct = (CharToken) fe;
			list.add(ct.getChar());
		}

		return ((String[]) list.toArray(new String[0]));
	}

	public int getMaxPercent() {
		if (this.acc.keyCount() > 0) {
			for (String key : this.acc.keysSortedByValue(2)) {
				if (!(StringUtil.isNullOrEmpty(key))) {
					return (this.acc.getCount(key) * 100 / this.acc.getAllCount());
				}
			}
		}

		return 0;
	}

	public String getDistribution() {
		List list = new ArrayList();
		for (String key : this.acc.keysSortedByValue()) {
			list.add(key + "=" + this.acc.getCount(key));
		}

		return StringUtil.getStringFromStrings(list, ", ");
	}

	class CharToken implements IFeaturable {
		private Accumulator<String> acc;
		private String string;

		public CharToken(Accumulator<String> acc,String string) {
			this.acc = acc;
			this.string = string;
		}

		public String getChar() {
			return this.string;
		}

		public String getFeatureValue(String feature) {
			if ("count".equals(feature))
				return String.valueOf(this.acc.getCount(this.string));
			if ("percent".equals(feature)) {
				return String.valueOf(100 * this.acc.getCount(this.string) / this.acc.getAllCount());
			}

			return "";
		}

		public Object getObject() {
			return this;
		}

		public boolean hasFeature(String feature) {
			return true;
		}
	}
}