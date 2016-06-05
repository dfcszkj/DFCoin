package com.hfxb.app.core.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtils {
	
	
	/**
	 * 
	 * <p>批量抓去</p>
	 * @param content	目录页源码
	 * @param rule		目录采集规则
	 * @return			解析后的目录集合, 不进行字符串替换
	 */
	public static List<String> getValues(String content, String pattern, Integer flag) {
		List<String> valueList = new ArrayList<String>();
		if(StringUtils.isNotBlank(pattern)) {
			Pattern p = null;
			if(flag == null) {
				p = Pattern.compile(pattern);
			} else {
				p = Pattern.compile(pattern, flag);
			}
	        Matcher m = p.matcher(content);
	        try {
				while (m.find()) {
					for (int i = 1 ; i <= m.groupCount(); i++) {
		                valueList.add(m.group(i));
		            }
				}
			} catch (Exception e) {
				valueList.add(String.valueOf(m.matches()));
			}
		}
        return valueList;
    }
	
	public static List<String> getValues(String content, String pattern) {
		return getValues(content, pattern, null);
	}

	/**
	 * 
	 * <p>根据正则表达式解析传入的内容</p>
	 * @param content	需要解析的字符串
	 * @param pattern	解析用的正则表达式
	 * @return
	 */
	public static boolean match(String content, String pattern, Integer flag) {
		
    	if(StringUtils.isNotEmpty(pattern)) {
    		// 对应JDK的BUG，把常用的((.|\n)+?)换成([\s\S]*?)
            pattern = pattern.replace("(.|\\n)", "[\\s\\S]");
            Pattern p = null;
			if(flag == null) {
				p = Pattern.compile(pattern);
			} else {
				p = Pattern.compile(pattern, flag);
			}
            Matcher m = p.matcher(content);
            try {
				if (m.find()) {
					return true;
				}
				return false;
			} catch (Exception e) {
				return false;
			}
    	}
        
        return false;
    }
	
	public static boolean match(String content, String pattern) {
		return match(content, pattern, null);
    }
	
	/**
	 * 
	 * <p>根据正则表达式解析传入的内容</p>
	 * @param content	需要解析的字符串
	 * @param pattern	解析用的正则表达式
	 * @return
	 */
	public static String getValue(String content, String pattern, Integer flag) {
		
		String result = null;
		
    	if(StringUtils.isNotEmpty(pattern)) {
    		// 对应JDK的BUG，把常用的((.|\n)+?)换成([\s\S]*?)
            pattern = pattern.replace("(.|\\n)", "[\\s\\S]");
            Pattern p = null;
            if(flag == null) {
            	p = Pattern.compile(pattern);
            } else {
            	p = Pattern.compile(pattern, flag);
            }
            Matcher m = p.matcher(content);
            try {
				if (m.find()) {
					result = m.group(1);
				}
			} catch (Exception e) {
				result = String.valueOf(m.matches());
			}
    	}
        
        return result;
    }
	
	public static String getValue(String content, String pattern) {
		return getValue(content, pattern, null);
    }
	
	
}
