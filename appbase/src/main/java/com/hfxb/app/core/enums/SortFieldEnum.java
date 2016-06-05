package com.hfxb.app.core.enums;

import java.util.HashMap;
import java.util.Map;

public enum SortFieldEnum {
	
	CREATE_TIME("create_time", "创建时间"),
	MODIFY_TIME("modify_time", "最后修改时间"),
	READ_NUM("read_num", "阅读量"),
	REPLY_NUM("reply_num", "回复量"),
	FAVORITE_NUM("favorite_num", "点赞量");
	
	private String code;
	
	private String name;
	
	private SortFieldEnum(String code, String name) {
		this.code = code;
		this.name = name;
	}
	
	public static SortFieldEnum parseEnum(String c){
		for(SortFieldEnum p : values()){
			if(p.getCode().equalsIgnoreCase(c)) {
				return p;
			}
		}
		return null;
	}
	
	public static Map<String,String> toMap() {
		Map<String,String> map = new HashMap<String,String>();
		for(SortFieldEnum s : values()){
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
