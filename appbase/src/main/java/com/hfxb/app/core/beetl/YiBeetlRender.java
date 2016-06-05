package com.hfxb.app.core.beetl;

import com.jfinal.render.Render;
import com.jfinal.render.RenderException;
import org.beetl.core.GroupTemplate;
import org.beetl.core.exception.BeetlException;

public class YiBeetlRender extends Render {
	GroupTemplate gt = null;
	private transient static final String encoding = getEncoding();
	private transient static final String contentType = "text/html; charset=" + encoding;

	public YiBeetlRender(GroupTemplate gt, String view) {
		this.gt = gt;
		this.view = view;
	}

	@Override
	public void render() {
		try {
			response.setContentType(contentType);
			YiWebRender webRender = new YiWebRender(gt);
			webRender.render(view, request, response);
		} catch (BeetlException e) {
			throw new RenderException(e);
		}

	}

}