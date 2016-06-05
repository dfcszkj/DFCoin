package com.hfxb.app.core.filter;

import org.apache.commons.lang.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {
	
	private static final Logger logger = LoggerFactory.getLogger(XssHttpServletRequestWrapper.class);

	public XssHttpServletRequestWrapper(HttpServletRequest request) {  
        super(request);  
    }  
  
    @Override  
    public String getHeader(String name) {  
        return StringEscapeUtils.escapeHtml(super.getHeader(name));  
    }  
  
    @Override  
    public String getQueryString() {  
        return StringEscapeUtils.escapeHtml(super.getQueryString());  
    }  
  
    @Override  
    public String getParameter(String name) {  
        return StringEscapeUtils.escapeHtml(super.getParameter(name));  
    }  
  
    @Override  
    public String[] getParameterValues(String name) {  
        try {
			String[] values = super.getParameterValues(name);  
			if(values != null) {  
			    int length = values.length;  
			    String[] escapseValues = new String[length];  
			    for(int i = 0; i < length; i++){
			        escapseValues[i] = StringEscapeUtils.escapeHtml(values[i]);  
			    }
			    return escapseValues;  
			}  
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}  
        return super.getParameterValues(name);
    }  

}
