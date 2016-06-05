package com.hfxb.app.core.enums;

import java.util.HashMap;
import java.util.Map;

public enum ChouShouStateEnums {

	ONSALE(1, "出售中"),
	CACELED(2, "已取消"),
	SALEED(3, "已售出"),
	LOADING(4, "等待处理"),
	ING(5, "正在处理"),
	SUCCESS(6, "交易完成"),;

	private Integer code;
	private String desc;

	private ChouShouStateEnums(Integer code, String desc) {
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
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public static Map<Integer,String> toMap() {
		Map<Integer,String> map = new HashMap<Integer,String>();
		for(ChouShouStateEnums s : values()){
			map.put(Integer.valueOf(s.getCode()), s.getDesc());
		}
		return map;
	}
}
