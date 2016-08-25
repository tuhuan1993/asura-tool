package com.asura.tools.util;

import static org.junit.Assert.*;

import org.junit.Test;

import com.asura.tools.util.collection.IWeightable;
import com.asura.tools.util.collection.TopList;

public class TopListTest {

	@Test
	public void test() {
		TopList<TT> list=new TopList<>(100);
		list.add(new TT(22d));
		list.add(new TT(44d));
		list.add(new TT(66d));
		list.add(new TT(88d));
		list.add(new TT(11d));
		for(TT t:list.getList()){
			System.out.println(t.getVal());
		}
	}
	
	public static class TT implements IWeightable{
		
		private double value;
		
		public TT(double val){
			value=val;
		}

		public double getVal(){
			return value;
		}
		
		@Override
		public double getWeight() {
			return value;
		}

		@Override
		public Object get() {
			return this;
		}
		
	}

}
