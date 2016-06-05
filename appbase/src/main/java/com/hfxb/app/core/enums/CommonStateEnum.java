package com.hfxb.app.core.enums;


public enum CommonStateEnum {

	ENABLE(1, "启用"),
	DISABLE(0, "禁用");
	
	private Integer code;
	
	private String desc;
	
	private CommonStateEnum(Integer code, String desc) {
		this.code = code;
		this.desc = desc;
	}
	
	public static CommonStateEnum parseEnum(int c){
		for(CommonStateEnum commonStatus : values()){
			if(c == commonStatus.getCode()) {
				return commonStatus;
			}
		}
		return ENABLE;
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
