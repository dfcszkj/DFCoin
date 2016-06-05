package com.hfxb.app.core.model;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "fakes")  
@XmlType
public class FakeModel {
	
//	@XmlElementWrapper(name = "url") 
    @XmlElement(name = "outBound")  
	List<FakeBoundModel> outBoundList;
    
    @XmlElement(name = "inBound")  
	List<FakeBoundModel> inBoundList;

	public List<FakeBoundModel> getOutBoundList() {
		return outBoundList;
	}

	public void setOutBoundList(List<FakeBoundModel> outBoundList) {
		this.outBoundList = outBoundList;
	}

	public List<FakeBoundModel> getInBoundList() {
		return inBoundList;
	}

	public void setInBoundList(List<FakeBoundModel> inBoundList) {
		this.inBoundList = inBoundList;
	}

}
