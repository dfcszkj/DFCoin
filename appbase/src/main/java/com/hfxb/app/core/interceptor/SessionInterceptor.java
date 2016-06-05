package com.hfxb.app.core.interceptor;

import com.hfxb.app.core.common.Constants;
import com.hfxb.app.core.utils.StringUtils;
import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;
import org.apache.shiro.SecurityUtils;

public class SessionInterceptor implements Interceptor {
	
	@Override
	public void intercept(Invocation i) {
		
		Controller c = i.getController();
		// if need filter session
		String requestURI = c.getRequest().getRequestURI();
		if(StringUtils.isInclude(requestURI, Constants.config.getStringArray("session.include"))){
			//if user has authenticated, invoke next interceptor
			if(SecurityUtils.getSubject().isAuthenticated()){
				i.invoke();
			} else {
				//else redirect to login page
				//if session is null, redirect to login page
				if(SecurityUtils.getSubject().getSession() != null) {
					if(StringUtils.isInclude(requestURI, Constants.config.getStringArray("session.exclude"))){
						i.invoke();
					} else {
						c.redirect(Constants.ADMIN_LOGIN_URL);
					}
				} else {
					c.redirect(Constants.ADMIN_LOGIN_URL);
				}
			}
		} else {
			i.invoke();
		}
		
	}

}
