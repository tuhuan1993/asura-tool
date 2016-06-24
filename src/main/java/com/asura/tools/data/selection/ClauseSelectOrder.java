package com.asura.tools.data.selection;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.asura.tools.condition.ClauseParser;
import com.asura.tools.condition.IClausable;
import com.asura.tools.condition.IExpClause;
import com.asura.tools.data.selection.expression.ExpSelectOrder;

public class ClauseSelectOrder implements ISelectOrder, IExpClause {
	private static final long serialVersionUID = 8119849591462167518L;
	private List<ISelectOrder> orList;
	private List<ISelectOrder> andList;
	private List<ISelectOrder> appendList;

	public ClauseSelectOrder() {
		this.orList = new ArrayList();
		this.andList = new ArrayList();
		this.appendList = new ArrayList();
	}

	public static ClauseSelectOrder fromExpression(String exp) {
		return ((ClauseSelectOrder) new ClauseParser().parse(exp, new ExpSelectOrder(), new ClauseSelectOrder()));
	}

	public void addOrSelectOrder(ISelectOrder order) {
		this.orList.add(order);
	}

	public void addAndSelectOrder(ISelectOrder order) {
		this.andList.add(order);
	}

	public void addAppendSelectOrder(ISelectOrder order) {
		this.appendList.add(order);
	}

	public DataBlocks sort(DataBlock block)
  {
    DataBlocks result = new DataBlocks();

    if ((((this.andList == null) || (this.andList.size() == 0))) && 
      (((this.orList == null) || (this.orList.size() == 0))) && ((
      (this.appendList == null) || (this.appendList.size() == 0))))
      result.addDataBlock(block);
    if ((this.andList != null) && (this.andList.size() > 0)) {
      List<DataBlock> blocks = new ArrayList<DataBlock>();
      blocks.add(block);

      List temps = new ArrayList();
      for (ISelectOrder order : this.andList) {
        temps.clear();
        for (DataBlock bl : blocks) {
          DataBlocks dbs = order.sort(bl);
          for (DataBlock db : dbs.getBlocks()) {
            temps.add(db);
          }
        }
        if (temps.size() == 0) {
          return new DataBlocks();
        }
        blocks.clear();
        blocks.addAll(temps);
      }

      for (Iterator<DataBlock> it = blocks.iterator(); it.hasNext(); ) { 
    	  DataBlock bl = it.next();
    	  result.addDataBlock(bl);
      }
    }
    if ((this.orList != null) && (this.orList.size() > 0)) {
      DataBlocks orResult = new DataBlocks();
      for (Iterator<?> it = this.orList.iterator(); it.hasNext(); ) { 
    	  ISelectOrder order = (ISelectOrder)it.next();
          orResult = orResult.orMerge(order.sort(block));
      }

      if ((this.andList != null) && (this.andList.size() > 0))
        result = result.andMerge(orResult);
      else {
        result = orResult;
      }
    }

    if ((this.appendList != null) && (this.appendList.size() > 0)) {
      for (ISelectOrder order : this.appendList) {
        result = result.appendMerge(order.sort(block));
      }
    }

    result.removeEmptyBlokcs();

    return result;
  }

	public Set<String> getAllFeatures() {
		HashSet set = new HashSet();
		for (ISelectOrder order : this.andList) {
			set.addAll(order.getAllFeatures());
		}

		for (ISelectOrder order : this.orList) {
			set.addAll(order.getAllFeatures());
		}

		return set;
	}

	public List<ISelectOrder> getOrList() {
		return this.orList;
	}

	public void setOrList(List<ISelectOrder> orList) {
		this.orList = orList;
	}

	public List<ISelectOrder> getAndList() {
		return this.andList;
	}

	public void setAndList(List<ISelectOrder> andList) {
		this.andList = andList;
	}

	public String toString() {
		return SelectMethod.getXStream().toXML(this);
	}

	public void addAnd(IClausable t, boolean negative) {
		this.andList.add((ISelectOrder) t);
	}

	public void addOr(IClausable t, boolean negative) {
		this.orList.add((ISelectOrder) t);
	}

	public ClauseSelectOrder clone() {
		return new ClauseSelectOrder();
	}

	public void addApend(IClausable t, boolean negative) {
		this.appendList.add((ISelectOrder) t);
	}

}
