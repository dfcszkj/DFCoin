package com.hfxb.app.core.handler;

import com.jfinal.handler.Handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class BaseHandler extends Handler {

	@Override
	public void handle(String target, HttpServletRequest request,
			HttpServletResponse response, boolean[] isHandled) {
		request.setAttribute("ctx", getContextPath(request));
		request.setAttribute("url", getBaseURL(request));
		
		nextHandler.handle(target, request, response, isHandled);
	}
	
	//项目根路径
	public String getContextPath(HttpServletRequest request) {
		return request.getContextPath();
	}
	
	//根域名
	public String getBaseURL(HttpServletRequest request) {
		String url = request.getScheme() + "://" + request.getServerName();
		if(request.getServerPort() != 80) {
			url = url + ":" + request.getServerPort();
		}
		url = url + getContextPath(request);
		return url;
	}

}
