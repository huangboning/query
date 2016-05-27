package com.studio.query.util;

import java.util.HashMap;
import java.util.Map;

public class CacheUtil {
	private static Map<String, Object> objectCacheFactory = new HashMap<String, Object>();
	private static Map<String, String> stringCacheFactory = new HashMap<String, String>();

	public static void putCacheString(String key, String value) {
		stringCacheFactory.put(key, value);
	}

	public static String getCacheString(String key) {
		return stringCacheFactory.get(key);
	}

	public static void putCacheObject(String key, Object value) {
		objectCacheFactory.remove(key);
		objectCacheFactory.put(key, value);
	}

	public static Object getCacheObject(String key) {
		return objectCacheFactory.get(key);
	}
}
