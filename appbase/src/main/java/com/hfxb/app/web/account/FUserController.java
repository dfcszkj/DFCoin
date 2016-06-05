package com.hfxb.app.web.account;

import com.hfxb.app.core.annotation.Action;
import com.hfxb.app.core.common.Constants;
import com.hfxb.app.core.enums.*;
import com.hfxb.app.core.model.Pagination;
import com.hfxb.app.core.utils.ParamUtils;
import com.hfxb.app.web.account.entity.*;
import com.hfxb.app.web.base.BaseController;
import com.hfxb.app.web.news.entity.NewsEntity;
import com.jfinal.ext.render.CaptchaRender;
import com.jfinal.plugin.activerecord.Page;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Action(action = "/admin/fuser")
public class FUserController extends BaseController {
	
	private static final Logger logger = LoggerFactory.getLogger(FUserController.class);
	
	/**
	 * 进入仪表盘页面
	 */
	public void dashboard() {
		render("/admin/index.html");
	}
	
	/**
	 * 进入账户管理首页
	 */
	public void index() {
		list();
	}
	
	/**
	 * 进入账户管理首页
	 */
	public void list() {
		Pagination pager = getPager();
		Page<AccountEntity> page = null;
		try {
			Map<String, String> p =ParamUtils.translateMap(getParaMap());
			p.put("role",RoleEnum.REGISTER_USER.getCode());
			page = AccountEntity.dao.getPager(pager, p);
			setAttr("page", page);
			setAttr("stateMap", PostStateEnum.toMap());
			setAttr("roleMap", RoleEnum.toMap());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		keepPara();
		render("/admin/fuser/list.html");
	}
	public void chong() {
		String id = getPara("id");
		if(StringUtils.isNotBlank(id)) {
			setAttr("account", AccountEntity.dao.findById(id));
		}
		render("/admin/fuser/chong.html");
	}
	/**
	 * 进入账户管理首页
	 */
	public void leavemsg() {
		setHeader();
		List<MsgTypeEntity> msgTypeList=MsgTypeEntity.dao.getAll();
		Pagination pager = getPager();
		Page<MsgEntity> page = null;
		Map<String,String> p=ParamUtils.translateMap(getParaMap());
		page = MsgEntity.dao.getPager(pager,p );
		setAttr("page", page);
		setAttr("typeList", msgTypeList);
		setAttr("site", getSite());
		render("/admin/fuser/leavemsg.html");
	}
	
	public void edit() {
		String id = getPara("id");
		if(StringUtils.isNotBlank(id)) {
			setAttr("account", AccountEntity.dao.findById(id));
		}
		setAttr("stateMap", AccountStateEnums.toMap());
		setAttr("roleMap", RoleEnum.toMap());
		render("/admin/fuser/edit.html");
	}
	
	public void doEdit() {
		AccountEntity account = getModel(AccountEntity.class);
//		if(StringUtils.isNotBlank(account.getStr("passwd"))){
//			account.set("passwd", com.hfxb.app.core.utils.DigestUtils.md5Hex(account.getStr("passwd")));
//		}
		account.update();
		redirect("/admin/fuser");
	}
	public void chushoubi(){
		Pagination pager = getPager();
		Page<ChuShouEntity> page = null;
		Map<String,String> p= ParamUtils.translateMap(getParaMap());
		page = ChuShouEntity.dao.getPager(pager,p);
		setAttr("page",page);
		setAttr("ChouShouStateMap", ChouShouStateEnums.toMap());
		render("/admin/fuser/chushoubi.html");
	}

	public  void lock(){
		AccountEntity entity = AccountEntity.dao.findById(getPara("id"));
		if(entity!=null) {
			if(null==entity.getInt("lockstate")||entity.getInt("lockstate") ==0){
				entity.set("lockstate", 1);
			} else {
				entity.set("lockstate", 0);
			}
			entity.update();
			redirect("/admin/fuser/list");
		}
	}

	public  void goumaibilock(){
		GouMaiEntity entity = GouMaiEntity.dao.findById(getPara("id"));
		if(entity!=null) {
			if(entity.getInt("state") ==1){
				entity.set("state", 2);
			} else {
				entity.set("state", 1);
			}
			entity.update();
			redirect("/admin/fuser/goumaibi");
		}
	}

	public  void tixianlock(){
		TiXianEntity entity = TiXianEntity.dao.findById(getPara("id"));
		if(entity!=null) {
			if(entity.getInt("status") ==1){
				entity.set("status", 2);
			} else {
				entity.set("status", 1);
			}
			entity.update();
			redirect("/admin/fuser/tixian");
		}
	}

	public void tixian(){
		Pagination pager = getPager();
		Page<TiXianEntity> page = null;
		Map<String,String> p= ParamUtils.translateMap(getParaMap());
		page = TiXianEntity.dao.getPager(pager,p);
		setAttr("tiXianMap", TiXianEnum.toMap());
		setAttr("page",page);
		render("/admin/fuser/tixian.html");
	}

	public  void chushoulock(){
		ChuShouEntity chuShouEntity=ChuShouEntity.dao.findById(getPara("id"));
		int status=getParaToInt("state");
		if(chuShouEntity!=null){

				AccountEntity accc=AccountEntity.dao.findById(chuShouEntity.getLong("userid"));
				if(accc!=null){
					if(AccountEntity.dao.checkShangXian(accc.getLong("id"),chuShouEntity.getLong("money"))){
						chuShouEntity.set("state",status);
						chuShouEntity.update();
						redirect("/admin/fuser/chushoubi");

//
//						if(status==3||status==6){
//							if(chuShouEntity.getInt("state")==3||chuShouEntity.getInt("state")==6) {
//								redirect("/admin/fuser/chushoubi");
//							}else{
//								accc.set("b1",accc.getLong("b1")+chuShouEntity.getLong("money"));
//								accc.update();
//								chuShouEntity.set("state",status);
//								chuShouEntity.update();
//								redirect("/admin/fuser/chushoubi");
//							}
//
//						}else{
//
//						}
					}else{
						addError("钱包已满!");
						keepPara();
						forwardAction("/admin/fuser/chushoubi");
					}
				}else{
					addError("出售用户不存在!");
					keepPara();
					forwardAction("/admin/fuser/chushoubi");
				}

		}else{
			addError("出售记录不存在!");
			keepPara();
			forwardAction("/admin/fuser/chushoubi");
		}

	}
	public void goumaibi(){
		Pagination pager = getPager();
		Page<GouMaiEntity> page = null;
		Map<String,String> p= ParamUtils.translateMap(getParaMap());
		page = GouMaiEntity.dao.getPager(pager,p);
		setAttr("page",page);
		render("/admin/fuser/goumaibi.html");
	}


	/**
	 * del account
	 */
	public void delete() {
		//id存在说明删除单篇文章， 否则说明批量删除
		String id = getPara("id");
		try {
			if(StringUtils.isNotBlank(id)) {
				String[] idArr = id.split(",");
				for(String i : idArr) {
					AccountEntity.dao.delete(i);
//					AccountEntity entity=AccountEntity.dao.findById(i);
//					entity.set("lockstate", 2);
//					entity.update();
				}
			}
		} catch (Exception e) {
			addError("删除用户失败， 用户ID： [" + id + "]" + e.getMessage());
			logger.error("删除用户失败， 用户ID： [" + id + "]" + e.getMessage(), e);
			keepPara();
		} finally {
			redirect("/admin/fuser");
		}
	}

	public void save() {
		AccountEntity account = getModel(AccountEntity.class);
		account.set("role", RoleEnum.REGISTER_USER.getCode());
		account.set("b1", 0);
		account.set("b2", 0);
		account.set("b3", 0);
		account.set("level_money", 0);
		account.set("logincount", 0);
		account.set("state", AccountStateEnums.UNACTIVE.getCode());
		account.set("level", AccountLevelEnums.LEVEL0.getCode());
		account.set("create_time", Calendar.getInstance().getTime());
		account.set("passwd", com.hfxb.app.core.utils.DigestUtils.md5Hex(account.getStr("passwd")));
		account.save();
		redirect("/admin/fuser");
	}
	public void add() {
		setAttr("stateMap", AccountStateEnums.toMap());
		render("/admin/fuser/edit.html");
	}
}
