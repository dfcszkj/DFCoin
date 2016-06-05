package com.hfxb.app.core.common;

import com.hfxb.app.core.model.FakeModel;
import org.apache.commons.configuration.PropertiesConfiguration;

/**
 * 常量表
 */
public final class Constants {
	
	public static PropertiesConfiguration config;
	
	public static PropertiesConfiguration qqconfig;
	
	public static FakeModel fake;
	
	public static final String ADMIN_LOGIN_URL = "/admin/account/login";
	public static final String USER_LOGIN_URL = "/user/login";
	/**
	 * 校验码码
	 */
	public static final String VERIFY_CODE = "vcode";
	
	/**
	 * session中存储的当前用户key
	 */
	public static final String KEY_LOGIN_ACCOUNT = "current_user";
	
	public static final String FRONT_LOGIN_USER = "front_login_user";
	
	/**
	 * cookie中的区域设置
	 */
	public static final String COOKIE_LOCAL = "_local";
	
	/**
	 * I18N资源
	 */
	public static final String I18N_RES = "_res";
	
	/** 默认每次执行批处理数量 **/
	public static final Integer DEFAULT_BATCH_SIZE = 100;
	
	/**
	 * 目录最大分级
	 */
	public static final Integer MAX_CATEGORY_GRADE = 3;
	
	public static final String BLOCK_ORDER_ASC = "1";
	
	/** 默认分页大小 **/
	public static final Integer DEFAULT_PAGESIZE = 15;
	
	public static final String CACHE_KEY_FOREVER = "front.forever";
	public static final String CACHE_KEY_LONG = "front.long";
	public static final String CACHE_KEY_MID = "front.mid";
	public static final String CACHE_KEY_SHORT = "front.short";
	
}
