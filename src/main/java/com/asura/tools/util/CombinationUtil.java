package com.asura.tools.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

public class CombinationUtil {
	public static int getCountOfCombinationByWeight(String word) {
		int a = word.length();
		int count = 0;
		for (int i = 0; i < a + 1; ++i) {
			count = i * (a + 1 - i) + count;
		}

		return count;
	}

	public static List<List> getLimitedCombination(List<List> llist, int length) {
		List result = new ArrayList();
		List<List> rlist = getCombination(llist, length);
		for (List list : rlist) {
			List rrlist = getCombination(list);
			result.addAll(rrlist);
		}

		return result;
	}

	public static List<List> getLimitedCombinationWithinMax(List<List> llist, int max) {
		List result = new ArrayList();
		for (int i = 1; i <= max; ++i) {
			result.addAll(getLimitedCombination(llist, i));
		}

		return result;
	}

	public static List<List<?>> getPermutation(List<?> list) {
		List llist = new ArrayList();
		if (list.size() > 0) {
			List newList = new ArrayList();
			newList.add(list.get(0));
			llist.add(newList);
			for (int i = 1; i < list.size(); ++i) {
				llist = buildPermutation(llist, list.get(i));
			}
		}

		return llist;
	}

	private static List<List<?>> buildPermutation(List<List<?>> llist, Object element) {
		List newLList = new ArrayList();
		for (List list : llist) {
			for (int i = 0; i <= list.size(); ++i) {
				List newList = new ArrayList();
				for (Iterator localIterator2 = list.iterator(); localIterator2.hasNext();) {
					Object obj = localIterator2.next();
					newList.add(obj);
				}
				newList.add(i, element);
				newLList.add(newList);
			}
		}

		return newLList;
	}

	public static List<List> getCombination(List list, int length) {
		List resultwList = new ArrayList();
		if (list.size() <= length) {
			resultwList.add(list);
		} else {
			boolean reverse = false;
			if (list.size() - length < length) {
				length = list.size() - length;
				reverse = true;
			}
			List<List> newList = new ArrayList();
			List wholeList = new ArrayList();
			for (int j = 0; j < list.size(); ++j) {
				wholeList.add(new Entry(list.get(j), j));
			}
			for (int i = 0; i < length; ++i) {
				List entryList = new ArrayList();
				for (int j = 0; j < list.size(); ++j) {
					entryList.add(new Entry(list.get(j), j));
				}
				newList.add(entryList);
			}
			newList = getCombination(newList);

			Iterator it = newList.iterator();
			HashSet resultSet = new HashSet();
			Object entry;
			while (it.hasNext()) {
				List lo = (List) it.next();
				HashSet hs = new HashSet();
				for (Iterator localIterator1 = lo.iterator(); localIterator1.hasNext();) {
					entry = localIterator1.next();
					hs.add(Integer.valueOf(((Entry) entry).getPosition()));
				}

				if (hs.size() < lo.size())
					it.remove();
				else if (resultSet.contains(hs))
					it.remove();
				else {
					resultSet.add(hs);
				}
			}

			if (reverse) {
				List nnList = new ArrayList();
				for (List newchildList : newList) {
					nnList.add(getLeftPart(wholeList, newchildList));
				}
				newList = nnList;
			}

			for (int i = 0; i < newList.size(); ++i) {
				List vList = new ArrayList();
				for (int j = 0; j < ((List) newList.get(i)).size(); ++j) {
					vList.add(((Entry) ((List) newList.get(i)).get(j)).getObject());
				}
				resultwList.add(vList);
			}
		}

		return resultwList;
	}

