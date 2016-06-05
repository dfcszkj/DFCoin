package com.hfxb.app.core.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class XssFilter implements Filter {

	@Override  
    public void init(FilterConfig filterConfig) throws ServletException {  
    }  
  
    @Override  
    public void doFilter(ServletRequest request, ServletResponse response,  
            FilterChain chain) throws IOException, ServletException {  
        try {
			chain.doFilter(new XssHttpServletRequestWrapper((HttpServletRequest) request), response);
		} catch (Exception e) {
			e.printStackTrace();
		}  
    }  
  
    @Override  
    public void destroy() {  
    }  
}
