package com.asura.tools.task;

import com.asura.tools.data.DataRecord;

public class DTaskContext {
	
	private DataRecord data=new DataRecord();
	
	public String[] getAllFields() {
		return data.getAllFields();
	}

	public String getFieldValue(String field) {
		return data.getFieldValue(field);
	}

	public Object getFieldObject(String field) {
		return data.getFieldObject(field);
	}

	public void deleteField(String field) {
		data.deleteField(field);
	}

	public boolean containsField(String field) {
		return data.containsField(field);
	}

	public void AddField(String field, String value) {
		data.AddField(field, value);
	}

	public void AddField(String field, Object value) {
		data.AddField(field, value);
	}

}
