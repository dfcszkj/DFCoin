package com.hfxb.app.core.handler;

import com.hfxb.app.core.common.Constants;
import com.hfxb.app.core.model.FakeBoundModel;
import com.hfxb.app.core.utils.RegexUtils;
import com.jfinal.handler.Handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class YiFakeStaticHandler extends Handler {
	
//	private String viewPostfix;
	
	public YiFakeStaticHandler() {
//		viewPostfix = ".html";
	}
	
	/*public YiFakeStaticHandler(String viewPostfix) {
		if (StrKit.isBlank(viewPostfix))
			throw new IllegalArgumentException("viewPostfix can not be blank.");
		this.viewPostfix = viewPostfix;
	}*/
	
	public void handle(String target, HttpServletRequest request, HttpServletResponse response, boolean[] isHandled) {
		/*if ("/".equals(target)) {
			nextHandler.handle(target, request, response, isHandled);
			return;
		}*/
		// pass static files
		String[] fakeDisallowStart = Constants.config.getStringArray("fake.disallow.start");
		for(String disallow : fakeDisallowStart) {
			if(target.startsWith(disallow)) {
				System.out.println("target=" + target + ", disallow="+disallow);
				nextHandler.handle(target, request, response, isHandled);
				return;
			}
		}
		String[] fakeDisallowEnd = Constants.config.getStringArray("fake.disallow.end");
		for(String disallow : fakeDisallowEnd) {
			if(target.endsWith(disallow)) {
				System.out.println("target=" + target + ", disallow="+disallow);
				nextHandler.handle(target, request, response, isHandled);
				return;
			}
		}
		
		/*if (target.indexOf('.') == -1) {
			HandlerKit.renderError404(request, response, isHandled);
			return ;
		}
		
		int index = target.lastIndexOf(viewPostfix);
		if (index != -1)
			target = target.substring(0, index);*/
		// deal parameter
		// list_cid, list_cid_pageNumber, show_id
		List<FakeBoundModel> urlList = Constants.fake.getOutBoundList();
		String real = "", fake = "";
		for(FakeBoundModel url : urlList) {
			//^/article/list?cid=(\d+)(;jsessionid=.*)?$
			real = url.getReal();
			///article/list_{}.html
			fake = url.getFake();
			List<String> values = RegexUtils.getValues(target, fake);
			if(values != null && values.size() > 0) {
				while(real.indexOf("{}") > 0 && (values != null && values.size() > 0)) {
					real = real.replace("{}", values.get(0));
					values.remove(0);
				}
				target = real;
			}
		}
		if(target.indexOf("?") > 0) {
			// get query string, such as: a=a1&b=b1
			String paramStr = target.substring(target.indexOf("?") + 1);
			// get param array, such as: [a=a1],[b=b1]
			String[] params = paramStr.split("&");
			for(String p : params) {
				// get param name and value, such as: [a],[a1]
				String[] pp = p.split("=");
				if(pp.length == 2) {
					// TODO set parameter to request
					request.getParameterMap().put(pp[0], new String[]{pp[1]});
				} 
			}
			target = target.substring(0, target.indexOf("?"));
		}
		nextHandler.handle(target, request, response, isHandled);
	}
}