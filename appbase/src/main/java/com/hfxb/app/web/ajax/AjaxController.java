package com.hfxb.app.web.ajax;

import com.hfxb.app.core.annotation.Action;
import com.hfxb.app.core.common.Constants;
import com.hfxb.app.core.enums.ChouShouStateEnums;
import com.hfxb.app.core.enums.MoneyTypeEnum;
import com.hfxb.app.core.enums.RoleEnum;
import com.hfxb.app.core.helper.DuoshuoHelper;
import com.hfxb.app.core.utils.HttpUtils;
import com.hfxb.app.web.account.entity.AccountEntity;
import com.hfxb.app.web.account.entity.ChuShouEntity;
import com.hfxb.app.web.account.entity.MoneyEntity;
import com.hfxb.app.web.account.entity.MsgEntity;
import com.hfxb.app.web.base.BaseController;
import com.jfinal.ext.render.CaptchaRender;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

@Action(action = "/ajax")
public class AjaxController extends BaseController {
	
	private static final Logger logger = LoggerFactory.getLogger(AjaxController.class);

	public void backMsg(){
		long id=getParaToLong("id");
		String password = getPara("content");
		Map<String,Object> result = new HashMap<String,Object>();
		try {
			MsgEntity  	accc=MsgEntity.dao.findById(id);
			accc.set("backcontent",password);
			accc.set("back_time",Calendar.getInstance().getTime());
			accc.update();
			result.put("message", "回复成功");
			result.put("result", true);
		} catch (Exception e) {
			result.put("message", "回复失败!");
			result.put("result", false);
		}
		renderJson(result);
	}

	public  void goumaibi(){
		long id=getParaToLong("id");
		String password = getPara("safePass");
		Map<String,Object> result = new HashMap<String,Object>();
		try {
			AccountEntity accc= (AccountEntity) getSession().getAttribute(Constants.FRONT_LOGIN_USER);
			accc=AccountEntity.dao.findById(accc.get("id"));
			if(!com.hfxb.app.core.utils.DigestUtils.md5Hex(password).equals(accc.getStr("safePass")))
			{
				result.put("message", "资金密码错误!");
				result.put("result", false);
			}else {
				ChuShouEntity chuShouEntity=ChuShouEntity.dao.findById(id);
				if(chuShouEntity!=null){
					Long money=chuShouEntity.getLong("money");
					int state=chuShouEntity.getInt("state");
					if(state== ChouShouStateEnums.CACELED.getCode()){
						result.put("message", "该记录已被取消!");
						result.put("result", false);
					}else if(state== ChouShouStateEnums.SALEED.getCode()){
						result.put("message", "该记录已售出!");
						result.put("result", false);
					}else if(accc.getLong("b3")<money){
						result.put("message", "钱包币不足!");
						result.put("result", false);
					}else if(state== ChouShouStateEnums.ONSALE.getCode()){
						if(!AccountEntity.dao.checkShangXian(accc.getLong("id"),chuShouEntity.getLong("money"))){
							result.put("message", "钱包已满");
							result.put("result", false);
						}else{
							accc.set("b1",accc.getLong("b1")+money);
							accc.set("b3",accc.getLong("b3")-money);
							accc.update();
							chuShouEntity.set("state",ChouShouStateEnums.SALEED.getCode());
							chuShouEntity.set("buyuserid",accc.get("id"));
							chuShouEntity.set("buy_time",Calendar.getInstance().getTime());
							chuShouEntity.set("buy_money",money);
							chuShouEntity.update();
							AccountEntity acccBei=AccountEntity.dao.findById(chuShouEntity.getLong("userid"));
							acccBei.set("b3",acccBei.getLong("b3")+money);
							acccBei.update();
							result.put("message", "购买成功");
							result.put("result", true);
						}
					}
				}else{
					result.put("message", "该记录不存在或已被删除!");
					result.put("result", false);
				}
			}
		} catch (Exception e) {
			result.put("message", "购买失败!");
			result.put("result", false);
		}
		renderJson(result);
	}


	public void index() {
		
	}

	public void getZhang(){
			Map<String,Object> result = new HashMap<String,Object>();
			String userid=getPara("userid");
			try {
				AccountEntity  accc=AccountEntity.dao.findByLoginName(userid);
				if(accc==null){
					result.put("message", "账户不存在!");
					result.put("result", false);
				}else{
					result.put("data",accc);
					result.put("message", "账户充值成功!");
					result.put("result", true);
				}
			} catch (Exception e) {
				result.put("message", "账户不存在!");
				result.put("result", false);
			}
			renderJson(result);
		}

