package com.hfxb.app.core.enums;

import java.util.HashMap;
import java.util.Map;

public enum ParamTypeEnum {
	
	GLOBAL("global", "全局参数");
	
	private String code;
	
	private String name;
	
	private ParamTypeEnum(String code, String name) {
		this.code = code;
		this.name = name;
	}
	
	public static ParamTypeEnum parseEnum(String c){
		for(ParamTypeEnum p : values()){
			if(p.getCode().equalsIgnoreCase(c)) {
				return p;
			}
		}
		return null;
	}
	
	public static Map<String,String> toMap() {
		Map<String,String> map = new HashMap<String,String>();
		for(ParamTypeEnum s : values()){
			map.put(s.getCode(), s.getName());
		}
		return map;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
