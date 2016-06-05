package com.hfxb.app.core.beetl;

import org.beetl.core.GroupTemplate;
import org.beetl.core.Template;
import org.beetl.core.exception.BeetlException;
import org.beetl.ext.web.SessionWrapper;
import org.beetl.ext.web.WebVariable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.util.Enumeration;

public class YiWebRender {
	GroupTemplate gt = null;

	public YiWebRender(GroupTemplate gt) {
		this.gt = gt;
	}

	public void render(String key, HttpServletRequest request, HttpServletResponse response, Object... args) {
		Writer writer = null;
		OutputStream os = null;
		String ajaxId = null;
		Template template = null;
		try {
			// response.setContentType(contentType);
			int ajaxIdIndex = key.lastIndexOf("#");
			if (ajaxIdIndex != -1) {
				ajaxId = key.substring(ajaxIdIndex + 1);
				key = key.substring(0, ajaxIdIndex);
				template = gt.getAjaxTemplate(key, ajaxId);
			} else {
				template = gt.getTemplate(key);
			}

			Enumeration<String> attrs = request.getAttributeNames();

			while (attrs.hasMoreElements()) {
				String attrName = attrs.nextElement();
				template.binding(attrName, request.getAttribute(attrName));

			}
			WebVariable webVariable = new WebVariable();
			webVariable.setRequest(request);
			webVariable.setResponse(response);
			template.binding("session",
					new SessionWrapper(request.getSession(false)));

			template.binding("servlet", webVariable);
			template.binding("request", request);
			template.binding("ctxPath", request.getContextPath());

			modifyTemplate(template, key, request, response, args);

			if (gt.getConf().isDirectByteOutput()) {
				os = response.getOutputStream();
				template.renderTo(os);
			} else {
				/*if("1".equals("static")) {
					os = new FileOutputStream(request.getParameter("yiTemplateFile"));
					template.renderTo(os);
				} else {*/
					writer = response.getWriter();
					template.renderTo(writer);
//				}
			}

		} catch (IOException e) {
			handleClientError(e);
		} catch (BeetlException e) {
			handleBeetlException(e);
		}

		finally {
			try {
				if (writer != null)
					writer.flush();
				if (os != null) {
					os.flush();
				}
			} catch (IOException e) {
				handleClientError(e);
			}
		}
	}

	/**
	 * 鍙互娣诲姞鏇村鐨勭粦瀹�
	 * 
	 * @param template
	 *            妯℃澘
	 * @param key
	 *            妯℃澘鐨勮祫婧恑d
	 * @param request
	 * @param response
	 * @param args
	 *            璋冪敤render鐨勬椂鍊欎紶鐨勫弬鏁�
	 */
	protected void modifyTemplate(Template template, String key,
			HttpServletRequest request, HttpServletResponse response,
			Object... args) {

	}

	/**
	 * 澶勭悊瀹㈡埛绔姏鍑虹殑IO寮傚父
	 * 
	 * @param ex
	 */
	protected void handleClientError(IOException ex) {
		// do nothing
	}

	/**
	 * 澶勭悊瀹㈡埛绔姏鍑虹殑IO寮傚父
	 * 
	 * @param ex
	 */
	protected void handleBeetlException(BeetlException ex) {
		throw ex;
	}
}
