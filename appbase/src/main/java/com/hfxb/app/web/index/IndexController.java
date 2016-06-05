package com.hfxb.app.web.index;

import java.util.*;

import com.hfxb.app.core.annotation.Action;
import com.hfxb.app.core.common.Constants;
import com.hfxb.app.core.enums.AccountStateEnums;
import com.hfxb.app.core.enums.BlockTargetEnum;
import com.hfxb.app.core.enums.MoneyTypeEnum;
import com.hfxb.app.core.enums.RoleEnum;
import com.hfxb.app.core.model.Pagination;
import com.hfxb.app.core.model.SystemInfo;
import com.hfxb.app.core.render.YiCaptchaRender;
import com.hfxb.app.core.utils.ParamUtils;
import com.hfxb.app.web.account.entity.AccountEntity;
import com.hfxb.app.web.account.entity.MoneyEntity;
import com.hfxb.app.web.base.BaseController;
import com.hfxb.app.web.news.entity.NewsEntity;
import com.hfxb.app.web.web.entity.BannerEntity;
import com.hfxb.app.web.web.entity.WebEntity;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jfinal.core.Const;

@Action(action = "/")
public class IndexController extends BaseController {

	private static final Logger logger = LoggerFactory.getLogger(IndexController.class);

	public void index() {
		setHeader();
		List<WebEntity> list= WebEntity.dao.getEnable();
		setAttr("list",list);
		List<BannerEntity> banner= BannerEntity.dao.getEnable();
		setAttr("site", getSite());
		setAttr("banner", banner);
		render("/themes/"+getSite().get("theme")+"/index.html");
	}
	public void indexh() {
		setHeader();
		List<WebEntity> list= WebEntity.dao.getEnable();
		setAttr("list",list);
		List<BannerEntity> banner= BannerEntity.dao.getEnable();
		setAttr("site", getSite());
		setAttr("banner", banner);
		render("/themes/"+getSite().get("theme")+"/index_h.html");
	}
	public void indexe() {
		setHeader();
		List<WebEntity> list= WebEntity.dao.getEnable();
		setAttr("list",list);
		List<BannerEntity> banner= BannerEntity.dao.getEnable();
		setAttr("site", getSite());
		setAttr("banner", banner);
		render("/themes/"+getSite().get("theme")+"/index_e.html");
	}
	public  void indexDetail()
	{
		setHeader();
		List<WebEntity> list= WebEntity.dao.getEnable();
		List<BannerEntity> banner= BannerEntity.dao.getEnable();
		setAttr("banner", banner);
		setAttr("list",list);
		setAttr("site", getSite());
		setAttr("id", getParaToInt("id"));
		render("/themes/"+getSite().get("theme")+"/xiangxi.html");
	}

	public void login() {
		forwardAction("/user/login");
	}

	/**
	 * generate verify code
	 */
	public void captcha(){
		YiCaptchaRender captchRender = new YiCaptchaRender();
		render(captchRender);
	}
	
	/**
	 * used for i18n
	 */
	public void setLocal()  {
		String local = getPara(Constants.COOKIE_LOCAL);
		// set default local: CHINA if empty
		local = StringUtils.isBlank(local) ? Locale.CHINA.toString() : local;
		setCookie("Constants.COOKIE_LOCAL", local, Const.DEFAULT_I18N_MAX_AGE_OF_COOKIE);
		renderNull();
	}



	public void test(){
		try
		{
		Db.update("update f_account set state=1,level=1,ji_time=null,level_money=0,has_guoqi=1 where CURDATE()>=ji_end_time");
		long chanbidongjie= Constants.config.getLong("system.chanbidongjie");
		Pagination pager=new Pagination(1,1000000000);
		Map<String, String> p =new HashMap<String, String>();
		p.put("role", RoleEnum.REGISTER_USER.getCode());
		Page<AccountEntity> page=AccountEntity.dao.getPager(pager,p);
		String[] tuanduis=Constants.config.getStringArray("system.tuandui");
		if(page!=null){
			for(AccountEntity account:page.getList()){
				try {
					if(account.getInt("level")>1){
						long money=account.getLong("level_money")*Constants.config.getLong("system.moneyfanbi")/100;
						if(AccountEntity.dao.checkShangXian(account.getLong("id"),money*(100-chanbidongjie)/100)){
							MoneyEntity moneyEntity=new MoneyEntity();
							moneyEntity.put("userid",account.getLong("id"));
							moneyEntity.put("create_time", Calendar.getInstance().getTime());
							moneyEntity.put("type", MoneyTypeEnum.MINING.getCode());
							moneyEntity.put("remark","采矿奖");
							moneyEntity.put("price",money);
							moneyEntity.put("biType",0);
							moneyEntity.put("yuPrice",account.getLong("b1")+account.getLong("b2")+account.getLong("b3")+money);
							moneyEntity.save();
							account.set("b2",account.getLong("b2")+money*chanbidongjie/100);
							account.set("b1",account.getLong("b1")+money*(100-chanbidongjie)/100);
							if(account.getInt("level")>=2){
								String tuandui=tuanduis[account.getInt("level")-2];
								String[] jiang=tuandui.split("\\|");
								long moneys=tuiJiang(0,account.getLong("id").toString(),jiang,0);
								long tuanduijiang=moneys*Constants.config.getLong("system.moneyfanbi")/100;
								MoneyEntity moneyEntity1=new MoneyEntity();
								moneyEntity1.put("userid",account.getLong("id"));
								moneyEntity1.put("create_time", Calendar.getInstance().getTime());
								moneyEntity1.put("type", MoneyTypeEnum.TEAM.getCode());
								moneyEntity1.put("remark","团队奖");
								moneyEntity1.put("price",tuanduijiang);
								moneyEntity1.put("biType",0);
								moneyEntity1.put("yuPrice",account.getLong("b1")+account.getLong("b2")+account.getLong("b3")+tuanduijiang);
								moneyEntity1.save();
								account.set("b2",account.getLong("b2")+tuanduijiang*chanbidongjie/100);
								account.set("b1",account.getLong("b1")+tuanduijiang*(100-chanbidongjie)/100);
							}
							account.update();
						}
					}
				}catch (Exception e){
				}
			}
		}
	} catch (Exception e) {
	}
}

	public  long tuiJiang(int level,String tui_id,String[] jiang,long oldMoney){
		List<AccountEntity> list= AccountEntity.dao.getByTuiJiang(tui_id);
		long money=0;
		StringBuilder s=new StringBuilder();
		int index=0;
		for(AccountEntity account:list){
			s.append(account.getLong("id"));
			if(index!=list.size()-1)
				s.append(",");
			if(account.getInt("level")>1)
				money+=account.getLong("level_money");
		}
		String s1=jiang[level];
		long ss=(money)*Integer.valueOf(s1)/100;
		return (level>=jiang.length-1||tui_id.length()==0)?(oldMoney+ss):tuiJiang(level+1,s.toString(),jiang,oldMoney+ss);
	}
}


	

