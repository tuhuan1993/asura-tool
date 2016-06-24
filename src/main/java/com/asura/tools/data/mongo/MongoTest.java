package com.asura.tools.data.mongo;

import java.util.ArrayList;
import java.util.List;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;

public class MongoTest {
	public static void main(String[] args) throws Exception {
		Mongo mongo = new Mongo();
		DB db = mongo.getDB("test");

		DBCollection users = db.getCollection("users");
		add(users);
	}

	private static void queryAll(DBCollection users) {
		long start = System.currentTimeMillis();
		System.out.println("查询users的所有数据：");

		DBCursor cur = users.find();
		int i = 0;
		while (cur.hasNext()) {
			System.out.println(cur.next());
			if (i++ % 10000 == 0) {
				System.out.println(i);
			}
		}

		System.out.println("time:" + (System.currentTimeMillis() - start));
	}

	public static void add(DBCollection users) {
		users.drop();

		queryAll(users);
		System.out.println("count: " + users.count());

		DBObject user = new BasicDBObject();
		user.put("name", "hoojo");
		user.put("age", Integer.valueOf(24));

		user.put("sex", "男");
		System.out.println(users.save(user).getN());

		List list = new ArrayList();
		for (int i = 0; i < 50; ++i) {
			DBObject ob = new BasicDBObject();
			ob.put("_id", "user" + i);
			ob.put("name", "user" + i);
			ob.put("age", String.valueOf(i));
			ob.put("url", "fjioeofwjfoiewjfoiewjfoiewjfoisjfoiwjefoijewoifjwifewj" + i);

			if (i % 1000 == 0) {
				System.out.println("add:" + i);
			}
			users.insert(new DBObject[] { ob });
		}

		users.insert(list);

		System.out.println("count: " + users.count());
		queryAll(users);
		seach(users);
	}

	private static void seach(DBCollection users) {
		DBObject query = new BasicDBObject();
		BasicDBList list = new BasicDBList();
		list.add(new BasicDBObject("age", new BasicDBObject("$gt", Integer.valueOf(11))));
		list.add(new BasicDBObject("name", new BasicDBObject("$gt", "user40")));
		query.put("$or", list);

		DBCursor cursor = users.find(query).sort(new BasicDBObject("age", Integer.valueOf(-1))).skip(10).limit(10);
		System.out.println("search");
		while (cursor.hasNext())
			System.out.println(cursor.next());
	}
}
