package com.asura.tools.data.mongo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.asura.tools.data.DataIterator;
import com.asura.tools.data.DataRecord;
import com.asura.tools.data.IEditor;
import com.asura.tools.sql.DeleteSQL;
import com.asura.tools.sql.ISQL;
import com.asura.tools.sql.LimitSQL;
import com.asura.tools.sql.SelectSQL;
import com.asura.tools.sql.WhereSQL;
import com.asura.tools.util.StringUtil;
import com.asura.tools.util.cache.SimpleCache;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoOptions;
import com.mongodb.WriteConcern;

public class MongoHandler {
	private MongoConnection connection;
	private static SimpleCache<String, DBCollection> cache = new SimpleCache(100000);
	private static SimpleCache<String, Mongo> dbCache = new SimpleCache(10000);

	public MongoHandler(MongoConnection con) {
		this.connection = con;
	}

	public MongoHandler(String con) {
		this.connection = new MongoConnection(con);
	}

	public MongoHandler() {
		this.connection = MongoConnection.fromConfig();
	}

	public void close() {
		getMongo().close();
		dbCache.remove(this.connection.getServers());
	}

	public long getCount(String dbName, String table) {
		return getTable(dbName, table).getCount();
	}

	public DBCollection getCollection(String dbName, String table) {
		return getTable(dbName, table);
	}

	public long getCount(String dbName, SelectSQL sql) {
		DBCollection table = getTable(dbName, sql.getTables().getTable(0));

		DBCursor cursor = find(table, sql);

		int count = cursor.size();

		cursor.close();

		return count;
	}

	public void deleteTable(String dbName, String table) {
		getTable(dbName, table).drop();
	}

	public void deleteDataBase(String dbName) {
		getMongo().getDB(dbName).dropDatabase();
	}

	public String[] getTables(String dbName) {
		return ((String[]) getMongo().getDB(dbName).getCollectionNames().toArray(new String[0]));
	}

	public String[] getDbs() {
		return ((String[]) getMongo().getDatabaseNames().toArray(new String[0]));
	}

	public void clearEmptyDbs() {
		for (String db : getDbs())
			if (getMongo().getDB(db).getCollectionNames().size() == 0) {
				getMongo().dropDatabase(db);
				System.out.println("drop " + db);
			}
	}

	private Mongo getMongo() {
		if (!(dbCache.iscached(this.connection.getServers()))) {
			MongoOptions options = new MongoOptions();
			options.autoConnectRetry = true;
			options.connectionsPerHost = 1000;
			options.maxWaitTime = 5000;
			options.socketTimeout = 0;
			options.connectTimeout = 15000;
			options.threadsAllowedToBlockForConnectionMultiplier = 5000;
			Mongo mongo = new Mongo(this.connection.parse(), options);
			dbCache.cache(this.connection.getServers(), mongo, 36000);
		}

		return ((Mongo) dbCache.get(this.connection.getServers()));
	}

	private DBCollection getTable(String dbName, String table) {
		String key = dbName + "-" + table;
		if (!(cache.iscached(key))) {
			cache.cache(key, getMongo().getDB(dbName).getCollection(table), 100);
		}

		return ((DBCollection) cache.get(key));
	}

	public long delete(String dbName, DeleteSQL sql) {
		DBCollection table = getTable(dbName, sql.getTables().getTable(0));
		DBObject ob = getWhere(sql.getWhere());
		if (ob == null) {
			long count = table.count();
			table.drop();
			return count;
		}
		return table.remove(ob, WriteConcern.SAFE).getN();
	}

	public List<DataRecord> selectList(String dbName, SelectSQL sql) {
		List list = new ArrayList();
		DataIterator it = select(dbName, sql);
		while (it.hasNext()) {
			list.add((DataRecord) it.next());
		}
		it.close();

		return list;
	}

	private DBObject getWhere(WhereSQL sql) {
		BasicDBList list = new BasicDBList();
		for (ISQL where : sql.getConditions().getFieldMap()) {
			DBObject ob = MongoConverter.convert(where);
			if (ob != null) {
				list.add(ob);
			}
		}

		if (list.size() > 0) {
			return new BasicDBObject("$and", list);
		}
		return null;
	}

	public DataIterator<DataRecord> select(String dbName, SelectSQL sql) {
		return select(dbName, sql, 100000);
	}

