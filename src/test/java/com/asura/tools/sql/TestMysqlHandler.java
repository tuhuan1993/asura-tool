package com.asura.tools.sql;

import static org.junit.Assert.*;

import org.junit.Test;

import com.asura.tools.data.DataRecord;
import com.asura.tools.data.mysql.MysqlHandler;
import com.asura.tools.util.math.NumberUtil;

public class TestMysqlHandler {

	@Test
	public void test() {
		DataRecord condition=new DataRecord();
		condition.AddField("taskdate", "2016-07-13");
		condition.AddField("name", "download-log-parse");
		DataRecord result=new MysqlHandler().getDataRecordByCondition("task_data_status", condition);
		System.out.println(result);
		if(result!=null){
			System.out.println(NumberUtil.getInt(result.getFieldValue("taskstatus")));
		}
	}

}
