package com.asura.tools.data.dictionary;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import com.asura.tools.batch.TaskBatch;
import com.asura.tools.debug.DebugPrinter;
import com.asura.tools.debug.SpendTimer;
import com.asura.tools.util.FileUtil;
import com.asura.tools.util.StringUtil;

public class DictionaryBuilder {
	public static void main(String[] args) {
		DebugPrinter.enableKey("dic");
		DebugPrinter.setAble(true);

		new DictionaryBuilder().build("/data/app/sk", "/data/app/words");
	}

	public void build(String file, String outputFile) {
		SpendTimer timer = SpendTimer.getInstance("dic");
		SpendTimer.enableKey("dic");
		timer.startTime();

		WordGroup wg = WordGroup.fromFime(file);

		timer.printSpendTime("initial from file");

		build(wg, outputFile);
	}

	public void build(WordGroup group, String output) {
		SpendTimer timer = SpendTimer.getInstance("dic");

		HashSet<SingleWord> sws = new HashSet();

		String exp = "count:nr_1_1000000 && percent:nr_1_1000";

		for (int i = 1; i < 4; ++i) {
			System.out.println("handle len " + i);
			handleLength(group, i, exp, sws);
		}

		timer.printSpendTime("handle len");

		WordGroup newGroup = new WordGroup();
		for (SingleWord sw : sws) {
			newGroup.addSingleWord(sw);
		}

		newGroup.sort("count:ns_1000000_3");
		System.out.println(newGroup.getWords());

		WordGroup[] wgs = new GroupExpDivider("acount:ns_1000000_3 && length:ns_1_10").divide(newGroup);

		timer.printSpendTime("divide by count and length");

		WordGroup latest = new WordGroup();

		TaskBatch tb = new TaskBatch(10);
		tb.start();
		int i = 0;
		for (WordGroup wg : wgs) {
			System.out.println("handle " + (i++) + " of " + wgs.length + ", count " + wg.count());
			if (wg.count() < -1)
				tb.addTask(new ConfictJob(latest, wg));
			else {
				latest.appendGroup(wg);
			}
		}
		tb.addDone();
		tb.waitForDone();

		timer.printSpendTime("handle blocks");

		latest.removeConflict("count:ns_1000000_1 && length:ns_4_2 && start:ns_0_100", 1);
		latest.removeStopwords(new String[] { "的", "之" });

		new Group4WordOptimizer().optimize(latest);

		timer.printSpendTime("remove conflict");

		latest.sort("count:ns_1000000_1");

		timer.printSpendTime("sort words");

		List list = new ArrayList();
		for (SingleWord sw : latest.getWords(1)) {
			list.add(sw.toString());
		}
		FileUtil.output(list, output, "utf8");
	}

	private void handleLength(WordGroup group, int len, String exp, HashSet<SingleWord> set) {
		Collection<SingleWord> col = (Collection) set.clone();
		if (len == 1) {
			col = group.getWords();
		}
		for (SingleWord sw : col)
			if (sw.getWord().length() == len) {
				String[] headers = sw.getHeaders().select(exp);
				for (String h : headers) {
					if (!(StringUtil.isNullOrEmpty(h))) {
						SingleWord newSw = group.get(h + sw.getWord());
						if (newSw != null) {
							set.add(newSw);
						}
					}
				}

				String[] tails = sw.getTails().select(exp);
				for (String t : tails)
					if (!(StringUtil.isNullOrEmpty(t))) {
						SingleWord newSw = group.get(sw.getWord() + t);
						if (newSw != null)
							set.add(newSw);
					}
			}
	}
}
