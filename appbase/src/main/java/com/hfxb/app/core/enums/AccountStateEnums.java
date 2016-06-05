package com.hfxb.app.core.enums;

import com.hfxb.app.core.common.Constants;

import java.util.HashMap;
import java.util.Map;

public enum AccountStateEnums {

	UNACTIVE(1, "普通用戶"),
	BAODAN(2, "报单中心"),
	TUANDUI(3, "经理");
	
	private Integer code;
	private String desc;
	
	private AccountStateEnums(Integer code, String desc) {
		this.code = code;
		this.desc = desc;
	}


	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getDesc() {
		String[] s= Constants.config.getStringArray("system.userState");
		return s[code-1];
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
	public static Map<Integer,String> toMap() {
		Map<Integer,String> map = new HashMap<Integer,String>();
		String[] states= Constants.config.getStringArray("system.userState");
		for(AccountStateEnums s : values()){
			map.put(Integer.valueOf(s.getCode()), states[s.getCode()-1]);
		}
		return map;
	}
}
