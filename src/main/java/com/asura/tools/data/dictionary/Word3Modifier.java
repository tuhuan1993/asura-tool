package com.asura.tools.data.dictionary;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.asura.tools.batch.IBatchTask;
import com.asura.tools.batch.TaskBatch;
import com.asura.tools.util.FileUtil;
import com.asura.tools.util.StringUtil;
import com.asura.tools.util.collection.Accumulator;
import com.asura.tools.util.collection.RecurseContainsSet;
import com.asura.tools.util.math.NumberUtil;

public class Word3Modifier {
	public static void main(String[] args) {
		String lines[] = FileUtil.getContentByLine("/data/app/v", "utf8");
		RecurseContainsSet<String> rcs = new RecurseContainsSet();
		Accumulator acc = new Accumulator();
		String args1[];
		int k = (args1 = lines).length;
		for (int j = 0; j < k; j++) {
			String line = args1[j];
			String ss[] = StringUtil.split(line, ",");
			if (ss.length == 2)
				acc.addKey(ss[0], NumberUtil.getInt(ss[1].trim()));
			else
				System.err.println(line);
		}

		int i = 0;
		TaskBatch tb = new TaskBatch(20);
		tb.setPrintCount(1000);
		tb.start();
		for (Iterator iterator = acc.getKeys().iterator(); iterator.hasNext();) {
			String key = (String) iterator.next();
			if (i++ % 10000 == 0)
				System.out.println(i);
			if (key.length() == 2 || key.length() == 3)
				tb.addTask(new ContainsTask(key, acc, rcs));
		}

		tb.addDone();
		tb.waitForDone();
		Map cm = rcs.getContainsMap();
		for (Iterator iterator1 = cm.keySet().iterator(); iterator1.hasNext(); System.out.println()) {
			String p = (String) iterator1.next();
			int pc = acc.getCount(p);
			System.out.print((new StringBuilder(String.valueOf(p))).append("=").append(pc).append(": ").toString());
			for (Iterator iterator3 = ((Set) cm.get(p)).iterator(); iterator3.hasNext();) {
				String c = (String) iterator3.next();
				int cc = acc.getCount(c);
				System.out.print((new StringBuilder(String.valueOf(c))).append("=").append(cc).append(", ").toString());
				if (cc * 10 > pc * 9 && pc > cc) {
					acc.minusKey(p, cc);
					System.out.println();
					System.out.println((new StringBuilder(String.valueOf(p))).append(" reduce to ").append(pc - cc)
							.append(" at ").append(c).toString());
					break;
				}
			}

		}

		String key;
		for (Iterator iterator2 = acc.keysSortedByValue().iterator(); iterator2.hasNext(); System.out
				.println((new StringBuilder(String.valueOf(key))).append(",").append(acc.getCount(key)).toString()))
			key = (String) iterator2.next();

	}

	static class ContainsTask implements IBatchTask {
		private String key;
		private Accumulator<String> acc;
		private RecurseContainsSet<String> rcs;

		public ContainsTask(String key, Accumulator<String> acc, RecurseContainsSet<String> rcs) {
			this.key = key;
			this.acc = acc;
			this.rcs = rcs;
		}

		public String getKey() {
			return this.key;
		}

		public void process() {
			for (String k1 : this.acc.getKeys()) {
				if (((k1.length() != 2) && (k1.length() != 3)) || (this.key.equals(k1)))
					continue;
				if (this.key.contains(k1)) {
					this.rcs.addContains(this.key, k1);
				}
				if (k1.contains(this.key))
					this.rcs.addContains(k1, this.key);
			}
		}
	}
}
