package com.asura.tools.condition;

import java.util.HashMap;

public class MeetClause<T, C extends IClausable> implements ICondition<T>, IExpClause {
	private HashMap<ICondition<T>, Boolean> ands;
	private HashMap<ICondition<T>, Boolean> ors;
	private IExpParser<C> subParser;

	public MeetClause(IExpParser<C> subParser) {
		this.subParser = subParser;
		this.ands = new HashMap<>();
		this.ors = new HashMap<>();
	}

	public boolean meet(T t) {
		for (ICondition<T> con : this.ands.keySet()) {
			boolean neg = ((Boolean) this.ands.get(con)).booleanValue();
			if ((con.meet(t)) && (!(neg)))
				continue;
			if ((con.meet(t)) || (!(neg))) {
				return false;
			}
		}

		int count = 0;
		for (ICondition<T> con : this.ors.keySet()) {
			boolean neg = ((Boolean) this.ors.get(con)).booleanValue();
			if (((con.meet(t)) && (!(neg))) || ((!(con.meet(t))) && (neg))) {
				++count;
			}
		}

		return ((count > 0) || (this.ors.size() == 0));
	}

	public MeetClause<T, C> parse(String expression) {
		return ((MeetClause) new ClauseParser().parse(expression, null, this));
	}

	public void addAnd(IClausable t, boolean negative) {
		this.ands.put((ICondition) t, Boolean.valueOf(negative));
	}

	public void addOr(IClausable t, boolean negative) {
		this.ors.put((ICondition) t, Boolean.valueOf(negative));
	}

	public MeetClause<T, C> clone() {
		return new MeetClause(this.subParser);
	}

	public void addApend(IClausable t, boolean negative) {
	}
}
