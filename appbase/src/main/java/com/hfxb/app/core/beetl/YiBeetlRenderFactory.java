package com.hfxb.app.core.beetl;

import com.jfinal.kit.PathKit;
import com.jfinal.render.IMainRenderFactory;
import com.jfinal.render.Render;
import org.beetl.core.Configuration;
import org.beetl.core.GroupTemplate;
import org.beetl.core.ResourceLoader;
import org.beetl.core.resource.WebAppResourceLoader;

import java.io.IOException;

public class YiBeetlRenderFactory implements IMainRenderFactory {

	public static String viewExtension = ".html";
	public static GroupTemplate groupTemplate = null;

	public YiBeetlRenderFactory() {
		init(PathKit.getWebRootPath());
		// init(null); use jfinalkit instead

	}

	public YiBeetlRenderFactory(ResourceLoader resourceLoader) {
		if (groupTemplate != null) {
			groupTemplate.close();
		}
		try {

			Configuration cfg = Configuration.defaultConfiguration();
			groupTemplate = new GroupTemplate(resourceLoader, cfg);
		} catch (IOException e) {
			throw new RuntimeException("生成GroupTemplate出错", e);
		}
	}

	public YiBeetlRenderFactory(String templateRoot) {
		init(templateRoot);
	}

	private void init(String root) {
		if (groupTemplate != null) {
			groupTemplate.close();
		}

		try {

			Configuration cfg = Configuration.defaultConfiguration();
			WebAppResourceLoader resourceLoader = new WebAppResourceLoader(root);
			groupTemplate = new GroupTemplate(resourceLoader, cfg);

		} catch (IOException e) {
			throw new RuntimeException("生成GroupTemplate出错", e);
		}
	}

	public Render getRender(String view) {
		return new YiBeetlRender(groupTemplate, view);
	}

	public String getViewExtension() {
		return viewExtension;
	}

}