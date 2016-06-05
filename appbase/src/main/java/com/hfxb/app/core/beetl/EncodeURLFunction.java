package com.hfxb.app.core.beetl;

import org.beetl.core.Context;
import org.beetl.core.Function;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;

public class EncodeURLFunction implements Function{
	
	private static final Logger logger = LoggerFactory.getLogger(EncodeURLFunction.class);

	private HttpServletResponse response; 
	
	public EncodeURLFunction(HttpServletResponse response) {
		super();
		this.response = response;
	}

	@Override
	public Object call(Object[] paras, Context ctx) {
		if(paras.length != 1) {  
			logger.error("Wrong arguments!");
			return null;
        }
		 return response.encodeURL(String.valueOf(paras[0]));  
	}

}
