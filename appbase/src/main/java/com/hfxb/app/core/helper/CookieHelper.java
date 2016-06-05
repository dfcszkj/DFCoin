package com.hfxb.app.core.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.net.URLEncoder;

public class CookieHelper {
	
	private static final Logger logger = LoggerFactory.getLogger(CookieHelper.class);
	
	/**
     * 用户的cookie名
     */
    public static final String USER_COOKIE = "filentech.user.cookie";
    

	/**
     * 添加用户信息到Cookie
     * 
     * @param user
     *            用户信息
     * @return Cookie信息
     */

    public static Cookie addCookie(String username, String password) {
        try {
            Cookie cookie = new Cookie(USER_COOKIE, URLEncoder.encode(username, "UTF-8")
                    + "," + password);
            // 默认保持两年cookie保存两周
            cookie.setMaxAge(60 * 60 * 24 * 365);
            return cookie;
        } catch (Exception e) {
            logger.error(e.getMessage(),e );
        }
        return null;
    }
    
    public static void listCookie(HttpServletRequest request) {
    	Cookie[] cookies = request.getCookies();
    	if (cookies != null) {
            // 遍历cookie查找用户信息
            for (Cookie cookie : cookies) {
            	String value = cookie.getValue();
            	logger.debug(value);
            }
    	}
    }
	
}
