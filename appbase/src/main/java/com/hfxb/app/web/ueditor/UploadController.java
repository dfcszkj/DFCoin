package com.hfxb.app.web.ueditor;


import com.baidu.ueditor.ActionEnter;
import com.hfxb.app.core.annotation.Action;
import com.hfxb.app.web.base.BaseController;
import com.jfinal.kit.PathKit;

@Action(action = "/ueditor")
public class UploadController extends BaseController {
	
	public void upload() throws Exception {
		getRequest().setCharacterEncoding( "utf-8" );
		getResponse().setHeader("Content-Type" , "text/html");
		
		String rootPath = PathKit.getWebRootPath();
		
		renderJson(new ActionEnter( getRequest(), rootPath ).exec());
	}
}
