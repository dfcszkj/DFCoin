package com.hfxb.app.core.enums;

import java.util.HashMap;
import java.util.Map;

public enum BlockTypeEnum {
	
	STANDARD("stanard", "标准文章列表"),
	CUSTOMIZE("customize", "自定义文章列表"),
	RANDOM("random", "随机文章列表(全站)"),
	CATEGORY("category", "随机文章列表(同类)"),
	HTML("html", "自定义HTML");
	
	private String code;
	
	private String name;
	
	private BlockTypeEnum(String code, String name) {
		this.code = code;
		this.name = name;
	}
	
	public static BlockTypeEnum parseEnum(String c){
		for(BlockTypeEnum p : values()){
			if(p.getCode().equalsIgnoreCase(c)) {
				return p;
			}
		}
		return null;
	}
	
	public static Map<String,String> toMap() {
		Map<String,String> map = new HashMap<String,String>();
		for(BlockTypeEnum s : values()){
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
