package com.asura.tools.annotation.handler.rchandler;

import org.junit.Test;

import com.asura.tools.annotation.parser.AnnotationParserFactoryBuilder;
import com.asura.tools.data.DataRecord;
import com.asura.tools.handler.rchandler.DefaultResChainHandler;
import com.asura.tools.handler.rchandler.ResChainHandler;
import com.asura.tools.handler.rchandler.ResChainHandlerLog;

public class TestRChandlerAnnotation {

	@Test
	public void test() throws Exception {
		
		RCHandlerAnnotationParserFactory factory=new AnnotationParserFactoryBuilder.Builder<RCHandlerAnnotationParserFactory>("com.asura.tools.annotation.handler").build(RCHandler.class, new RCHandlerAnnotationParserFactory());
		DataRecord record=factory.getResChainHandler("shendiao").process(new DataRecord(),null);
		for(String field:record.getAllFields()){
			System.out.println(field+":"+record.getFieldValue(field));
		}
	}
	
	@RCHandler(group = "shendiao", name="niefei", desc="test case")
	public static class AnnotationTest extends DefaultResChainHandler{

		@Override
		public DataRecord handle(DataRecord record, ResChainHandlerLog log) {
			System.out.println("call niefei");
			record.AddField("0", "niefei");
			return record;
		}
		
	}
	
	@RCHandler(group = "shendiao", name="niefei1", desc="test case1")
	public static class AnnotationTest1 extends DefaultResChainHandler{

		@Override
		public DataRecord handle(DataRecord record, ResChainHandlerLog log) {
			System.out.println("call niefei1");
			record.AddField("1", "niefei1");
			isContinue=false;
			return record;
		}
		
	}
	
	@RCHandler(group = "shendiao", name="niefei2", desc="test case2", prehandler="niefei1")
	public static class AnnotationTest2 extends DefaultResChainHandler{

		@Override
		public DataRecord handle(DataRecord record, ResChainHandlerLog log) {
			System.out.println("call niefei2");
			record.AddField("2", "niefei2");
			return record;
		}
		
	}
	
	@RCHandler(group = "shendiao", name="niefei3", desc="test case3", prehandler="niefei1")
	public static class AnnotationTest3 extends DefaultResChainHandler{

		@Override
		public DataRecord handle(DataRecord record, ResChainHandlerLog log) {
			System.out.println("call niefei3");
			record.AddField("3", "niefei3");
			return record;
		}
		
	}
	
	@RCHandler(group = "shendiao", name="niefei4", desc="test case4", prehandler="niefei2")
	public static class AnnotationTest4 extends DefaultResChainHandler{

		@Override
		public DataRecord handle(DataRecord record, ResChainHandlerLog log) {
			System.out.println("call niefei4");
			record.AddField("4", "niefei4");
			setContinue(false);
			return record;
		}
		
	}
	
	@RCHandler(group = "shendiao", name="niefei5", desc="test case5", prehandler="niefei4")
	public static class AnnotationTest5 extends DefaultResChainHandler{

		@Override
		public DataRecord handle(DataRecord record, ResChainHandlerLog log) {
			System.out.println("call niefei5");
			record.AddField("5", "niefei5");
			return record;
		}
		
	}
	

}