	public void login() {
		Map<String,Object> result = new HashMap<String,Object>();
		String email = getPara("login_name");
		String password = com.hfxb.app.core.utils.DigestUtils.md5Hex(getPara("password"));
        try {
			String vcode = getPara(Constants.VERIFY_CODE);
			if(CaptchaRender.validate(this, vcode.toUpperCase(), Constants.VERIFY_CODE)) {
				result.put("message", "验证码错误!");
				result.put("result", false);
			} else {
				AccountEntity account = AccountEntity.dao.getByName(email);
				if(account == null||(account.getInt("lockstate")!=null&&account.getInt("lockstate")==2)){
					result.put("message", "用户不存在!");
					result.put("result", false);
				} else if(!password.equals(account.get("passwd"))) {
					result.put("message", "密码错误!");
					result.put("result", false);
				}  else if(account.getInt("lockstate")!=null&&account.getInt("lockstate")==1) {
					result.put("message", "账号已被锁定!");
					result.put("result", false);
				} else {
					String ip=HttpUtils.getIpAddr(getRequest());
					account.set("login_time", Calendar.getInstance().getTime());
					account.set("login_ip", ip);
					account.set("logincount",account.getInt("logincount")+1);
					account.update();
					account.put("tui_count",AccountEntity.dao.getChildCount(account.getLong("id")));
					getSession().setAttribute(Constants.FRONT_LOGIN_USER, account);
					result.put("result", true);
				}
			}
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            result.put("result", false);
            result.put("message", "unknow error:" + e.getMessage());
		}
		renderJson(result);
	}

	public void setPass(){
		Map<String,Object> result = new HashMap<String,Object>();
		String secondPass = getPara("secondPass");
		String safePass = getPara("safePass");
		try {
			AccountEntity accountEntity=getModel(AccountEntity.class);
			AccountEntity accc= (AccountEntity) getSession().getAttribute(Constants.FRONT_LOGIN_USER);
			accc=AccountEntity.dao.findById(accc.get("id"));
			accc.set("secondPass",com.hfxb.app.core.utils.DigestUtils.md5Hex(secondPass));
			accc.set("safePass",com.hfxb.app.core.utils.DigestUtils.md5Hex(safePass));
			accc.update();
			result.put("message", "密码设置成功!");
			result.put("result", true);

			getSession().setAttribute(Constants.FRONT_LOGIN_USER, accc);

		} catch (Exception e) {
			logger.error("登录密码修改失败!" + e.getMessage(), e);
			result.put("message", "登录密码修改失败!");
			result.put("result", false);
		}
		renderJson(result);
	}

	public void doPassEdit(){
		Map<String,Object> result = new HashMap<String,Object>();
		String password = getPara("oldPass");
		String newPass = getPara("password");
		String secondPass = getPara("secondPass");
		try {
			AccountEntity accountEntity=getModel(AccountEntity.class);
			AccountEntity accc= (AccountEntity) getSession().getAttribute(Constants.FRONT_LOGIN_USER);
			accc=AccountEntity.dao.findById(accc.get("id"));
			if(!com.hfxb.app.core.utils.DigestUtils.md5Hex(secondPass).equals(accc.getStr("secondPass")))
			{
				result.put("message", "二级密码错误!");
				result.put("result", false);
			}else if(!com.hfxb.app.core.utils.DigestUtils.md5Hex(password).equals(accc.getStr("passwd")))
			{
				result.put("message", "原密码错误!");
				result.put("result", false);
			}else{
				accc.set("passwd",com.hfxb.app.core.utils.DigestUtils.md5Hex(newPass));
				accc.update();
				result.put("message", "密码修改成功!");
				result.put("result", true);
			}

		} catch (Exception e) {
			logger.error("登录密码修改失败!" + e.getMessage(), e);
			result.put("message", "登录密码修改失败!");
			result.put("result", false);
		}
		renderJson(result);
	}
	public void doSecondPassEdit(){
		Map<String,Object> result = new HashMap<String,Object>();
		String password = getPara("oldPass");
		String newPass = getPara("password");
		try {
			AccountEntity accountEntity=getModel(AccountEntity.class);
			AccountEntity accc= (AccountEntity) getSession().getAttribute(Constants.FRONT_LOGIN_USER);
			accc=AccountEntity.dao.findById(accc.get("id"));
			if(!com.hfxb.app.core.utils.DigestUtils.md5Hex(password).equals(accc.getStr("secondPass")))
			{
				result.put("message", "原密码错误!");
				result.put("result", false);
			}else{
				accc.set("secondPass",com.hfxb.app.core.utils.DigestUtils.md5Hex(newPass));
				accc.update();
				result.put("message", "二级密码修改成功!");
				result.put("result", true);
			}

		}  catch (Exception e) {
			logger.error("二级密码修改失败!" + e.getMessage(), e);
			result.put("message", "二级密码修改失败!");
			result.put("result", false);
		}
		renderJson(result);
	}
	public void doSafePassEdit(){
		Map<String,Object> result = new HashMap<String,Object>();
		String password = getPara("oldPass");
		String newPass = getPara("password");
		try {
			AccountEntity accountEntity=getModel(AccountEntity.class);
			AccountEntity accc= (AccountEntity) getSession().getAttribute(Constants.FRONT_LOGIN_USER);
			accc=AccountEntity.dao.findById(accc.get("id"));
			if(!com.hfxb.app.core.utils.DigestUtils.md5Hex(password).equals(accc.getStr("safePass")))
			{
				result.put("message", "原密码错误!");
				result.put("result", false);
			}else{
				accc.set("safePass",com.hfxb.app.core.utils.DigestUtils.md5Hex(newPass));
				accc.update();
				result.put("message", "资金密码修改成功!");
				result.put("result", true);
			}

		} catch (Exception e) {
			logger.error("资金密码修改失败!" + e.getMessage(), e);
			result.put("message", "资金密码修改失败!");
			result.put("result", false);
		}
		renderJson(result);
	}

