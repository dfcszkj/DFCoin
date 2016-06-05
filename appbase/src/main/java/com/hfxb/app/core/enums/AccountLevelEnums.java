package com.hfxb.app.core.enums;

public enum AccountLevelEnums {

	LEVEL0(1, "未激活"), ;

	private Integer code;
	private String desc;

	private AccountLevelEnums(Integer code, String desc) {
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

}
