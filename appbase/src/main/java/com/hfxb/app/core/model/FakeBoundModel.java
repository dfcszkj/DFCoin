package com.hfxb.app.core.model;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement
@XmlType(propOrder = {"real", "fake"}) 
public class FakeBoundModel {
	
	@XmlAttribute  
    private String title;
	
	// 实际的动态网址
	@XmlElement(required = true)  
	private String real;
	
	// 伪静态地址
	@XmlElement(required = true)  
	private String fake;
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getReal() {
		return real;
	}

	public void setReal(String real) {
		this.real = real;
	}

	public String getFake() {
		return fake;
	}

	public void setFake(String fake) {
		this.fake = fake;
	}

}
