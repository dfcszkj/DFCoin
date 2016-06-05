package com.hfxb.app.web.news;

import com.hfxb.app.core.annotation.Action;
import com.hfxb.app.core.model.Pagination;
import com.hfxb.app.core.utils.ParamUtils;
import com.hfxb.app.web.base.BaseController;
import com.hfxb.app.web.news.entity.NewsEntity;
import com.jfinal.plugin.activerecord.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

@Action(action = "/gonggao")
public class GongGaoController extends BaseController {

	private static final Logger logger = LoggerFactory.getLogger(GongGaoController.class);

	public void index() {
		setHeader();
		Pagination pager = getPager();
		Page<NewsEntity> page = null;
		Map<String,String> p=ParamUtils.translateMap(getParaMap());
		p.put("type","2");
		page = NewsEntity.dao.getPager(pager,p );
		setAttr("page", page);
		setAttr("site", getSite());
		render("/themes/"+getSite().get("theme")+"/user/xx_gonggao.html");
	}

	public void detail(){
		setHeader();
		NewsEntity entity=NewsEntity.dao.findById(getPara("id"));
		setAttr("entity",entity);
		render("/themes/"+getSite().get("theme")+"/user/xiangxi.html");
	}
}
