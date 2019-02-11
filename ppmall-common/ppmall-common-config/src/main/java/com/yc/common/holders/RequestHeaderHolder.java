package com.yc.common.holders;

import java.util.HashMap;
import java.util.Map;

public class RequestHeaderHolder {
	private static final ThreadLocal<Map<String, Object>> headerHolder = new ThreadLocal<Map<String, Object>>() {
		/**
		 * ThreadLocal没有被当前线程赋值时或当前线程刚调用remove方法后调用get方法，返回此方法值
		 */
		@Override
		protected Map<String, Object> initialValue() {
			return new HashMap<>();
		}
	};


	public static void clearCurrentHeader() {
		headerHolder.remove();
	}
	
	public static void setCurrentHeader(Map<String, Object> header) {
		headerHolder.set(header);
	}
	
	public static Map<String, Object> getCurrentHeader() {
		return headerHolder.get();
	}
}
