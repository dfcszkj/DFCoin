package com.hfxb.app.web.web;

import com.hfxb.app.core.annotation.Action;
import com.hfxb.app.core.enums.CommonStateEnum;
import com.hfxb.app.core.utils.FileUtils;
import com.hfxb.app.web.base.BaseController;
import com.hfxb.app.web.news.entity.NewsEntity;
import com.hfxb.app.web.web.entity.BannerEntity;
import com.hfxb.app.web.web.entity.WebEntity;
import com.jfinal.upload.UploadFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;
import java.util.List;

@Action(action = "/admin/banner")
public class BannerController extends BaseController {

	private static final Logger logger = LoggerFactory.getLogger(BannerController.class);

	public void index() {
		List<BannerEntity> list= BannerEntity.dao.getAll();
		setAttr("list",list);
		setAttr("site", getSite());
		render("/admin/banner/list.html");
	}


	public void add(){
		render("/admin/banner/add.html");
	}

	public void edit(){
		long id=getParaToLong("id");
		BannerEntity entity=BannerEntity.dao.findById(id);
		setAttr("id",id);
		setAttr("title",entity.getStr("title"));
		setAttr("icon",entity.getStr("banner"));
		setAttr("state",entity.getInt("state"));
		render("add.html");
	}



	public void recycle() {
		String id = getPara("id");
		try {
			if(org.apache.commons.lang.StringUtils.isNotBlank(id)) {
				String[] idArr = id.split(",");
				for(String i : idArr) {
					BannerEntity.dao.delete(i);
				}
			}
		} catch (Exception e) {
			addError("删除banner失败ID： [" + id + "]" + e.getMessage());
			logger.error("删除banner失败， ID： [" + id + "]" + e.getMessage(), e);
			keepPara();
		} finally {
			redirect("/admin/banner");
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
			redirect("/admin/banner");
		}
	}

	public  void doEdit(){
		UploadFile file = getFile("icon");

		int id=getParaToInt("id");
		try{

			int state=getParaToInt("state");
			BannerEntity entity=BannerEntity.dao.findById(id);
			if(file!=null){
				String filename= FileUtils.saveFile(file.getFile());
				entity.set("banner",filename);
			}
			String title=getPara("title");
			entity.set("state",state);
			entity.set("title",title);
			entity.update();
			redirect("/admin/banner");
		}catch (Exception e){
			addError("保存失败, " + e.getMessage());
			keepPara();
			render("add.html");
		}
	}

	public void save(){
		UploadFile file = getFile("icon");
		try{

			String filename="";
			if(file!=null){
				filename=FileUtils.saveFile(file.getFile());
			}
			String title=getPara("title");
			String icon=filename;
			int state=getParaToInt("state");
			BannerEntity entity=new BannerEntity();
			entity.set("banner",icon);
			entity.set("create_time", Calendar.getInstance().getTime());
			entity.set("state",state);
			entity.set("title",title);
			entity.save();
			redirect("/admin/banner");
		}catch (Exception e){
			addError("保存失败, " + e.getMessage());
			keepPara();
			render("add.html");
		}

	}

}
