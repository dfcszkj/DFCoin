package com.hfxb.app.core.utils;

import java.io.*;

public class StringUtils {
	
	private static final String PROTOCAL_SPLIT = "://";
	private static final String EMPTY = "";
	
	//a
	private static final Integer UPPER_CHAR_START = 65;
	//z
	private static final Integer UPPER_CHAR_END = 90;
	
	/**
     * <p>字符串是否为空 ("") 或 null.</p>
     *
     * <pre>
     * StringUtils.isEmpty(null)      = true
     * StringUtils.isEmpty("")        = true
     * StringUtils.isEmpty(" ")       = false
     * StringUtils.isEmpty("bob")     = false
     * StringUtils.isEmpty("  bob  ") = false
     * </pre>
     *
     * @param str  需要验证的字符串
     * @return <code>true</code> 字符串为空或者字符串对象为null
     */
    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    /**
     * <p>字符串是否为不空 ("") 或 不为null.</p>
     *
     * <pre>
     * StringUtils.isNotEmpty(null)      = false
     * StringUtils.isNotEmpty("")        = false
     * StringUtils.isNotEmpty(" ")       = true
     * StringUtils.isNotEmpty("bob")     = true
     * StringUtils.isNotEmpty("  bob  ") = true
     * </pre>
     *
     * @param str  需要验证的字符串
     * @return <code>true</code> 字符串不为空并且字符串对象不为null
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    /**
     * <p>字符串是否为空白, 空字符串("") 或者 null.</p>
     *
     * <pre>
     * StringUtils.isBlank(null)      = true
     * StringUtils.isBlank("")        = true
     * StringUtils.isBlank(" ")       = true
     * StringUtils.isBlank("bob")     = false
     * StringUtils.isBlank("  bob  ") = false
     * </pre>
     *
     * @param str  需要验证的字符串
     * @return <code>true</code> 字符串为空白, 空字符串("") 或者 null
     * @since 2.0
     */
    public static boolean isBlank(String str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if ((Character.isWhitespace(str.charAt(i)) == false)) {
                return false;
            }
        }
        return true;
    }

    /**
     * <p>字符串是否不为 (""), 不为null 并且不为 空白.</p>
     *
     * <pre>
     * StringUtils.isNotBlank(null)      = false
     * StringUtils.isNotBlank("")        = false
     * StringUtils.isNotBlank(" ")       = false
     * StringUtils.isNotBlank("bob")     = true
     * StringUtils.isNotBlank("  bob  ") = true
     * </pre>
     *
     * @param str  需要验证的字符串
     * @return <code>true</code> 字符串是否不为 (""), 不为null 并且不为 空白
     */
    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }
    
    /**
     * <p>Removes control characters (char &lt;= 32) from both
     * ends of this String returning an empty String ("") if the String
     * is empty ("") after the trim or if it is <code>null</code>.
     *
     * <p>The String is trimmed using {@link String#trim()}.
     * Trim removes start and end characters &lt;= 32.
     * To strip whitespace use {@link #stripToEmpty(String)}.</p>
     *
     * <pre>
     * StringUtils.trimToEmpty(null)          = ""
     * StringUtils.trimToEmpty("")            = ""
     * StringUtils.trimToEmpty("     ")       = ""
     * StringUtils.trimToEmpty("abc")         = "abc"
     * StringUtils.trimToEmpty("    abc    ") = "abc"
     * </pre>
     *
     * @param str  the String to be trimmed, may be null
     * @return the trimmed String, or an empty String if <code>null</code> input
     * @since 2.0
     */
    public static String trimToEmpty(String str) {
        return str == null ? EMPTY : str.trim();
    }
    
    /**
     * <p>Compares two Strings, returning <code>true</code> if they are equal.</p>
     *
     * <p><code>null</code>s are handled without exceptions. Two <code>null</code>
     * references are considered to be equal. The comparison is case sensitive.</p>
     *
     * <pre>
     * StringUtils.equals(null, null)   = true
     * StringUtils.equals(null, "abc")  = false
     * StringUtils.equals("abc", null)  = false
     * StringUtils.equals("abc", "abc") = true
     * StringUtils.equals("abc", "ABC") = false
     * </pre>
     *
     * @see String#equals(Object)
     * @param str1  the first String, may be null
     * @param str2  the second String, may be null
     * @return <code>true</code> if the Strings are equal, case sensitive, or
     *  both <code>null</code>
     */
    public static boolean equals(String str1, String str2) {
        return str1 == null ? str2 == null : str1.equals(str2);
    }

    /**
     * <p>Compares two Strings, returning <code>true</code> if they are equal ignoring
     * the case.</p>
     *
     * <p><code>null</code>s are handled without exceptions. Two <code>null</code>
     * references are considered equal. Comparison is case insensitive.</p>
     *
     * <pre>
     * StringUtils.equalsIgnoreCase(null, null)   = true
     * StringUtils.equalsIgnoreCase(null, "abc")  = false
     * StringUtils.equalsIgnoreCase("abc", null)  = false
     * StringUtils.equalsIgnoreCase("abc", "abc") = true
     * StringUtils.equalsIgnoreCase("abc", "ABC") = true
     * </pre>
     *
     * @see String#equalsIgnoreCase(String)
     * @param str1  the first String, may be null
     * @param str2  the second String, may be null
     * @return <code>true</code> if the Strings are equal, case insensitive, or
     *  both <code>null</code>
     */
    public static boolean equalsIgnoreCase(String str1, String str2) {
        return str1 == null ? str2 == null : str1.equalsIgnoreCase(str2);
    }

    /**
     * 
     * <p>将输入流转换成字符串</p>
     * @param inputStream	
     * @param charset	编码
     * @return String
     * @throws UnsupportedEncodingException
     */
    public static String stream2String(InputStream inputStream, String charset) throws UnsupportedEncodingException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, charset));
		StringBuilder sb = new StringBuilder();
		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + System.getProperty("line.separator"));
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}
    
	/**
	 * 
	 * <p>清除文本内容中的空白行</p>
	 * @param source
	 * @return
	 */
	public static String removeBlankLine(String source) {
		StringBuffer sb = new StringBuffer();
		StringReader reader = new StringReader(source);
		try {
			BufferedReader bufferedReader = new BufferedReader(reader);
            String line = null;
            while((line = bufferedReader.readLine()) != null){
            	 if(StringUtils.isNotBlank(line)) {
                	sb.append(line + System.getProperty("line.separator"));
                }
            }
            reader.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return sb.toString();
	}
	
	/**
	 * 
	 * <p>判断字符串数组中是否包含目标串</p>
	 * @param src
	 * @param dest
	 * @return
	 */
	public static boolean contains(String[] src, String dest) {
		if(src.length>0){
			for(String str : src) {
				if(str.equals(dest)){
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * 
	 * <p>获取完整的URL地址</p>
	 * @param baseUrl	目标网址
	 * 					一般为http://a.com
	 * @param destUrl	要抓取的页面地址
	 * 					可能为  http://a.com/b/c.html
	 * 					    /b/c.html
	 * @return	完整的抓取目标地址	如：http://a.com/b/c.html
	 */
	public static String getFullUrl(String baseUrl, String destUrl) {
		
		int index = destUrl.indexOf(PROTOCAL_SPLIT);
		//处理 http://a.com/b/c.html，获取不带协议的地址字符串a.com/b/c.html
		if(index>0) {
			destUrl = destUrl.substring(index + PROTOCAL_SPLIT.length(), destUrl.length());
		}
		//获取根域名后的地址/b/c.html
		index = destUrl.indexOf("/");
		if(index>=0) {
			destUrl = destUrl.substring(index, destUrl.length());
		}
		
		if(baseUrl.endsWith("/") && destUrl.startsWith("/")) {
			baseUrl = baseUrl.substring(0, baseUrl.length()-1);
		}
		destUrl = baseUrl + destUrl;
		return destUrl;
	}
	
	/**
	 * 优化jdk的indexOf方法
	 * @param src
	 * @param dest
	 * @return
	 */
	public static Integer indexOf(String src, String dest) {
		int result = -1;
		if(isNotBlank(src)) {
			result = src.indexOf(dest);
		}
		return result;
	}
	
	/**
	 * 给指定的字符串前补指定个数的0
	 * @param str	  要补0的字符串
	 * @param len    要补0的个数
	 * @return       补0后的字符串
	 */
	public static String addZeroBeforeStr(String str, int len){
		if(str == null ) str = "";
		
		if(len == 0) return str;
		
		StringBuffer zero = new StringBuffer();
		for(int i=0; i<len; i++){
			zero.append(0);
		}
		
		return zero.append(str).toString();
	}
	
	/**
	 * 添加SQL语句的IN查询中的问号如(?,?,?,?)
	 * @param len  问号的个数
	 * @return  带括号(?,?,?,?)
	 */
	public static String addSQLINQuestionMark(int len){
		StringBuffer sb = new StringBuffer();
		sb.append("(");
		
		for(int i = 0; i < len; i++){
			sb.append(" ? ,");
		}
		
		sb.setCharAt(sb.length() - 1, ' ');
		sb.append(")");
		
		return sb.toString();
	}
	
	/**
	 * subString, used for frontpage
	 * @param s
	 * @param start
	 * @param end
	 * @return
	 */
	public static String subString(String s,int start, int end) {
		if(s.length() < start || end < start) {
			return "";
		} else if(s.length()>=start && s.length()<=end) {
			return s.substring(start);
		} else {
			//s.length() > end
			return s.substring(start, end);
		}
	}
	
	public static String subString(String s,int start) {
		if(s.length() < start) {
			return "";
		} else {
			return s.substring(start);
		}
	}
	
	public static String getIntro(String s,int start, int end) {
		if(isNotBlank(s)) {
			s = s.replaceAll("<.+?>", "").trim();
		}
		return subString(s, start, end);
	}
	
	public static Boolean isInclude(String requestURI, String[] excludeURI) {
		if(StringUtils.isBlank(requestURI)) {
			return true;
		}
		for(String uri : excludeURI) {
			if(StringUtils.isNotBlank(uri) && requestURI.startsWith(uri)) {
				return true;
			}
		}
		return false;
	}
	
	public static Integer length(String text) {
		return isBlank(text) ? 0 : text.length();
	}
	
	/**
	 * generate a random string
	 * @param length
	 * @return
	 */
	public static String randomStr(int length) {
		StringBuffer sb = new StringBuffer();
		for(int i=0;i<length;i++) {
			sb.append((char) MathUtils.randomInt(UPPER_CHAR_START, UPPER_CHAR_END));
		}
		return sb.toString();
	}
}
