package com.hfxb.app.web.web;

import com.hfxb.app.core.annotation.Action;
import com.hfxb.app.core.enums.CommonStateEnum;
import com.hfxb.app.core.utils.FileUtils;
import com.hfxb.app.web.base.BaseController;
import com.hfxb.app.web.news.entity.NewsEntity;
import com.hfxb.app.web.web.entity.WebEntity;
import com.jfinal.upload.UploadFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;
import java.util.List;

@Action(action = "/admin/web")
public class WebController extends BaseController {

	private static final Logger logger = LoggerFactory.getLogger(WebController.class);

	public void index() {
		List<WebEntity> list= WebEntity.dao.getAll();
		setAttr("list",list);
		setAttr("site", getSite());
		render("/admin/web/list.html");
	}


	public void add(){
		render("/admin/web/add.html");
	}

	public void edit(){
		long id=getParaToLong("id");
		WebEntity entity=WebEntity.dao.findById(id);
		setAttr("id",id);
		setAttr("title",entity.getStr("name"));
		setAttr("content",entity.getStr("content"));
		render("add.html");
	}


	public void recycle() {
		int type=getParaToInt("type");
		String id = getPara("id");
		try {
			if(org.apache.commons.lang.StringUtils.isNotBlank(id)) {
				String[] idArr = id.split(",");
				for(String i : idArr) {
					WebEntity.dao.delete(i);
				}
			}
		} catch (Exception e) {
			addError("删除文章失败， 文章ID： [" + id + "]" + e.getMessage());
			logger.error("删除文章失败， 文章ID： [" + id + "]" + e.getMessage(), e);
			keepPara();
		} finally {
			redirect("/admin/web");
		}

	}
	public void lock() {
		WebEntity entity = WebEntity.dao.findById(getPara("id"));
		if(entity!=null) {
			if(entity.getInt("state") == CommonStateEnum.ENABLE.getCode()){
				entity.set("state", CommonStateEnum.DISABLE.getCode());
			} else {
				entity.set("state", CommonStateEnum.ENABLE.getCode());
			}
			entity.update();
			redirect("/admin/web");
		}
	}

	public  void doEdit(){
		int id=getParaToInt("id");
		try{

			int state=getParaToInt("state");
			WebEntity entity=WebEntity.dao.findById(id);
			String title=getPara("title");
			entity.set("content",getPara("content"));
			entity.set("state",state);
			entity.set("name",title);
			entity.update();
			redirect("/admin/web");
		}catch (Exception e){
			addError("保存失败, " + e.getMessage());
			keepPara();
			render("add.html");
		}
	}

	public void save(){
		try{
			String title=getPara("title");
			int state=getParaToInt("state");
			WebEntity entity=new WebEntity();
			entity.set("content",getPara("content"));
			entity.set("create_time", Calendar.getInstance().getTime());
			entity.set("state",state);
			entity.set("name",title);
			entity.save();
			redirect("/admin/web");
		}catch (Exception e){
			addError("保存失败, " + e.getMessage());
			keepPara();
			render("add.html");
		}

	}

}
