package com.hfxb.app.web.news;

import com.hfxb.app.core.annotation.Action;
import com.hfxb.app.core.enums.CommonStateEnum;
import com.hfxb.app.core.enums.PostStateEnum;
import com.hfxb.app.core.enums.RoleEnum;
import com.hfxb.app.core.model.Pagination;
import com.hfxb.app.core.utils.FileUtils;
import com.hfxb.app.core.utils.ParamUtils;
import com.hfxb.app.web.base.BaseController;
import com.hfxb.app.web.news.entity.NewsEntity;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.upload.UploadFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;
import java.util.List;

@Action(action = "/news")
public class NewsController extends BaseController {

	private static final Logger logger = LoggerFactory.getLogger(NewsController.class);

	public void index() {
		List<NewsEntity> list=NewsEntity.dao.getAllByType(1);
		setAttr("list",list);
		setAttr("site", getSite());
		render("/themes/"+getSite().get("theme")+"/news.html");
	}

	public void detail(){
		NewsEntity entity=NewsEntity.dao.findById(getPara("id"));
		setAttr("entity",entity);
		setAttr("site", getSite());
		render("/themes/"+getSite().get("theme")+"/newsdetail.html");
	}


}
