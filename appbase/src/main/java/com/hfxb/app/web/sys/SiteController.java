package com.hfxb.app.web.sys;

import com.hfxb.app.core.annotation.Action;
import com.hfxb.app.core.common.Constants;
import com.hfxb.app.core.utils.FileUtils;
import com.hfxb.app.core.utils.PropertiesUtils;
import com.hfxb.app.web.account.entity.BiToRMBEntity;
import com.hfxb.app.web.base.BaseController;
import com.hfxb.app.web.sys.entity.SiteEntity;
import com.hfxb.app.web.sys.entity.SystemEntity;
import com.jfinal.upload.UploadFile;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;
import java.util.Calendar;

@Action(action = "/admin/site")
public class SiteController extends BaseController {
	public void index() {
		setAttr("icon",Constants.config.getString("system.logo"));
		setAttr("webname",Constants.config.getString("system.webname"));
		setAttr("copyright",Constants.config.getString("system.copyright"));
		setAttr("weburl",Constants.config.getString("system.weburl"));
		setAttr("site", SiteEntity.dao.getSite());
		render("add.html");
	}

	public void save() {
		UploadFile file = getFile("icon");
		try {

			PropertiesConfiguration config = new PropertiesConfiguration("main.properties");
			if(file!=null){
				String filename= FileUtils.saveFile(file.getFile());
				config.setProperty("system.logo",filename);
			}

			config.setProperty("system.webname", getPara("webname"));
			config.setProperty("system.copyright", getPara("copyright"));
			config.setProperty("system.weburl", getPara("weburl"));
			config.save();
			com.hfxb.app.core.common.Constants.config = PropertiesUtils.load(
					FileUtils.locateAbsolutePathFromClasspath("main.properties").getName(), "utf-8");

			redirect("/admin/site");
		} catch(Exception e) {
			addError("编辑站点信息出错, " + e.getMessage());
			keepPara();
			render("add.html");
		}

	}

	
}
