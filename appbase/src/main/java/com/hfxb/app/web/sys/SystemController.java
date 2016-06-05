package com.hfxb.app.web.sys;

import com.hfxb.app.core.annotation.Action;
import com.hfxb.app.core.common.Constants;
import com.hfxb.app.core.utils.FileUtils;
import com.hfxb.app.core.utils.PropertiesUtils;
import com.hfxb.app.web.account.entity.BiToRMBEntity;
import com.hfxb.app.web.base.BaseController;
import com.hfxb.app.web.sys.entity.SiteEntity;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;
import java.util.Calendar;

@Action(action = "/admin/system")
public class SystemController extends BaseController {

	public void index() {
		setAttr("updateLeveldate",Constants.config.getLong("system.updateLeveldate"));
		setAttr("chanbidongjie",Constants.config.getLong("system.chanbidongjie"));
		setAttr("jiangdongjie",Constants.config.getLong("system.jiangdongjie"));
		setAttr("enableChong",Constants.config.getLong("system.enableChong"));
		setAttr("phoneusercount",Constants.config.getLong("system.phoneusercount"));
		setAttr("meiyuantormb",Constants.config.getLong("system.meiyuantormb"));
		setAttr("chushoufee",Constants.config.getLong("system.chushoufee"));
		setAttr("chushouPingTaifee",Constants.config.getLong("system.chushouPingTaifee"));
		String[] userlevels=Constants.config.getStringArray("system.userlevel");
		StringBuilder sb=new StringBuilder();
		for(String s:userlevels){
			sb.append(s).append(",");
		}
		setAttr("userlevel",sb.toString().substring(0,sb.toString().length()-1));
		String[] userlevelmoneys=Constants.config.getStringArray("system.userlevelmoney");
		StringBuilder sbMoney=new StringBuilder();
		for(String s:userlevelmoneys){
			sbMoney.append(s).append(",");
		}
		setAttr("userlevelmoney",sbMoney.toString().substring(0,sbMoney.toString().length()-1));

		String[] manmoneys=Constants.config.getStringArray("system.manmoney");
		StringBuilder sbmanmoney=new StringBuilder();
		for(String s:manmoneys){
			sbmanmoney.append(s).append(",");
		}
		setAttr("manmoney",sbmanmoney.toString().substring(0,sbmanmoney.toString().length()-1));
		setAttr("fandate",Constants.config.getString("system.fandate"));
		setAttr("moneyfanbi",Constants.config.getString("system.moneyfanbi"));
		String[] tuanduis=Constants.config.getStringArray("system.tuandui");
		setAttr("bitormb", BiToRMBEntity.dao.findLast().getBigDecimal("money"));

		StringBuilder sbtuandui=new StringBuilder();
		for(String s:tuanduis){
			sbtuandui.append(s).append(",");
		}

		setAttr("tuandui",sbtuandui.toString().substring(0,sbtuandui.toString().length()-1));

		String[] states=Constants.config.getStringArray("system.userState");
		StringBuilder sbState=new StringBuilder();
		for(String s:states){
			sbState.append(s).append(",");
		}
		setAttr("userState",sbState.toString().substring(0,sbState.toString().length()-1));

		String[] chushoujias=Constants.config.getStringArray("system.chushoujia");
		StringBuilder chushoujia=new StringBuilder();
		for(String s:chushoujias){
			chushoujia.append(s).append(",");
		}
		setAttr("chushoujia",chushoujia.toString().substring(0,chushoujia.toString().length()-1));


		setAttr("chushouliang",Constants.config.getLong("system.chushouliang"));
		setAttr("chushoubei",Constants.config.getLong("system.chushoubei"));
		setAttr("tixianliang",Constants.config.getLong("system.tixianliang"));
		setAttr("tixianbei",Constants.config.getLong("system.tixianbei"));
		setAttr("zhuanbiliang",Constants.config.getLong("system.zhuanbiliang"));
		setAttr("zhuanbibei",Constants.config.getLong("system.zhuanbibei"));
		setAttr("bite_url",Constants.config.getString("system.bite_url"));
		render("add.html");
	}

	public void save() {

		try {
			PropertiesConfiguration config = new PropertiesConfiguration("main.properties");
			config.setProperty("system.updateLeveldate", getPara("updateLeveldate"));
			config.setProperty("system.userState", getPara("userState"));
			config.setProperty("system.chanbidongjie", getPara("chanbidongjie"));
			config.setProperty("system.jiangdongjie", getPara("jiangdongjie"));
			config.setProperty("system.userlevel", getPara("userlevel"));
			config.setProperty("system.userlevelmoney", getPara("userlevelmoney"));
			config.setProperty("system.manmoney", getPara("manmoney"));
			config.setProperty("system.fandate", getPara("fandate"));
			config.setProperty("system.moneyfanbi", getPara("moneyfanbi"));
			config.setProperty("system.tuandui", getPara("tuandui"));
			config.setProperty("system.enableChong", getPara("enableChong"));
			config.setProperty("system.meiyuantormb", getPara("meiyuantormb"));
			config.setProperty("system.phoneusercount", getPara("phoneusercount"));
			config.setProperty("system.chushoufee", getPara("chushoufee"));
			config.setProperty("system.chushouPingTaifee", getPara("chushouPingTaifee"));

			config.setProperty("system.chushouliang", getPara("chushouliang"));
			config.setProperty("system.chushoubei", getPara("chushoubei"));
			config.setProperty("system.tixianliang", getPara("tixianliang"));
			config.setProperty("system.tixianbei", getPara("tixianbei"));
			config.setProperty("system.zhuanbiliang", getPara("zhuanbiliang"));
			config.setProperty("system.zhuanbibei", getPara("zhuanbibei"));
			config.setProperty("system.bite_url", getPara("bite_url"));

//			if( BiToRMBEntity.dao.findLast().getBigDecimal("money")!= BigDecimal.valueOf(Double.valueOf(getPara("bitormb")))){
//				BiToRMBEntity entity=new BiToRMBEntity();
//				entity.put("money",getPara("bitormb"));
//				entity.put("create_time", Calendar.getInstance().getTime());
//				entity.save();
//			}
			config.setProperty("system.chushoujia", getPara("chushoujia"));
			config.save();
			com.hfxb.app.core.common.Constants.config = PropertiesUtils.load(
						FileUtils.locateAbsolutePathFromClasspath("main.properties").getName(), "utf-8");

			redirect("/admin/system");
		} catch(Exception e) {
			addError("编辑站点信息出错, " + e.getMessage());
			keepPara();
			render("add.html");
		}

	}
	
}
