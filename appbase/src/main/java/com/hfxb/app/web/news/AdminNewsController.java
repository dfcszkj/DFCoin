package com.hfxb.app.web.news;

import ch.qos.logback.core.util.FileUtil;
import com.hfxb.app.core.annotation.Action;
import com.hfxb.app.core.enums.CommonStateEnum;
import com.hfxb.app.core.enums.PostStateEnum;
import com.hfxb.app.core.enums.RoleEnum;
import com.hfxb.app.core.model.Pagination;
import com.hfxb.app.core.utils.FileUtils;
import com.hfxb.app.core.utils.ParamUtils;
import com.hfxb.app.core.utils.StringUtils;
import com.hfxb.app.web.account.entity.AccountEntity;
import com.hfxb.app.web.base.BaseController;
import com.hfxb.app.web.news.entity.NewsEntity;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.upload.UploadFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;

@Action(action = "/admin/news")
public class AdminNewsController extends BaseController {

	private static final Logger logger = LoggerFactory.getLogger(AdminNewsController.class);
	
	public void index() {
		keepPara();
		forwardAction("list");
	}

	public void list() {
		Pagination pager = getPager();
		Page<NewsEntity> page = null;
		try {
			page = NewsEntity.dao.getPager(pager, ParamUtils.translateMap(getParaMap()));
			setAttr("page", page);
			setAttr("type", getParaToInt("type"));
			setAttr("stateMap", PostStateEnum.toMap());
			setAttr("roleMap", RoleEnum.toMap());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		keepPara();
		render("/admin/news/list.html");
	}

	public void add(){
		int type=getParaToInt("type");
		setAttr("type",type);
		render("/admin/news/add.html");
	}

	public void edit(){
		long id=getParaToLong("id");
		int type=getParaToInt("type");
		setAttr("type",type);
		NewsEntity entity=NewsEntity.dao.findById(id);
		setAttr("id",id);
		setAttr("title",entity.getStr("title"));
		setAttr("icon",entity.getStr("icon"));
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
					NewsEntity.dao.delete(i);
				}
			}
		} catch (Exception e) {
			addError("删除文章失败， 文章ID： [" + id + "]" + e.getMessage());
			logger.error("删除文章失败， 文章ID： [" + id + "]" + e.getMessage(), e);
			keepPara();
		} finally {
			redirect("/admin/news/list?type="+type);
		}

	}
	public void lock() {
		NewsEntity entity = NewsEntity.dao.findById(getPara("id"));
		if(entity!=null) {
			if(entity.getInt("state") == CommonStateEnum.ENABLE.getCode()){
				entity.set("state", CommonStateEnum.DISABLE.getCode());
			} else {
				entity.set("state", CommonStateEnum.ENABLE.getCode());
			}
			entity.update();
			redirect("/admin/news/list?type="+getPara("type"));
		}
	}

	public  void doEdit(){
		UploadFile file = getFile("icon");
		int type=getParaToInt("type");
		int id=getParaToInt("id");
		try{

			int state=getParaToInt("state");
			NewsEntity entity=NewsEntity.dao.findById(id);
			if(file!=null){
				String filename= FileUtils.saveFile(file.getFile());
				entity.set("icon",filename);
			}
			String title=getPara("title");
			entity.set("content",getPara("content"));
			entity.set("edit_time", Calendar.getInstance().getTime());
			entity.set("state",state);
			entity.set("title",title);
			entity.update();
			redirect("/admin/news/list?type="+type);
		}catch (Exception e){
			addError("保存失败, " + e.getMessage());
			keepPara();
			render("add.html?type="+type);
		}
	}

	public void save(){
		UploadFile file = getFile("icon");
		int type=getParaToInt("type");
		try{

			String filename="";
			if(file!=null){
				filename=FileUtils.saveFile(file.getFile());
			}
			String title=getPara("title");
			String icon=filename;
			int state=getParaToInt("state");
			NewsEntity entity=new NewsEntity();
			entity.set("authorid",0);
			entity.set("cate_id",0);
			entity.set("clicks",0);
			entity.set("content",getPara("content"));
			entity.set("icon",icon);
			entity.set("type",type);
			entity.set("create_time", Calendar.getInstance().getTime());
			entity.set("state",state);
			entity.set("title",title);
			entity.save();
			redirect("/admin/news/list?type="+type);
		}catch (Exception e){
			addError("保存失败, " + e.getMessage());
			keepPara();
			render("add.html?type="+type);
		}

	}

}
