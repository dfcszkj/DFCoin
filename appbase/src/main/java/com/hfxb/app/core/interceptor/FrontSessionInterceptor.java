package com.hfxb.app.core.interceptor;


import com.hfxb.app.core.common.Constants;
import com.hfxb.app.core.utils.StringUtils;
import com.hfxb.app.web.account.entity.AccountEntity;
import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;

public class FrontSessionInterceptor implements Interceptor {
	
	@Override
	public void intercept(Invocation i) {
		
		Controller c = i.getController();
		if(c.getSession() == null) {
			c.redirect("/");
		} else {
			// if need filter session
			String requestURI = c.getRequest().getRequestURI();
			if(StringUtils.isInclude(requestURI, Constants.config.getStringArray("front.session.include"))){
				AccountEntity account = (AccountEntity) c.getSession().getAttribute(Constants.FRONT_LOGIN_USER);
				if(account != null){
					if(account.getStr("secondPass")==null||account.getStr("secondPass").length()==0){
						String[] setPassArr={"/user/changeUser","/user/setmima","/user/logout"};
						if(StringUtils.isInclude(requestURI,setPassArr)){
							i.invoke();
						}else{
							c.redirect("/user/setmima");
						}
					}else if(account.getInt("level")==1){
						String[] jihuoArr={"/user/zhuanBi","/user/chuShouBi"};
						if(!StringUtils.isInclude(requestURI,jihuoArr)) {
							i.invoke();
						}else{
							c.redirect("/user/jihuo");
						}

					}else{
						i.invoke();

					}

				} else {
					if(StringUtils.isInclude(requestURI, Constants.config.getStringArray("front.session.exclude"))){
						i.invoke();
					} else {
						c.redirect(Constants.USER_LOGIN_URL);
					}
				}
			} else {
				i.invoke();
			}
		}
		
	}

}
