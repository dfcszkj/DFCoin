package com.hfxb.app.core.enums;

import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;

public enum PostTypeEnum {
	
	ARTICLE("article","文章"),
	PHOTO("photo", "图片集"),
	VIDEO("video", "视频"),
	BBS("bbs", "公告");
	
	private String code;
	
	private String desc;
	
	private PostTypeEnum(String code, String desc) {
		this.code = code;
		this.desc = desc;
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

	public static PostTypeEnum parseEnum(String name){
		for(PostTypeEnum status : values()){
			if(StringUtils.equals(name, status.getCode())) {
				return status;
			}
		}
		return null;
	}
	
	public static Map<String,String> toMap() {
		Map<String,String> map = new HashMap<String,String>();
		for(PostTypeEnum s : values()){
			map.put(s.getCode(), s.getDesc());
		}
		return map;
	}
	
}
