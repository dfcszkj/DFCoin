package com.hfxb.app.core.model;

public class Pagination {
	
	private Integer pn;
	
	private Integer size;
	
	public Pagination(Integer pn, Integer size) {
		super();
		this.pn = pn;
		this.size = size;
	}

	public Integer getPn() {
		return pn;
	}

	public void setPn(Integer pn) {
		this.pn = pn;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

}
