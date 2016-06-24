package com.asura.tools.data.mysql;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.asura.tools.data.DataRecord;
import com.asura.tools.data.IEditor;

public class MysqlHandler {
	private Connection connection;

	public MysqlHandler(ConnectionInformation ci) {
		this.connection = MysqlConnection.getInstance(ci);
	}

	public MysqlHandler(ConnectionInformation ci, boolean newconnect) {
		if (!(newconnect))
			this.connection = MysqlConnection.getInstance(ci);
		else
			this.connection = MysqlConnection.getNewInstance(ci);
	}

	public Iterator<DataRecord> iterator(final String sql) {
		return new Iterator<DataRecord>() {
			private ResultSet resultSet;
			private Statement ps;
			private ResultSetMetaData meta;

			{
				try {
					ps = connection.createStatement();
					resultSet = ps.executeQuery(sql);
					resultSet.setFetchSize(100);
					meta = resultSet.getMetaData();
				} catch (SQLException e) {
					e.printStackTrace();
					if (ps != null)
						try {
							ps.close();
						} catch (SQLException e1) {
							e.printStackTrace();
						}
				}

			}

			public boolean hasNext() {
				try {
					boolean has = this.resultSet.next();
					this.resultSet.previous();
					if (!(has)) {
						this.ps.close();
					}
					return has;
				} catch (SQLException e) {
					try {
						if (this.ps != null)
							this.ps.close();
					} catch (SQLException localSQLException1) {
					}
				}
				return false;
			}

			public DataRecord next() {
				try {
					this.resultSet.next();
					DataRecord dr = new DataRecord();
					for (int i = 1; i <= this.meta.getColumnCount(); ++i) {
						dr.AddField(this.meta.getColumnName(i),
								String.valueOf(this.resultSet.getObject(this.meta.getColumnName(i))));
					}
					return dr;
				} catch (Exception e) {
					e.printStackTrace();
				}

				return null;
			}

			public void remove() {
				if (this.ps == null)
					return;
				try {
					this.ps.close();
				} catch (SQLException localSQLException) {
				}
			}
		};
	}

	public IEditor editor(final String table) {
		return new IEditor() {
			public void begineTransaction() {
				try {
					MysqlHandler.this.connection.setAutoCommit(false);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			public void commit() {
				try {
					MysqlHandler.this.connection.commit();
					MysqlHandler.this.connection.setAutoCommit(true);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			public void updateRecord(DataRecord dr) {
				if (dr.getAllFields().length > 0) {
					String sql = "update  " + table + MysqlHandler.this.getSetString(dr, table)
							+ MysqlHandler.this.getWhereString(dr, table);

					PreparedStatement ps = null;
					try {
						ps = MysqlHandler.this.connection.prepareStatement(sql, 1008);
						ps.executeUpdate();
						ps.close();
					} catch (SQLException e) {
						e.printStackTrace();
					} finally {
						if (ps != null)
							try {
								ps.close();
							} catch (SQLException localSQLException2) {
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
					String sql = MysqlHandler.this.getInsertString(dr, table);

					PreparedStatement ps = null;
					try {
						ps = MysqlHandler.this.connection.prepareStatement(sql, 1008);
						ps.execute(sql);
						ps.close();
					} catch (SQLException e) {
						e.printStackTrace();
					} finally {
						if (ps != null)
							try {
								ps.close();
							} catch (SQLException localSQLException2) {
							}
					}
				}
			}

			public void deleteRecord(DataRecord dr) {
				if (dr.getAllFields().length > 0) {
					String sql = "delete from " + table + MysqlHandler.this.getWhereString(dr, table);

					PreparedStatement ps = null;
					try {
						ps = MysqlHandler.this.connection.prepareStatement(sql, 1008);
						ps.execute(sql);
						ps.close();
					} catch (SQLException e) {
						e.printStackTrace();
					} finally {
						if (ps != null)
							try {
								ps.close();
							} catch (SQLException localSQLException2) {
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
				String sql = "select * from  " + table + MysqlHandler.this.getWhereString(dr, table);
				Iterator it = MysqlHandler.this.iterator(sql);
				boolean contains = false;
				if (it.hasNext()) {
					contains = true;
				}
				it.remove();

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

		return value.replace("'", "\\'").replace("\"", "\\\"");
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

			return "insert into " + table + "(" + fields.toString() + ") values(" + values.toString() + ")";
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

		return ((String[]) list.toArray(new String[0]));
	}

	public boolean containsRecord(String table, DataRecord dr) {
		String sql = "select * from  " + table + getWhereString(dr, table);
		Iterator it = iterator(sql);
		boolean contains = false;
		if (it.hasNext()) {
			contains = true;
		}
		it.remove();

		return contains;
	}

	public void close() {
		try {
			if (this.connection != null)
				this.connection.close();
		} catch (SQLException localSQLException) {
		}
	}
}
