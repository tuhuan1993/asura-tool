package com.asura.tools.util;

import com.alibaba.fastjson.JSONObject;

public class JsonUtil {

	public static String fromObject(Object obj) {
		return JSONObject.toJSONString(obj);
	}

	public static <T> T json2Object(String json, Class<T> clazz) {

		return JSONObject.parseObject(json, clazz);
	}
}
