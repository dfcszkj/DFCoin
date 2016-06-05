package com.hfxb.app.core.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

/**
 * FIXME: 暂时通过这个方法将dao操作集中到实体类中操作， 以后通过反射对数据库进行映射
 * @author qq
 *
 */
public class ParamUtils {
	
	private static final Logger logger = LoggerFactory.getLogger(ParamUtils.class);

	public static Integer getInt(HttpServletRequest request, String key) {
		String obj = request.getParameter(key);
		if(!Pattern.matches("\\d*", obj)) {
			logger.error("无法获取Integer值， key：{}, value:{}", key, obj);
			return null;
		}
		return Integer.parseInt(obj);
	}
	
	public static Long getLong(HttpServletRequest request, String key) {
		String obj = request.getParameter(key);
		if(!Pattern.matches("\\d*", obj)) {
			logger.error("无法获取Long值， key：{}, value:{}", key, obj);
			return null;
		}
		return Long.parseLong(obj);
	}
	
	public static String get(HttpServletRequest request, String key) {
		return getString(request, key);
	}
	
	public static String getString(HttpServletRequest request, String key) {
		return request.getParameter(key);
	}
	
	public static Boolean getBoolean(HttpServletRequest request, String key) {
		String value = get(request, key);
		return "true".equalsIgnoreCase(value);
	}
	
	/**
	 * change Map<String, String[]> to Map<String,String>
	 * 	String[] is the checkbox parameters
	 * @param paraMap
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static Map<String,String> translateMap(Map<String, String[]> paraMap) {
	    // 返回值Map
	    Map<String,String> returnMap = new HashMap<String,String>();
	    Iterator<Entry<String, String[]>> entries = paraMap.entrySet().iterator();
	    Entry entry;
	    String name = "";
	    String value = "";
	    while (entries.hasNext()) {
	        entry = (Entry) entries.next();
	        name = (String) entry.getKey();
	        Object valueObj = entry.getValue();
	        if(null == valueObj){
	            value = "";
	        }else if(valueObj instanceof String[]){
	            String[] values = (String[])valueObj;
	            for(int i=0;i<values.length;i++){
	                value = values[i] + ",";
	            }
	            value = value.substring(0, value.length()-1);
	        }else{
	            value = valueObj.toString();
	        }
	        returnMap.put(name, value);
	    }
	    return returnMap;
	}
	
}
