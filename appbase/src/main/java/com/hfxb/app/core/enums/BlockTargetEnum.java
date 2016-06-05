package com.hfxb.app.core.enums;

import java.util.HashMap;
import java.util.Map;


public enum BlockTargetEnum {
	
	ALL("0", "全站"),
	INDEX("1", "首页"),
	LIST("2", "列表页"),
	ARTICLE("3", "内容页");
	
	private String code;
	
	private String name;
	
	private BlockTargetEnum(String code, String name) {
		this.code = code;
		this.name = name;
	}
	
	public static BlockTargetEnum parseEnum(String c){
		for(BlockTargetEnum p : values()){
			if(p.getCode().equalsIgnoreCase(c)) {
				return p;
			}
		}
		return null;
	}
	
	public static Map<String,String> toMap() {
		Map<String,String> map = new HashMap<String,String>();
		for(BlockTargetEnum s : values()){
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