	public void doChong(){
		Map<String,Object> result = new HashMap<String,Object>();
		long userid=getParaToLong("userid");
		int bType=getParaToInt("chongSelect");
		long money=getParaToLong("money");
		try {
			AccountEntity  accc=AccountEntity.dao.findById(userid);
			if(accc==null){
				result.put("message", "账户不存在!");
				result.put("result", false);
			}else{
				MoneyEntity moneyEntity=new MoneyEntity();
				moneyEntity.put("userid",accc.getLong("id"));
				moneyEntity.put("create_time",Calendar.getInstance().getTime());
				moneyEntity.put("type", MoneyTypeEnum.RECHARGE.getCode());
				moneyEntity.put("remark","充值费用"+money);
				moneyEntity.put("price",money);
				moneyEntity.put("biType",bType);
				moneyEntity.put("yuPrice",accc.getLong("b"+bType)+money);
				moneyEntity.save();

				accc.set("b"+bType,(accc.getLong("b"+bType)+money));
				accc.update();
				result.put("message", "账户充值成功!");
				result.put("result", true);
			}
		} catch (Exception e) {
			logger.error("账户充值失败!" + e.getMessage(), e);
			result.put("message", "账户充值失败!");
			result.put("result", false);
		}
		renderJson(result);
	}

	public void bind() {
		regist();
	}
	
	public void regist(){
		Map<String,Object> result = new HashMap<String,Object>();
		String userName = getPara("username");
		String password = getPara("user_pass");
		try {
			if(AccountEntity.dao.getByName(userName) != null) {
				result.put("message", "用户 [" + userName + "] 已存在!");
				result.put("result", false);
			}  else if(StringUtils.isBlank(password)) {
				 result.put("message", "密码不能为空!");
				result.put("result", false);
			} else {
				password=com.hfxb.app.core.utils.DigestUtils.md5Hex(password);
				String phone=getPara("phone");
				String truename=getPara("truename");
				String card_id=getPara("card_id");
				AccountEntity account = new AccountEntity();
				account.set("login_name", userName);
				account.set("b1", 0);
				account.set("b2", 0);
				account.set("b3", 0);
				account.set("phone", phone);
				account.set("truename", truename);
				account.set("card_id", card_id);
				AccountEntity accc= (AccountEntity) getSession().getAttribute(Constants.FRONT_LOGIN_USER);
				account.set("tui_id", accc.getInt("id"));
				account.set("role", RoleEnum.REGISTER_USER.getCode());
				account.set("passwd", password);
				account.set("create_time", Calendar.getInstance().getTime());
				account.save();
				result.put("message", "注册成功!");
				result.put("result", true);
			}
		} catch (Exception e) {
			logger.error("注册新用户 [" + userName + "] 失败，" + e.getMessage(), e);
			result.put("message", "注册新用户 [" + userName + "] 失败!");
			result.put("result", false);
		}
		renderJson(result);
	}
	
	/**
	 * ajax判断用户是否可注册
	 */
	public void userExist() {
		renderJson(AccountEntity.dao.userExist(getPara("key")));
	}

	/**
	 *  用户退出登录
	 */
	public void logout(){
		Map<String,Object> obj = new HashMap<String,Object>();
		getSession().invalidate();
		obj.put("result", true);
		renderJson(obj);
	}
	
	public void subscribe() {
//		String email = getPara("email");
		
	}

}