	private static List getLeftPart(List sourceList, List valueList) {
		List leftList = new ArrayList();
		for (Iterator localIterator1 = sourceList.iterator(); localIterator1.hasNext();) {
			Object o = localIterator1.next();
			Entry entry = (Entry) o;
			boolean contains = false;
			for (Iterator localIterator2 = valueList.iterator(); localIterator2.hasNext();) {
				Object ob = localIterator2.next();
				Entry ventry = (Entry) ob;
				if (ventry.getPosition() == entry.getPosition()) {
					contains = true;
					break;
				}
			}
			if (!(contains)) {
				leftList.add(o);
			}
		}

		return leftList;
	}

	public static List<List> getCombination(List<List> list) {
		Hashtable recorder = new Hashtable();
		Hashtable done = new Hashtable();
		int changePosition = 0;
		int currentPosition = 0;
		List result = new ArrayList();

		for (int i = 0; i < list.size(); ++i) {
			if (((List) list.get(i)).size() == 0) {
				list.remove(i);
			}

		}

		for (int i = 0; i < list.size(); ++i) {
			recorder.put(Integer.valueOf(i), Integer.valueOf(0));
		}

		for (int i = 0; i < list.size(); ++i) {
			done.put(Integer.valueOf(i), Integer.valueOf(((List) list.get(i)).size() - 1));
		}

		while ((recorder.size() <= done.size()) && (recorder.size() != 0)) {
			List oneList = new ArrayList();
			for (int i = 0; i < list.size(); ++i) {
				oneList.add(((List) list.get(i)).get(((Integer) recorder.get(Integer.valueOf(i))).intValue()));
			}
			result.add(oneList);
			adjustValue(list, recorder, done, currentPosition, changePosition);
		}

		return result;
	}

	private static void adjustValue(List<List> list, Hashtable<Integer, Integer> recorder,
			Hashtable<Integer, Integer> done, int currentPosition, int changePosition) {
		for (int i = 0; i < list.size(); ++i)
			if (i < changePosition) {
				if (((Integer) recorder.get(Integer.valueOf(i))).intValue() < ((Integer) done.get(Integer.valueOf(i)))
						.intValue()) {
					if (currentPosition != i)
						continue;
					recorder.put(Integer.valueOf(i),
							Integer.valueOf(((Integer) recorder.get(Integer.valueOf(i))).intValue() + 1));
					currentPosition = i;
					return;
				}

				recorder.put(Integer.valueOf(i), Integer.valueOf(0));
				for (int j = i + 1; j < changePosition; ++j) {
					if (((Integer) recorder.get(Integer.valueOf(j)))
							.intValue() < ((Integer) done.get(Integer.valueOf(j))).intValue()) {
						recorder.put(Integer.valueOf(j),
								Integer.valueOf(((Integer) recorder.get(Integer.valueOf(j))).intValue() + 1));
						break;
					}
					recorder.put(Integer.valueOf(j), Integer.valueOf(0));
				}

				currentPosition = 0;
			} else {
				if ((i != changePosition) || (currentPosition != changePosition))
					continue;
				if (((Integer) recorder.get(Integer.valueOf(i))).intValue() < ((Integer) done.get(Integer.valueOf(i)))
						.intValue()) {
					recorder.put(Integer.valueOf(i),
							Integer.valueOf(((Integer) recorder.get(Integer.valueOf(i))).intValue() + 1));
					currentPosition = i;
					return;
				}
				recorder.put(Integer.valueOf(i), Integer.valueOf(0));
				for (int j = i + 1; j < done.size() + 1; ++j) {
					if (j < done.size()) {
						if (((Integer) recorder.get(Integer.valueOf(j)))
								.intValue() < ((Integer) done.get(Integer.valueOf(j))).intValue()) {
							recorder.put(Integer.valueOf(j),
									Integer.valueOf(((Integer) recorder.get(Integer.valueOf(j))).intValue() + 1));
							break;
						}
						recorder.put(Integer.valueOf(j), Integer.valueOf(0));
					} else {
						recorder.put(Integer.valueOf(j), Integer.valueOf(0));
					}
				}
				currentPosition = 0;
				++changePosition;

				return;
			}
	}
}
