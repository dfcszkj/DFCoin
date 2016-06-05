package com.hfxb.app.core.enums;

import java.util.HashMap;
import java.util.Map;

public enum RoleEnum {

	MANAGER("0", "超级管理员"),
	REGISTER_USER("1", "注册用户"),
	GUEST("2", "访客");
	
	private String code;
	private String desc;
	
	private RoleEnum(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}
	
	public static RoleEnum parseEnum(String c){
		for(RoleEnum p : values()){
			if(p.getCode() == c) {
				return p;
			}
		}
		return null;
	}
	
	public static Map<String,String> toMap() {
		Map<String,String> map = new HashMap<String,String>();
		for(RoleEnum s : values()){
			map.put(s.getCode(), s.getDesc());
		}
		return map;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

}
