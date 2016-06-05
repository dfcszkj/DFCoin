package com.hfxb.app.web.sys;

import com.hfxb.app.core.annotation.Action;
import com.hfxb.app.core.enums.CommonStateEnum;
import com.hfxb.app.core.model.Pagination;
import com.hfxb.app.core.utils.FileUtils;
import com.hfxb.app.core.utils.ParamUtils;
import com.hfxb.app.core.utils.PropertiesUtils;
import com.hfxb.app.web.account.entity.BiToRMBEntity;
import com.hfxb.app.web.account.entity.MsgEntity;
import com.hfxb.app.web.base.BaseController;
import com.hfxb.app.web.web.entity.WebEntity;
import com.jfinal.plugin.activerecord.Page;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

@Action(action = "/admin/bitormb")
public class BiToRMBController extends BaseController {

	private static final Logger logger = LoggerFactory.getLogger(BiToRMBController.class);

	public void index() {
		Pagination pager = getPager();
		Page<BiToRMBEntity> page = null;
		Map<String,String> p= ParamUtils.translateMap(getParaMap());
		page = BiToRMBEntity.dao.getPager(pager,p );
		setAttr("page", page);
		setAttr("site", getSite());
		render("/admin/bitormb/list.html");
	}


	public void add(){
		render("/admin/bitormb/add.html");
	}

	public void edit(){
		long id=getParaToLong("id");
		BiToRMBEntity entity=BiToRMBEntity.dao.findById(id);
		setAttr("id",id);
		setAttr("create_time",entity.getDate("create_time"));
		setAttr("money",entity.getBigDecimal("money"));



		render("add.html");
	}


	public void recycle() {
		String id = getPara("id");
		try {
			if(org.apache.commons.lang.StringUtils.isNotBlank(id)) {
				String[] idArr = id.split(",");
				for(String i : idArr) {
					BiToRMBEntity.dao.delete(i);
				}
			}
		} catch (Exception e) {
			addError("删除人民币比率失败,ID： [" + id + "]" + e.getMessage());
			logger.error("删除人民币比率失败,ID： [" + id + "]" + e.getMessage(), e);
			keepPara();
		} finally {
			redirect("/admin/bitormb");
		}

	}


	public  void doEdit(){
		int id=getParaToInt("id");
		try{
			BiToRMBEntity entity=BiToRMBEntity.dao.findById(id);
			entity.set("money",getPara("money"));
			entity.set("create_time",getParaToDate("create_time"));
			entity.update();
			PropertiesConfiguration config = null;
			try {
				config = new PropertiesConfiguration("main.properties");
				config.setProperty("system.bitormb",BiToRMBEntity.dao.findLast().getBigDecimal("money"));
				config.save();
				com.hfxb.app.core.common.Constants.config = PropertiesUtils.load(
						FileUtils.locateAbsolutePathFromClasspath("main.properties").getName(), "utf-8");
			} catch (Exception e) {
				e.printStackTrace();
			}
			redirect("/admin/bitormb");
		}catch (Exception e){
			addError("保存失败, " + e.getMessage());
			keepPara();
			render("add.html");
		}
	}

	public void save(){
		try{
			BiToRMBEntity entity=new BiToRMBEntity();
			entity.set("money",getPara("money"));
			entity.set("create_time",getParaToDate("create_time"));
			entity.save();
			PropertiesConfiguration config = null;
			try {
				config = new PropertiesConfiguration("main.properties");
				config.setProperty("system.bitormb",BiToRMBEntity.dao.findLast().getBigDecimal("money"));
				config.save();
				com.hfxb.app.core.common.Constants.config = PropertiesUtils.load(
						FileUtils.locateAbsolutePathFromClasspath("main.properties").getName(), "utf-8");
			} catch (Exception e) {
				e.printStackTrace();
			}
			redirect("/admin/bitormb");
		}catch (Exception e){
			addError("保存失败, " + e.getMessage());
			keepPara();
			render("add.html");
		}

	}

}
