package com.hfxb.app.web.account;

import com.hfxb.app.core.annotation.Action;
import com.hfxb.app.core.common.Constants;
import com.hfxb.app.core.enums.AccountLevelEnums;
import com.hfxb.app.core.enums.AccountStateEnums;
import com.hfxb.app.core.enums.PostStateEnum;
import com.hfxb.app.core.enums.RoleEnum;
import com.hfxb.app.core.model.Pagination;
import com.hfxb.app.core.utils.ParamUtils;
import com.hfxb.app.web.account.entity.AccountEntity;
import com.hfxb.app.web.base.BaseController;
import com.jfinal.ext.render.CaptchaRender;
import com.jfinal.plugin.activerecord.Page;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.relation.Role;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

@Action(action = "/admin/account")
public class AccountController extends BaseController {
	
	private static final Logger logger = LoggerFactory.getLogger(AccountController.class);
	
	/**
	 * 进入仪表盘页面
	 */
	public void dashboard() {
		redirect("/admin/system");
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
			Map<String, String> p=ParamUtils.translateMap(getParaMap());
			p.put("role", RoleEnum.MANAGER.getCode());
			page = AccountEntity.dao.getPager(pager, p);
			setAttr("page", page);
			setAttr("stateMap", PostStateEnum.toMap());
			setAttr("roleMap", RoleEnum.toMap());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		keepPara();
		render("/admin/account/list.html");
	}
	
	public void edit() {
		String id = getPara("id");
		if(StringUtils.isNotBlank(id)) {
			setAttr("account", AccountEntity.dao.findById(id));
		}
		setAttr("roleMap", RoleEnum.toMap());
		render("/admin/account/edit.html");
	}
	
	public void doEdit() {
		AccountEntity account = getModel(AccountEntity.class);
		if(StringUtils.isNotBlank(account.getStr("passwd"))){
			account.set("passwd", com.hfxb.app.core.utils.DigestUtils.md5Hex(account.getStr("passwd")));
		}
		account.update();
		redirect("/admin/account");
	}
	
	/**
	 * lock account
	 */
	public void lock() {
		//id存在说明删除单篇文章， 否则说明批量删除

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
				}
			}
		} catch (Exception e) {
			addError("删除用户失败， 用户ID： [" + id + "]" + e.getMessage());
			logger.error("删除用户失败， 用户ID： [" + id + "]" + e.getMessage(), e);
			keepPara();
		} finally {
			redirect("/admin/account");
		}
	}
	
	/**
	 * unlock account
	 */
	public void unlock() {

	}

	/**
	 * get 请求转到登录页面
	 * 其他请求执行登录操作
	 */
	public void login() {
		setHeader();
		Subject currentUser = SecurityUtils.getSubject();
		//if agent has not login, return the index page
		if(currentUser.isAuthenticated()) {
			redirect("/admin/account/dashboard");
		} else {
			render("/admin/account/login.html");
		}
	}
	
	public void doLogin() {
		String vcode = getPara(Constants.VERIFY_CODE);
		//if verify code is blank or not equals verify code in session, return login.html
		if(CaptchaRender.validate(this, vcode.toUpperCase(), Constants.VERIFY_CODE)) {
			addError("verify code error!");
			this.keepPara();
			forwardAction(Constants.ADMIN_LOGIN_URL);
		} else {
			Subject currentUser = SecurityUtils.getSubject();
			//if agent has not login, return the index page
			if(currentUser.isAuthenticated()) {
				redirect("/admin/account/dashboard");
			} else {
				UsernamePasswordToken token = new UsernamePasswordToken(getPara("login_name"),  com.hfxb.app.core.utils.DigestUtils.md5Hex(getPara("passwd")));
				if(token != null && getParaToBoolean("remeberme", false)) {
					token.setRememberMe(true);
				}
	            try {
	                currentUser.login(token);
	                redirect("/admin/account/dashboard");
	            } catch (UnknownAccountException e) {
	            	addError("用户名 ["+token.getPrincipal()+"] 不存在!");
	            	logger.error(e.getMessage(), e);
	            	forwardAction(Constants.ADMIN_LOGIN_URL);
	            } catch (IncorrectCredentialsException e) {
	            	addError("密码错误!");
	            	logger.error(e.getMessage(), e);
	            	forwardAction(Constants.ADMIN_LOGIN_URL);
	            } catch (LockedAccountException e) {
	            	addError("user ["+token.getPrincipal()+"] locked!");
	            	logger.error(e.getMessage(), e);
	            	forwardAction(Constants.ADMIN_LOGIN_URL);
	            } catch (Exception e) {
	                addError("unknow error: " + e.getMessage());
	                logger.error(e.getMessage(), e);
	                forwardAction(Constants.ADMIN_LOGIN_URL);
	            }
			}
		}
	}
	public void add() {
		render("/admin/account/add.html");
	}
	public void save() {
		AccountEntity account = getModel(AccountEntity.class);
		account.set("role", RoleEnum.MANAGER.getCode());
		account.set("b1", 0);
		account.set("b2", 0);
		account.set("b3", 0);
		account.set("logincount", 0);
		account.set("state", AccountStateEnums.UNACTIVE.getCode());
		account.set("level", AccountLevelEnums.LEVEL0.getCode());
		account.set("create_time", Calendar.getInstance().getTime());
		account.set("passwd", com.hfxb.app.core.utils.DigestUtils.md5Hex(account.getStr("passwd")));
		account.save();
		redirect("/admin/account");
	}

	public void regist() {
		render("/admin/account/regist.html");
	}

	public void doRegist(){
		AccountEntity account = getModel(AccountEntity.class);
		try {
			if(AccountEntity.dao.getByName(account.getStr("login_name")) != null) {
				addError("用户 [" + account.getStr("login_name") + "] 已存在!");
				keepPara();
				render("regist.html");
			} else if(StringUtils.isBlank(account.getStr("passwd"))) {
				addError("密码不能为空!");
				keepPara();
				render("regist.html");
			} else {
				account.set("role", RoleEnum.MANAGER.getCode());
				account.set("passwd", com.hfxb.app.core.utils.DigestUtils.md5Hex(account.getStr("passwd")));
				account.set("create_time", Calendar.getInstance().getTime());
				if(account.save()) {
					redirect(Constants.ADMIN_LOGIN_URL);
				} else {

				}
			}
		} catch (Exception e) {
			addError("create [" + account.getStr("login_name") + "] error!");
			keepPara();
			render("regist.html");
		}
	}
	
	/**
	 * ajax判断用户是否可注册
	 */
	public void exists() {
		Map<String,Object> obj = new HashMap<String,Object>();
		obj.put("exist", AccountEntity.dao.userExist(getPara("key")));
		renderJson(obj);
	}
	
	/**
	 *  用户退出登录
	 */
	public void logout(){
		SecurityUtils.getSubject().logout();
		getSession().invalidate();
		redirect(Constants.ADMIN_LOGIN_URL);
	}
}
