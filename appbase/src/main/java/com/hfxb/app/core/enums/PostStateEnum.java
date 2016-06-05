package com.hfxb.app.core.enums;

import com.alibaba.druid.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 文章状态枚举
 * @author ilgqh
 *
 */
public enum PostStateEnum {

	AUDIT("0","待审核"),
	PUBLISH("1","已发布"),
	DELETE("2","已删除");
	
	private String code;
	private String desc;
	
	private PostStateEnum(String code, String desc) {
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
	
	public static PostStateEnum parseEnum(String name){
		for(PostStateEnum status : values()){
			if(StringUtils.equals(status.getCode(), name)) {
				return status;
			}
		}
		return null;
	}
	
	public static Map<Integer,String> toMap() {
		Map<Integer,String> map = new HashMap<Integer,String>();
		for(PostStateEnum s : values()){
			map.put(Integer.valueOf(s.getCode()), s.getDesc());
		}
		return map;
	}
	
}
