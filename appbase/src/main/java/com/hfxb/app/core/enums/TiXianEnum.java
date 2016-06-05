package com.hfxb.app.core.enums;

import com.alibaba.druid.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 *  金额类型 枚举
 * @author ilgqh
 *
 */
public enum TiXianEnum {
	DAICHULI("1","待处理"),
	YICHULI("2","已处理"),
	YIQUXIAO("3","已取消"),;

	private String code;
	private String desc;

	private TiXianEnum(String code, String desc) {
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
	
	public static TiXianEnum parseEnum(String name){
		for(TiXianEnum status : values()){
			if(StringUtils.equals(status.getCode(), name)) {
				return status;
			}
		}
		return null;
	}
	
	public static Map<Integer,String> toMap() {
		Map<Integer,String> map = new HashMap<Integer,String>();
		for(TiXianEnum s : values()){
			map.put(Integer.valueOf(s.getCode()), s.getDesc());
		}
		return map;
	}
	
}