	private DBCursor find(DBCollection table, SelectSQL sql) {
		DBCursor cursor = null;
		DBObject ob = getWhere(sql.getWhere());

		BasicDBObject keys = null;
		if ((sql.getFields() != null) && (sql.getFields().size() > 0)) {
			keys = new BasicDBObject();
			for (String f : sql.getFields().getFields()) {
				keys.put(f, Integer.valueOf(1));
			}

			keys.put("_id", Integer.valueOf(0));
		}

		cursor = table.find(ob, keys);

		if (sql.getLimit() != null) {
			LimitSQL limit = sql.getLimit();
			if (limit.getStart() > 0) {
				cursor = cursor.skip(limit.getStart());
			}

			if (limit.getCount() > 0) {
				cursor = cursor.limit(limit.getCount());
			}
		}

		if (sql.getOrderBy() != null) {
			BasicDBObject order = new BasicDBObject();
			for (String s : sql.getOrderBy().getFields().getFields()) {
				if (!(s.contains("desc")))
					order.put(s, Integer.valueOf(1));
				else {
					order.put(s.replace(" desc ", "").trim(), Integer.valueOf(-1));
				}
			}

			cursor = cursor.sort(order);
		}

		return cursor;
	}

	public DataIterator<DataRecord> select(final String dbName, final SelectSQL sql, final int fetchSize) {
		return new DataIterator<DataRecord>() {
			private Iterator<DBObject> it;
			private DBCursor cursor;

			{
				if (getMongo().getDatabaseNames().contains(dbName)) {
					DBCollection table = getTable(dbName, sql.getTables().getTable(0));
					cursor = find(table, sql);
					cursor.batchSize(fetchSize);
					it = cursor.iterator();
				} else {
					it = new EmptyDbIterator();
				}

			}

			public void close() {
				if (this.cursor != null) {
					this.cursor.close();
					this.cursor = null;
				}

				if (this.it != null)
					this.it = null;
			}

			public boolean hasNext() {
				return this.it.hasNext();
			}

			public DataRecord next() {
				DataRecord dr = new DataRecord();
				DBObject ob = (DBObject) this.it.next();
				for (String key : ob.keySet()) {
					if (!(key.equals("_id"))) {
						if ((ob.containsField(key)) && (ob.get(key) != null))
							dr.AddField(key, ob.get(key));
						else {
							dr.AddField(key, "");
						}
					}
				}
				return dr;
			}

			public void reset() {
				if (!(this.it instanceof EmptyDbIterator))
					this.it = this.cursor.iterator();
			}
		};
	}

	public IEditor getEditor(String db, String tableName, String[] keys) {
		return getEditor(db, tableName, keys, null);
	}

	public IEditor getEditor(final String db, final String tableName, final String[] keys, final String[] index) {
		return new IEditor() {
			private DBCollection table;
			{
				table = getTable(db, tableName);
				if (index != null && index.length > 0) {
					String as2[];
					int j = (as2 = index).length;
					for (int i = 0; i < j; i++) {
						String in = as2[i];
						table.ensureIndex(new BasicDBObject(in, Integer.valueOf(1)));
					}
				}
			}

			public void addRecord(DataRecord dr) {
				addRecord(dr, false);
			}

			private DBObject getObject(DataRecord dr) {
				DBObject ob = new BasicDBObject();
				for (String f : dr.getAllFields()) {
					Object obj = dr.getFieldObject(f);
					if (obj != null) {
						ob.put(f, obj);
					}
				}

				ob.put("_id", getKey(dr));

				return ob;
			}

			private String getKey(DataRecord dr) {
				List list = new ArrayList();
				for (String key : keys) {
					list.add(dr.getFieldValue(key));
				}

				String key = StringUtil.getStringFromStrings(list, "어");

				return key.substring(0, Math.min(254, key.length()));
			}

			public void addRecord(DataRecord dr, boolean override) {
				DBObject ob = getObject(dr);
				if (override)
					this.table.save(ob);
				else
					this.table.insert(new DBObject[] { ob });
			}

			public void addRecords(List<DataRecord> drs) {
				addRecords(drs, false);
			}

			public void addRecords(List<DataRecord> drs, boolean override) {
				if (override) {
					for (DataRecord dr : drs)
						this.table.save(getObject(dr));
				} else {
					List list = new ArrayList();
					for (DataRecord dr : drs) {
						list.add(getObject(dr));
					}

					this.table.insert(list);
				}
			}

			public void begineTransaction() {
			}

			public void commit() {
			}

			public boolean containsRecord(DataRecord dr) {
				String key = getKey(dr);
				return (this.table.find(new BasicDBObject("_id", key)).size() > 0);
			}

			public void deleteRecord(DataRecord dr) {
				String key = getKey(dr);
				this.table.remove(new BasicDBObject("_id", key));
			}

			public void deleteRecords(List<DataRecord> drs) {
				for (DataRecord dr : drs)
					deleteRecord(dr);
			}

			public void processRecord(DataRecord dr) {
			}

			public void updateRecord(DataRecord dr) {
				addRecord(dr, true);
			}

			public void execute(String sql) {
			}
		};
	}
}
