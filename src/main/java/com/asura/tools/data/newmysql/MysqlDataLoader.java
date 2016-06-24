package com.asura.tools.data.newmysql;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.asura.tools.data.DataRecord;
import com.asura.tools.sql.SelectSQL;
import com.asura.tools.timer.IntervalExecutor;
import com.asura.tools.util.StringUtil;

public class MysqlDataLoader {
	private HashMap<String, DataRecord> map;
	private IntervalExecutor loader;

	public MysqlDataLoader(MysqlHandler handler, SelectSQL sql, String[] keys) {
		this.map = new HashMap();
		load(handler, sql, keys);

		this.loader = getLoader(handler, sql, keys);
		this.loader.execute();
	}

	public MysqlDataLoader(MysqlHandler handler, SelectSQL sql, String[] keys, int seconds) {
		this.map = new HashMap();
		load(handler, sql, keys);

		this.loader = getLoader(handler, sql, keys);
		this.loader.setName("mysql loader " + sql.getTables().getTable(0));
		this.loader.setInterval(1000 * seconds);
		this.loader.start();
	}

	public DataRecord getData(String key) {
		return ((DataRecord) this.map.get(key));
	}

	public String[] getKeys() {
		return ((String[]) this.map.keySet().toArray(new String[0]));
	}

	public DataRecord[] getValues() {
		return ((DataRecord[]) this.map.values().toArray(new DataRecord[0]));
	}

	public void close() {
		if (this.loader != null) {
			this.loader.stop();
		}

		if (this.map != null) {
			this.map.clear();
			this.map = null;
		}
	}

	private void load(MysqlHandler handler, SelectSQL sql, String[] keys) {
		HashMap temp = new HashMap();
		List<DataRecord> list = handler.selectList(sql);
		for (DataRecord dr : list) {
			List keyList = new ArrayList();
			for (String key : keys) {
				keyList.add(dr.getFieldValue(key));
			}

			temp.put(StringUtil.getStringFromStrings(keyList, "|"), dr);
		}
		this.map = temp;
	}

	private IntervalExecutor getLoader(final MysqlHandler handler,final SelectSQL sql, final String[] keys) {
		return new IntervalExecutor() {
			public void execute() {
				MysqlDataLoader.this.load(handler, sql, keys);
			}
		};
	}
}
