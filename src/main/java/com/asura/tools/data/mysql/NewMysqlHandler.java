package com.asura.tools.data.mysql;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.asura.tools.data.DataException;
import com.asura.tools.data.DataIterator;
import com.asura.tools.data.DataRecord;
import com.asura.tools.data.IEditor;
import com.asura.tools.util.ExceptionUtil;

public class NewMysqlHandler {
	private Connection connection;
	private ConnectionInformation ci;
	private static HashMap<String, String[]> keyCache = new HashMap();

	public NewMysqlHandler(ConnectionInformation ci) {
		this.ci = ci;
		this.connection = MysqlConnection.getInstance(ci);
	}

	public Connection getConnection() {
		return this.connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public NewMysqlHandler(ConnectionInformation ci, boolean newconnect) {
		this.ci = ci;
		if (!(newconnect))
			this.connection = MysqlConnection.getInstance(ci);
		else
			this.connection = MysqlConnection.getNewInstance(ci);
	}

	public void execute(String sql) {
		PreparedStatement ps = null;
		try {
			ps = this.connection.prepareStatement(sql, 1008);
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			throw new DataException("Failed to execute sql '" + sql + "'" + ExceptionUtil.getExceptionContent(e));
		} finally {
			if (ps != null)
				try {
					ps.close();
				} catch (SQLException localSQLException1) {
				}
		}
	}

	public void commit() throws SQLException {
		if (this.connection != null)
			this.connection.commit();
	}

	public void setAutoCommit(boolean autoCommit) throws SQLException {
		if (this.connection != null)
			this.connection.setAutoCommit(autoCommit);
	}

	public DataIterator<DataRecord> iterator(final String sql) {
		return new DataIterator<DataRecord>() {
			private ResultSet resultSet;
			private Statement ps;
			private ResultSetMetaData meta;

			{
				initial();
			}

			private void initial() {
				try {
					this.ps = NewMysqlHandler.this.connection.createStatement(1005, 1007);

					this.resultSet = this.ps.executeQuery(sql);
					this.resultSet.setFetchSize(100);
					this.meta = this.resultSet.getMetaData();
				} catch (Exception e) {
					close();
					throw new DataException(
							"Failed to initial sql '" + sql + "'\n" + ExceptionUtil.getExceptionContent(e));
				}
			}

			public boolean hasNext() {
				try {
					boolean has = this.resultSet.next();
					this.resultSet.previous();
					return has;
				} catch (Exception e) {
					throw new DataException(
							"Failed to get whether there is next record.\n" + ExceptionUtil.getExceptionContent(e));
				}
			}

			public DataRecord next() {
				try {
					this.resultSet.next();
					DataRecord dr = new DataRecord();
					for (int i = 1; i <= this.meta.getColumnCount(); ++i) {
						dr.AddField(this.meta.getColumnLabel(i),
								String.valueOf(this.resultSet.getObject(this.meta.getColumnLabel(i))));
					}
					return dr;
				} catch (Exception e) {
					throw new DataException("Failed to get next record.\n" + ExceptionUtil.getExceptionContent(e));
				}
			}

			public void close() {
				if (this.ps != null)
					try {
						this.ps.close();
					} catch (SQLException localSQLException) {
					}
				if (this.resultSet == null)
					return;
				try {
					this.resultSet.close();
				} catch (SQLException localSQLException1) {
				}
			}

			public void reset() {
				close();
				initial();
			}
		};
	}

	public IEditor editor(final String table) {
		return new IEditor() {
			public void begineTransaction() {
				try {
					NewMysqlHandler.this.connection.setAutoCommit(false);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			public void commit() {
				try {
					NewMysqlHandler.this.connection.commit();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			public void updateRecord(DataRecord dr) {
				if (dr.getAllFields().length > 0) {
					String sql = "update  " + table + NewMysqlHandler.this.getSetString(dr, table)
							+ NewMysqlHandler.this.getWhereString(dr, table);

					PreparedStatement ps = null;
					try {
						ps = NewMysqlHandler.this.connection.prepareStatement(sql, 1008);
						ps.executeUpdate();
						ps.close();
					} catch (SQLException e) {
						throw new DataException(
								"Failed to update record '" + sql + "'" + ExceptionUtil.getExceptionContent(e));
					} finally {
						if (ps != null)
							try {
								ps.close();
							} catch (SQLException localSQLException1) {
							}
					}
				}
			}

			public void addRecord(DataRecord dr, boolean override) {
				if ((containsRecord(dr)) && (override)) {
					deleteRecord(dr);
				}
				addRecord(dr);
			}

			public void addRecord(DataRecord dr) {
				if (dr.getAllFields().length > 0) {
					String sql = NewMysqlHandler.this.getInsertString(dr, table);

					PreparedStatement ps = null;
					try {
						ps = NewMysqlHandler.this.connection.prepareStatement(sql, 1008);
						ps.execute(sql);
						ps.close();
					} catch (SQLException e) {
						throw new DataException(
								"Failed to add record '" + sql + "'" + ExceptionUtil.getExceptionContent(e));
					} finally {
						if (ps != null)
							try {
								ps.close();
							} catch (SQLException localSQLException1) {
							}
					}
				}
			}

			public void deleteRecord(DataRecord dr) {
				if (dr.getAllFields().length > 0) {
					String sql = "delete from " + table + NewMysqlHandler.this.getWhereString(dr, table);

					PreparedStatement ps = null;
					try {
						ps = NewMysqlHandler.this.connection.prepareStatement(sql, 1008);
						ps.execute(sql);
						ps.close();
					} catch (SQLException e) {
						throw new DataException(
								"Failed to delete record '" + sql + "'" + ExceptionUtil.getExceptionContent(e));
					} finally {
						if (ps != null)
							try {
								ps.close();
							} catch (SQLException localSQLException1) {
							}
					}
				}
			}

			public void processRecord(DataRecord dr) {
				switch (dr.getDataAction()) {
				case Add:
					addRecord(dr);
					break;
				case Delete:
					deleteRecord(dr);
					break;
				case Update:
					updateRecord(dr);
				}
			}

			public boolean containsRecord(DataRecord dr) {
				String sql = "select * from  " + table + NewMysqlHandler.this.getWhereString(dr, table);
				DataIterator it = NewMysqlHandler.this.iterator(sql);
				boolean contains = false;
				if (it.hasNext()) {
					contains = true;
				}
				it.close();

				return contains;
			}

			public void addRecords(List<DataRecord> drs) {
			}

			public void addRecords(List<DataRecord> drs, boolean override) {
			}

			public void deleteRecords(List<DataRecord> drs) {
			}

			public void execute(String sql) {
			}
		};
	}

	private String getValidValue(String value) {
		if (value == null) {
			return "";
		}
		return ((value.contains("\\'")) ? value.replace("\\'", "'").replace("'", "''").replace("\\", "")
				: value.replace("'", "''").replace("\\", ""));
	}

	private String getInsertString(DataRecord dr, String table) {
		if (dr.getAllFields().length > 0) {
			StringBuffer fields = new StringBuffer();
			for (String s : dr.getAllFields()) {
				fields.append(s + ",");
			}
			fields.deleteCharAt(fields.length() - 1);

			StringBuffer values = new StringBuffer();
			for (String s : dr.getAllFields()) {
				values.append("'" + getValidValue(dr.getFieldValue(s)) + "',");
			}
			values.deleteCharAt(values.length() - 1);

			return "insert ignore into " + table + "(" + fields.toString() + ") values(" + values.toString() + ")";
		}
		return "";
	}

	private String getWhereString(DataRecord dr, String table) {
		String[] ks = getPrimaryKeyMetaData(table);
		StringBuffer values = new StringBuffer();
		for (String s : ks) {
			values.append(s + "='" + getValidValue(dr.getFieldValue(s)) + "' and ");
		}

		if (values.length() == 0) {
			return "";
		}

		return " where " + values.toString().substring(0, values.length() - 4) + " ";
	}

	private String getSetString(DataRecord dr, String table) {
		StringBuffer values = new StringBuffer();
		for (String s : dr.getAllFields()) {
			values.append(s + "='" + getValidValue(dr.getFieldValue(s)) + "',");
		}

		if (values.length() == 0) {
			return "";
		}

		return " set " + values.toString().substring(0, values.length() - 1) + " ";
	}

	private String[] getPrimaryKeyMetaData(String tableName) {
		if (keyCache.containsKey(this.ci.getDbName() + tableName)) {
			return ((String[]) keyCache.get(this.ci.getDbName() + tableName));
		}
		ArrayList list = new ArrayList();
		try {
			DatabaseMetaData meta = this.connection.getMetaData();

			ResultSet rs = meta.getPrimaryKeys(null, null, tableName);
			while (rs.next()) {
				list.add(rs.getString(4));
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		keyCache.put(this.ci.getDbName() + tableName, (String[]) list.toArray(new String[0]));
		return ((String[]) list.toArray(new String[0]));
	}

	public boolean containsRecord(String table, DataRecord dr) throws SQLException {
		String sql = "select * from  " + table + getWhereString(dr, table);
		DataIterator it = iterator(sql);
		boolean contains = false;
		if (it.hasNext()) {
			contains = true;
		}
		it.close();

		return contains;
	}

	public DataRecord getRecordByKey(String table, DataRecord dr) {
		String sql = "select * from  " + table + getWhereString(dr, table);
		DataIterator it = iterator(sql);
		DataRecord result = null;
		if (it.hasNext()) {
			result = (DataRecord) it.next();
		}
		it.close();

		return result;
	}

	public void close() {
		try {
			if (this.connection != null)
				this.connection.close();
		} catch (SQLException localSQLException) {
		}
	}
}
