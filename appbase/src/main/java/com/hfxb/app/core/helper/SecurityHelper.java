package com.hfxb.app.core.helper;

import com.hfxb.app.core.common.Constants;
import com.jfinal.core.Controller;
import org.apache.commons.lang.StringUtils;

import java.util.List;


public class SecurityHelper {

	/**
	 * 
	 * @return
	 */
	public static boolean allowUpdate() {
		return true;
		//if current subject has not authenticated, return false
		/*if(!SecurityUtils.getSubject().isAuthenticated()) {
			return false;
		}
		List<Object> roles = Constants.config.getList("update.roles");
		for(Object role : roles) {
			if(SecurityUtils.getSubject().hasRole(String.valueOf(role))) {
				return true;
			}
		}
		return false;*/
	}
	
	/**
	 * if the request has not the right referer, do nothing
	 * @param c
	 * @return
	 */
	public static boolean allowFrom(Controller c) {
		String referer = c.getRequest().getHeader("Referer");
		String domain = getRootDomain(referer);
		if(StringUtils.isBlank(domain)) {
			return false;
		}
		List<Object> allowDomains = Constants.config.getList("allow.domain");
		for(Object d : allowDomains) {
			if(domain.endsWith(String.valueOf(d).trim())) {
				return true;
			}
		}
		return false;
	}
	
	private static String getRootDomain(String s) {
		if(StringUtils.isBlank(s)) {
			return "";
		} else if(s.indexOf("://") > 0) {
			int start = s.indexOf("://") + 3;
			int end = s.indexOf("/", start);
			// end > start 说明referer中包含/， 否则referer就是域名
			if(end > start) {
				return s.substring(start, end);
			} else {
				return s.substring(start);
			}
		}
		return "";
	}
}
