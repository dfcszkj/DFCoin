package com.hfxb.app.core.enums;

import com.alibaba.druid.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 *  金额类型 枚举
 * @author ilgqh
 *
 */
public enum MoneyTypeEnum {
	MINING("1","采矿"),
	DIRECT("2","直推奖"),
	ADMIN("3","管理奖"),
	TEAM("4","团队奖"),
	MANAGE("5","经理奖"),
	ZHUAN("6","转币"),
	ZHUANDE("7","被转币"),
	SALE("8","出售币"),
	ACTIVE("9","激活"),
	RECHARGE("10","充值"),
	BAODAN("11","报单中心");

	private String code;
	private String desc;

	private MoneyTypeEnum(String code, String desc) {
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
	
	public static MoneyTypeEnum parseEnum(String name){
		for(MoneyTypeEnum status : values()){
			if(StringUtils.equals(status.getCode(), name)) {
				return status;
			}
		}
		return null;
	}
	
	public static Map<Integer,String> toMap() {
		Map<Integer,String> map = new HashMap<Integer,String>();
		for(MoneyTypeEnum s : values()){
			map.put(Integer.valueOf(s.getCode()), s.getDesc());
		}
		return map;
	}
	
}
