package com.hfxb.app.core.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {

private static final String DEFAULT_PATTERN = "yyyy-MM-dd HH:mm:ss";
	
	/** 
     * @return 获得当前Calendar 
     */  
    public static Calendar getCalendar(){  
        return Calendar.getInstance();  
    }  
    /** 
     * @return 获得今年 
     */  
    public static int getThisYear(){  
        return getCalendar().get(Calendar.YEAR);  
    }     /**
     * @return 获得今年
     */
    public static int getThisDay(){
        return getCalendar().get(Calendar.DAY_OF_MONTH);
    }
    /** 
     * @return 获得本月 
     */  
    public static int getThisMonth(){  
        return getCalendar().get(Calendar. MONTH)+1;  
    }  
    /** 
     * @return 获得当前时间 
     */  
    public static Date getNow(){  
        return getCalendar().getTime(); 
    }
    
    public static String getNow(String pattern) {
    	return new SimpleDateFormat(pattern).format(getNow());
    }
    
    public static String format(Date date, String pattern) {
    	if(StringUtils.isBlank(pattern)) {
    		pattern = DEFAULT_PATTERN;
    	}
    	return new SimpleDateFormat(pattern).format(date);
    }
    
    /**
     * format timestamp 
     * @param timestamp
     * @param pattern
     * @return
     */
    public static String format(Long timestamp, String pattern) {
    	return format(new Date(timestamp), pattern);
    } 
    
}
