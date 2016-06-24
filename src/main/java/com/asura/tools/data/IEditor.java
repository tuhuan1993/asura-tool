package com.asura.tools.data;

import java.util.List;

public interface IEditor {
	public void begineTransaction();

	public void commit();

	public void execute(String paramString);

	public void updateRecord(DataRecord paramDataRecord);

	public void addRecord(DataRecord paramDataRecord);

	public void addRecords(List<DataRecord> paramList);

	public void addRecords(List<DataRecord> paramList, boolean paramBoolean);

	public void addRecord(DataRecord paramDataRecord, boolean paramBoolean);

	public void deleteRecord(DataRecord paramDataRecord);

	public void deleteRecords(List<DataRecord> paramList);

	public void processRecord(DataRecord paramDataRecord);

	public boolean containsRecord(DataRecord paramDataRecord);
}
