package com.hfxb.app.web.account;

import com.hfxb.app.core.annotation.Action;
import com.hfxb.app.core.common.Constants;
import com.hfxb.app.core.enums.*;
import com.hfxb.app.core.helper.DuoshuoHelper;
import com.hfxb.app.core.mail.MailSenderFactory;
import com.hfxb.app.core.model.Pagination;
import com.hfxb.app.core.utils.DesUtils;
import com.hfxb.app.core.utils.FileUtils;
import com.hfxb.app.core.utils.ParamUtils;
import com.hfxb.app.core.utils.StringUtils;
import com.hfxb.app.web.account.entity.*;
import com.hfxb.app.web.base.BaseController;
import com.hfxb.app.web.news.entity.NewsEntity;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.upload.UploadFile;
import com.qq.connect.QQConnectException;
import com.qq.connect.api.OpenID;
import com.qq.connect.javabeans.AccessToken;
import com.qq.connect.oauth.Oauth;
import com.sun.xml.internal.messaging.saaj.util.FinalArrayList;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.configuration.SystemConfiguration;
import org.apache.shiro.authc.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.*;

@Action(action = "/user")
public class UserController extends BaseController {
	
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	
	/**
	 * 进入账户管理首页
	 */
	public void index() {
		setHeader();
		AccountEntity accc= (AccountEntity) getSession().getAttribute(Constants.FRONT_LOGIN_USER);
		Pagination meiripager = getPager();
		meiripager.setPn(1);
		meiripager.setSize(5);
		Page<MoneyEntity> meiriPage = null;
		Map<String,String> meirip= ParamUtils.translateMap(getParaMap());
		meirip.put("userid",accc.getLong("id").toString());
		meiriPage = MoneyEntity.dao.getPagerGroup(meiripager,meirip);
		setAttr("meiriPage",meiriPage);

		Pagination mingxipager = getPager();
		mingxipager.setPn(1);
		mingxipager.setSize(5);
		Page<MoneyEntity> mingxipage = null;
		Map<String,String> mingxip= ParamUtils.translateMap(getParaMap());
		mingxip.put("userid",accc.getLong("id").toString());
		mingxipage = MoneyEntity.dao.getPager(mingxipager,mingxip);
		setAttr("mingxipage",mingxipage);


		Pagination gonggaopager = getPager();
		gonggaopager.setPn(1);
		gonggaopager.setSize(4);
		Page<NewsEntity> gonggaopage = null;
		Map<String,String> gonggaop=ParamUtils.translateMap(getParaMap());
		gonggaop.put("type","2");
		gonggaopage = NewsEntity.dao.getPager(gonggaopager,gonggaop );
		setAttr("gonggaopage",gonggaopage);
		setAttr("moneyTypeMap", MoneyTypeEnum.toMap());
		setAttr("accountStateMap", AccountStateEnums.toMap());
		render("/themes/"+getSite().get("theme")+"/user/index.html");
	}

	public void login() {
//		if(getFrontUser() != null) {
//			redirect("/");
//		} else {
		setHeader();

		render("/themes/"+getSite().get("theme")+"/user/login.html");
//		}
	}

	public void doZhuanBi(){
		ZhuanBiEntity zhuanBiEntity=getModel(ZhuanBiEntity.class);
		AccountEntity accc= (AccountEntity) getSession().getAttribute(Constants.FRONT_LOGIN_USER);
		String pass=getPara("safepass");
		accc=AccountEntity.dao.findById(accc.get("id"));
		try {

			AccountEntity accountToEntity=AccountEntity.dao.findByLoginName(getPara("zhuanZhangHao"));
			if(accountToEntity== null) {
				addError("转币用户不存在!");
				setAttr("skip",1);
				keepPara();
				forwardAction("/user/zhuanBi");
			} else if(accc.getLong("b1")<zhuanBiEntity.getLong("money")) {
				addError("当前余额不足!");
				setAttr("skip",1);
				keepPara();
				forwardAction("/user/zhuanBi");
			}else if(!com.hfxb.app.core.utils.DigestUtils.md5Hex(pass).equals(accc.getStr("safePass")))
			{
				addError("资金密码错误!");
				setAttr("skip",1);
				keepPara();
				forwardAction("/user/zhuanBi");
			}else if(!AccountEntity.dao.checkShangXian(accountToEntity.getLong("id"),zhuanBiEntity.getLong("money"))){
				addError("对方钱包上限已满,无法转币!");
				setAttr("skip",1);
				keepPara();
				forwardAction("/user/zhuanBi");
			}else
			{
				accc.set("b1",accc.getLong("b1")-zhuanBiEntity.getLong("money"));
//				MoneyEntity moneyEntity=new MoneyEntity();
//				moneyEntity.put("userid",accc.getLong("id"));
//				moneyEntity.put("create_time",Calendar.getInstance().getTime());
//				moneyEntity.put("type",MoneyTypeEnum.ZHUAN.getCode());
//				moneyEntity.put("remark","转币支出");
//				moneyEntity.put("price",-zhuanBiEntity.getLong("money"));
//				moneyEntity.put("biType",1);
//				moneyEntity.put("yuPrice",accc.getLong("b1")-zhuanBiEntity.getLong("money"));
//				moneyEntity.save();
//
//				MoneyEntity moneyEntity1=new MoneyEntity();
//				moneyEntity1.put("userid",accountToEntity.getLong("id"));
//				moneyEntity1.put("create_time",Calendar.getInstance().getTime());
//				moneyEntity1.put("type",MoneyTypeEnum.ZHUANDE.getCode());
//				moneyEntity1.put("remark","转币获得");
//				moneyEntity1.put("price",zhuanBiEntity.getLong("money"));
//				moneyEntity1.put("biType",1);
//				moneyEntity1.put("yuPrice",accountToEntity.getLong("b1")+zhuanBiEntity.getLong("money"));
//				moneyEntity1.save();
				zhuanBiEntity.put("create_time",Calendar.getInstance().getTime());
				zhuanBiEntity.put("userid",accc.getLong("id"));
				zhuanBiEntity.put("create_time",Calendar.getInstance().getTime());
				zhuanBiEntity.put("touserid",accountToEntity.getLong("id"));
				zhuanBiEntity.save();
				accountToEntity.set("b1",accountToEntity.getLong("b1")+zhuanBiEntity.getLong("money"));
				accc.update();
				getSession().setAttribute(Constants.FRONT_LOGIN_USER, accc);
				accountToEntity.update();
				addError("转币成功!");
				setAttr("skip",1);
				keepPara();
				forwardAction("/user/zhuanBi");
			}
		} catch (Exception e) {
			logger.error("转币失败，" + e.getMessage(), e);
			addError("转币失败!");
			setAttr("skip",1);
			keepPara();
			forwardAction("/user/zhuanBi");
		}
	}

	public void tixian(){
		setHeader();
		AccountEntity accc= (AccountEntity) getSession().getAttribute(Constants.FRONT_LOGIN_USER);

		Pagination pager = getPager();
		Page<TiXianEntity> page = null;
		Map<String,String> p= ParamUtils.translateMap(getParaMap());
		p.put("userid",accc.getLong("id").toString());
		page = TiXianEntity.dao.getPager(pager,p);

		setAttr("page", page);
		setAttr("tiXianMap", TiXianEnum.toMap());
		setAttr("tixianliang", Constants.config.getLong("system.tixianliang"));
		setAttr("tixianbei", Constants.config.getLong("system.tixianbei"));

		render("/themes/"+getSite().get("theme")+"/user/cw_tixian.html");
	}

	public void tixianquxiao(){
		AccountEntity accc= (AccountEntity) getSession().getAttribute(Constants.FRONT_LOGIN_USER);
		accc=AccountEntity.dao.findById(accc.get("id"));
		try {
			TiXianEntity ti=TiXianEntity.dao.findById(getPara("id"));
			if(ti.getInt("status")==Integer.valueOf(TiXianEnum.YICHULI.getCode())){
				ti.set("status",TiXianEnum.YIQUXIAO.getCode());
				ti.update();
				accc.set("b3",accc.getLong("b3")+ti.getLong("money"));
				accc.update();
				addError("取消成功!");
				forwardAction("/user/tixian");
			}else{
				addError("已处理,请刷新查看状态!");
				forwardAction("/user/tixian");
			}

		} catch (Exception e) {
			logger.error("取消失败，" + e.getMessage(), e);
			addError("取消失败!");
			keepPara();
			forwardAction("/user/tixian");
		}
	}

	public void doTiXian(){
		AccountEntity accc= (AccountEntity) getSession().getAttribute(Constants.FRONT_LOGIN_USER);
		accc=AccountEntity.dao.findById(accc.get("id"));
		TiXianEntity zhuanBiEntity=new TiXianEntity();
		zhuanBiEntity.put("create_time",Calendar.getInstance().getTime());
		zhuanBiEntity.put("userid",accc.getLong("id"));
		zhuanBiEntity.put("type",0);
		zhuanBiEntity.put("status",1);
		zhuanBiEntity.put("bite_link",getPara("bite_link"));
		zhuanBiEntity.put("money",getParaToLong("money"));
		String pass=getPara("safepass");
		try {

			if(accc.getLong("b3")<zhuanBiEntity.getLong("money")) {
				addError("当前余额不足!");
				setAttr("skip",1);
				keepPara();
				forwardAction("/user/doTiXian");
			}else if(!com.hfxb.app.core.utils.DigestUtils.md5Hex(pass).equals(accc.getStr("safePass")))
			{
				addError("资金密码错误!");
				setAttr("skip",1);
				keepPara();
				forwardAction("/user/doTiXian");
			}else
			{
				accc.set("b3",accc.getLong("b3")-zhuanBiEntity.getLong("money"));
				zhuanBiEntity.save();
				accc.update();
				getSession().setAttribute(Constants.FRONT_LOGIN_USER, accc);
				redirect("/user/tixian?skip=1");
			}
		} catch (Exception e) {
			logger.error("提现申请失败，" + e.getMessage(), e);
			addError("提现申请失败!");
			setAttr("skip",1);
			keepPara();
			forwardAction("/user/tixian");
		}
	}

	public  void DFCZhuankuangji(){
		setHeader();
		AccountEntity accc= (AccountEntity) getSession().getAttribute(Constants.FRONT_LOGIN_USER);

		Pagination pager = getPager();
		Page<ZhuanMyEntity> page = null;
		Map<String,String> p= ParamUtils.translateMap(getParaMap());
		p.put("userid",accc.getLong("id").toString());
		page = ZhuanMyEntity.dao.getPager(pager,p);
		setAttr("page", page);

		render("/themes/"+getSite().get("theme")+"/user/cw_dfczhuankuangji.html");
	}

	public void doDFCZhuankuangji(){
		AccountEntity accc= (AccountEntity) getSession().getAttribute(Constants.FRONT_LOGIN_USER);
		accc=AccountEntity.dao.findById(accc.get("id"));
		ZhuanMyEntity zhuanBiEntity=new ZhuanMyEntity();
		zhuanBiEntity.put("create_time",Calendar.getInstance().getTime());
		zhuanBiEntity.put("userid",accc.getLong("id"));
		zhuanBiEntity.put("type",0);
		zhuanBiEntity.put("money",getParaToLong("money"));
		String pass=getPara("safepass");
		try {

			if(accc.getLong("b1")<zhuanBiEntity.getLong("money")) {
				addError("当前余额不足!");
				setAttr("skip",1);
				keepPara();
				forwardAction("/user/doDFCZhuankuangji");
			}else if(!com.hfxb.app.core.utils.DigestUtils.md5Hex(pass).equals(accc.getStr("safePass")))
			{
				addError("资金密码错误!");
				setAttr("skip",1);
				keepPara();
				forwardAction("/user/doDFCZhuankuangji");
			}else
			{
				accc.set("b1",accc.getLong("b1")-zhuanBiEntity.getLong("money"));
				accc.set("b2",accc.getLong("b2")+zhuanBiEntity.getLong("money"));
				zhuanBiEntity.save();
				accc.update();
				getSession().setAttribute(Constants.FRONT_LOGIN_USER, accc);
				redirect("/user/DFCZhuankuangji?skip=1");
			}
		} catch (Exception e) {
			logger.error("转币失败，" + e.getMessage(), e);
			addError("转币失败!");
			setAttr("skip",1);
			keepPara();
			forwardAction("/user/DFCZhuankuangji");
		}
	}
	public void goumaibi(){
		setHeader();
		setAttr("user",getFrontUser());
		setAttr("biList", BiToRMBEntity.dao.getNear12());
		setAttr("bi", BiToRMBEntity.dao.findLast());
		setAttr("enableChong", Constants.config.getLong("system.enableChong"));
		setAttr("chushouFeeBi", Constants.config.getLong("system.chushoufee"));
		setAttr("meiyuantormb", Constants.config.getLong("system.meiyuantormb"));
		setAttr("chushouliang", Constants.config.getLong("system.chushouliang"));
		setAttr("chushoubei", Constants.config.getLong("system.chushoubei"));
		setAttr("bite_url", Constants.config.getString("system.bite_url"));

		Pagination pager = getPager();
		Page<ChuShouEntity> page = null;
		Map<String,String> p= ParamUtils.translateMap(getParaMap());
		AccountEntity accc= (AccountEntity) getSession().getAttribute(Constants.FRONT_LOGIN_USER);
		p.put("notuserid",accc.getLong("id").toString());
		p.put("notstate","3");
		page = ChuShouEntity.dao.getPager(pager,p);

		Page<GouMaiEntity> pageMai = GouMaiEntity.dao.getPager(pager,p);
		setAttr("page",page);
		setAttr("pageMai",pageMai);
		render("/themes/"+getSite().get("theme")+"/user/cw_goumaibi.html");
	}

	public  void doGoumaibi(){
		UploadFile file = getFile("icon");
		BiToRMBEntity bili=BiToRMBEntity.dao.findLast();
		long biCount=getParaToLong("money");
		BigDecimal biMoney=bili.getBigDecimal("money");
		AccountEntity accc= (AccountEntity) getSession().getAttribute(Constants.FRONT_LOGIN_USER);
		String pass=getPara("safepass");
		accc=AccountEntity.dao.findById(accc.get("id"));
		int bType=getParaToInt("chongSelect");
		try{
			String filename="";
			if(file!=null){
				filename=FileUtils.saveFile(file.getFile());
			}
			String icon=filename;
			GouMaiEntity goumai=new GouMaiEntity();
			goumai.put("userid",accc.getLong("id"));
			goumai.put("money",biCount);
			goumai.put("type",bType);
			goumai.put("money_tormb",bili.getBigDecimal("money"));
			goumai.put("state",GouMaiStateEnums.SUBMIT.getCode());
			goumai.put("create_time",Calendar.getInstance().getTime());
			goumai.put("img",icon);
			goumai.save();
			redirect("/user/goumaibi");
		}catch (Exception e){
			logger.error("购买失败，" + e.getMessage(), e);
			addError("购买失败!");
			keepPara();
			forwardAction("/user/goumaibi");
		}

	}
	public  void changeUser(){
		String name=getPara("name");
		AccountEntity accc= (AccountEntity) getSession().getAttribute(Constants.FRONT_LOGIN_USER);
		AccountEntity change=AccountEntity.dao.findByLoginName(name);
		if(!accc.getStr("phone").equals(change.getStr("phone"))){
			addError("切换错误,手机号码不一致!");
			keepPara();
			forwardAction("/user");
		}else{
			getSession().setAttribute(Constants.FRONT_LOGIN_USER, change);
			redirect("/user");
		}
	}
	public void chuShouBi(){
		setHeader();
		setAttr("user",getFrontUser());
		setAttr("biList", BiToRMBEntity.dao.getNear12());
		setAttr("bi", BiToRMBEntity.dao.findLast());
		setAttr("chushouFeeBi", Constants.config.getLong("system.chushoufee"));
		setAttr("chushoujia",Constants.config.getStringArray("system.chushoujia"));
		setAttr("chushouliang", Constants.config.getLong("system.chushouliang"));
		setAttr("chushoubei", Constants.config.getLong("system.chushoubei"));
		Pagination pager = getPager();
		Page<ChuShouEntity> page = null;
		Map<String,String> p= ParamUtils.translateMap(getParaMap());
		AccountEntity accc= (AccountEntity) getSession().getAttribute(Constants.FRONT_LOGIN_USER);
		Page<ChuShouEntity> pageAll = ChuShouEntity.dao.getPager(pager,p);
		setAttr("pageAll",pageAll);
		setAttr("ChouShouStateMap", ChouShouStateEnums.toMap());

		p.put("userid",accc.getLong("id").toString());
		page = ChuShouEntity.dao.getPager(pager,p);
		setAttr("page",page);

		Page<ChuShouEntity> chushouMai = ChuShouEntity.dao.getPager(pager,p);
		setAttr("chushouMai",chushouMai);

		render("/themes/"+getSite().get("theme")+"/user/cw_chushoubi.html");
	}

	public void chushouQuXiao(){
		AccountEntity accc= (AccountEntity) getSession().getAttribute(Constants.FRONT_LOGIN_USER);
		ChuShouEntity chuShouEntity=ChuShouEntity.dao.findById(getPara("id"));
		if(AccountEntity.dao.checkShangXian(accc.getLong("id"),chuShouEntity.getLong("money"))){
			if(accc.getLong("id").longValue()==chuShouEntity.getLong("userid").longValue()){
				accc.set("b1",accc.getLong("b1")+chuShouEntity.getLong("money"));
				accc.update();
				chuShouEntity.set("state",2);
				chuShouEntity.update();
				redirect("/user/chuShouBi");
			}else{
				addError("用户没有权限!");
				keepPara();
				forwardAction("/user/chuShouBi");
			}
		}else{
			addError("钱包已满!");
			keepPara();
			forwardAction("/user/chuShouBi");
		}

	}
	public void doChuShouBi(){
		BiToRMBEntity bili=BiToRMBEntity.dao.findLast();
		long biCount=getParaToLong("money");
		Long chushouFeeBi=Constants.config.getLong("system.chushoufee");
		Long chushouPingTaifee=Constants.config.getLong("system.chushouPingTaifee");
		int type=getParaToInt("type");
		long chushouFee=biCount*(type==1?chushouFeeBi:chushouPingTaifee)/100;
		AccountEntity accc= (AccountEntity) getSession().getAttribute(Constants.FRONT_LOGIN_USER);
		String pass=getPara("safepass");
		accc=AccountEntity.dao.findById(accc.get("id"));
		try {

			if (!com.hfxb.app.core.utils.DigestUtils.md5Hex(pass).equals(accc.getStr("safePass"))) {
				addError("资金密码错误!");
				keepPara();
				forwardAction("/user/chuShouBi");
			}else if (accc.getLong("b1") < biCount+chushouFee) {
				addError("当前余额不足!");
				keepPara();
				forwardAction("/user/chuShouBi");
			}else{
				accc.set("b1",accc.getLong("b1")-(biCount+chushouFee));
				accc.update();
				getSession().setAttribute(Constants.FRONT_LOGIN_USER, accc);
				ChuShouEntity chouShou=new ChuShouEntity();
				chouShou.put("userid",accc.get("id"));
				chouShou.put("money",biCount);
				chouShou.put("type",type);
				chouShou.put("chushoufee",chushouFee);
				chouShou.put("feebili",type==1?chushouFeeBi:chushouPingTaifee);
				chouShou.put("state",ChouShouStateEnums.ONSALE.getCode());
				chouShou.put("create_time",Calendar.getInstance().getTime());
				chouShou.put("money_tormb",getPara("feebi"));
				chouShou.save();
				redirect("/user/chuShouBi");
			}

		} catch (Exception e) {
			logger.error("出售币失败，" + e.getMessage(), e);
			addError("出售币失败失败!");
			keepPara();
			forwardAction("/user/chuShouBi");
		}
	}
	public void zhuanBi(){
		if(checkSafePass()){
			setHeader();
			Pagination pager = getPager();
			Page<ZhuanBiEntity> page = null;
			Map<String,String> p= ParamUtils.translateMap(getParaMap());
			AccountEntity accc= (AccountEntity) getSession().getAttribute(Constants.FRONT_LOGIN_USER);
			p.put("userid",accc.getLong("id").toString());
			page = ZhuanBiEntity.dao.getPager(pager,p);
			setAttr("page",page);
			setAttr("user",getFrontUser());
			setAttr("zhuanbiliang", Constants.config.getLong("system.zhuanbiliang"));
			setAttr("zhuanbibei", Constants.config.getLong("system.zhuanbibei"));
			render("/themes/"+getSite().get("theme")+"/user/cw_zhuanbi.html");
		}

	}

	public void meirishouyi(){
		setHeader();
		setAttr("user",getFrontUser());
		AccountEntity accc= (AccountEntity) getSession().getAttribute(Constants.FRONT_LOGIN_USER);
		Pagination pager = getPager();
		Page<MoneyEntity> page = null;
		Map<String,String> p= ParamUtils.translateMap(getParaMap());
		p.put("userid",accc.getLong("id").toString());
		page = MoneyEntity.dao.getPagerGroup(pager,p);
		setAttr("page", page);
		render("/themes/"+getSite().get("theme")+"/user/cw_meirishouyi.html");
	}

	public void zhanghumingxi(){
		setHeader();
		setAttr("user",getFrontUser());
		AccountEntity accc= (AccountEntity) getSession().getAttribute(Constants.FRONT_LOGIN_USER);
		Pagination pager = getPager();
		Page<MoneyEntity> page = null;
		Map<String,String> p= ParamUtils.translateMap(getParaMap());
		p.put("userid",accc.getLong("id").toString());
		page = MoneyEntity.dao.getPager(pager,p);
		setAttr("page", page);
		setAttr("moneyTypeMap", MoneyTypeEnum.toMap());
		render("/themes/"+getSite().get("theme")+"/user/cw_zhanghumingxi.html");
	}

	public void bind() {
		doRegist();
	}
	
	public void regist() {
		setHeader();
		setAttr("user",getFrontUser());
		render("/themes/"+getSite().get("theme")+"/user/hy_huiyuanzhuce.html");
	}

	public void doRegist(){
		UploadFile file = getFile("icon");
		String userName = getPara("username");
		String password = getPara("password");
		String phone=getPara("phone");
		String tui_name=getPara("tui_name");
		int phoneCount= Constants.config.getInt("system.phoneusercount");
		AccountEntity accountTui=AccountEntity.dao.findByLoginName(tui_name);
		try {
			if(AccountEntity.dao.getByName(userName) != null) {
				addError("用户 [" + userName + "] 已存在!");
				keepPara();
				setAttr("skip",1);
            	forwardAction("/user/regist");
			}else if(tui_name!=null&&tui_name.length()!=0&&accountTui==null){
				addError("推荐人不存在");
				keepPara();
				setAttr("skip",1);
				forwardAction("/user/regist");
			} else if(StringUtils.isBlank(password)) {
				addError("密码不能为空!");
				keepPara();
				setAttr("skip",1);
				forwardAction("/user/regist");
			}else if(phoneCount<=AccountEntity.dao.userCountByPhone(phone,getPara("card_id"))) {
				addError("同一资料最多可"+phoneCount+"注册个账号!");
				keepPara();
				setAttr("skip",1);
				forwardAction("/user/regist");
			}else
			{
				BiToRMBEntity bili=BiToRMBEntity.dao.findLast();
				String filename="";
				if(file!=null){
					filename=FileUtils.saveFile(file.getFile());
				}
				String icon=filename;
				String truename=getPara("truename");
				String card_id=getPara("card_id");
				AccountEntity account = new AccountEntity();
				account.put("pic",icon);
				account.set("login_name", userName);
				account.set("b1", 0);
				account.set("b2", 0);
				account.set("b3", 0);
				account.set("level_money", 0);
				account.set("logincount", 0);
				account.set("state", AccountStateEnums.UNACTIVE.getCode());
				account.set("level", AccountLevelEnums.LEVEL0.getCode());
				account.set("phone", phone);
				account.set("truename", truename);
				account.set("card_id", card_id);
				AccountEntity accc= (AccountEntity) getSession().getAttribute(Constants.FRONT_LOGIN_USER);
				account.set("tui_id", accountTui==null?accc.get("id"):accountTui.get("id"));
				account.set("role", RoleEnum.REGISTER_USER.getCode());
				account.set("passwd", com.hfxb.app.core.utils.DigestUtils.md5Hex(password));
				account.set("create_time", Calendar.getInstance().getTime());
				account.save();
				redirect("/user/regist");
			}
		} catch (Exception e) {
			logger.error("注册新用户 [" + userName + "] 失败，" + e.getMessage(), e);
			addError("注册新用户 [" + userName + "] 失败!");
			keepPara();
			setAttr("skip",1);
			forwardAction("/user/regist");
		}
	}
	public void setmima(){
		setHeader();
		setAttr("user",getFrontUser());
		render("/themes/"+getSite().get("theme")+"/user/zh_mima.html");
	}

	public void passManage(){
		setHeader();
		setAttr("user",getFrontUser());
		render("/themes/"+getSite().get("theme")+"/user/zh_mimaguanli.html");
	}

	public  void secondPass(){
		setHeader();
		setAttr("user",getFrontUser());
		render("/themes/"+getSite().get("theme")+"/user/secondpass.html");
	}

	public  void addSecondPass(){
		String secondPass = getPara("secondPass");
		AccountEntity accc= (AccountEntity) getSession().getAttribute(Constants.FRONT_LOGIN_USER);
		accc=AccountEntity.dao.findById(accc.get("id"));
		try {
			String oldsecondPass=accc.getStr("secondPass");
			if(oldsecondPass!=null){
				addError("用户二级密码已存在!");
				keepPara();
				forwardAction("/user/secondPass");
			}else{
				accc.set("secondPass", com.hfxb.app.core.utils.DigestUtils.md5Hex(secondPass));
				accc.update();
				getSession().setAttribute(Constants.FRONT_LOGIN_USER, accc);
				String urls=getPara("urls");
				if(urls!=null){
					forwardAction(urls);
				}else
				{
					forwardAction("/user");
				}
			}
		} catch (Exception e) {
			logger.error("添加二级密码 [" + accc.getStr("login_name") + "] 失败，" + e.getMessage(), e);
			addError("添加二级密码 [" +  accc.getStr("login_name")  + "] 失败!");
			keepPara();
			forwardAction("/user/secondPass");
		}
	}

	public  void safePass(){
		setHeader();
		setAttr("user",getFrontUser());
		render("/themes/"+getSite().get("theme")+"/user/safepass.html");
	}

	public  void addSafePass(){
		String secondPass = getPara("safePass");
		AccountEntity accc= (AccountEntity) getSession().getAttribute(Constants.FRONT_LOGIN_USER);
		accc=AccountEntity.dao.findById(accc.get("id"));
		try {
			String oldsecondPass=accc.getStr("safePass");
			if(oldsecondPass!=null){
				addError("用户安全密码已存在!");
				keepPara();
				forwardAction("/user/safePass");
			}else{
				accc.set("safePass", com.hfxb.app.core.utils.DigestUtils.md5Hex(secondPass));
				accc.update();
				getSession().setAttribute(Constants.FRONT_LOGIN_USER, accc);
				String urls=getPara("urls");
				if(urls!=null){
					forwardAction(urls);
				}else
				{
					forwardAction("/user");
				}
			}
		} catch (Exception e) {
			logger.error("添加安全密码 [" + accc.getStr("login_name") + "] 失败，" + e.getMessage(), e);
			addError("添加安全密码 [" +  accc.getStr("login_name")  + "] 失败!");
			keepPara();
			forwardAction("/user/safePass");
		}
	}

	public void profile() {
		if(checkSecondPass()){
			setHeader();
			setAttr("user",getFrontUser());
			setAttr("accountEntity",getFrontUser());
			setAttr("skip",getPara("skip"));
			render("/themes/"+getSite().get("theme")+"/user/zh_gerengziliao.html");
		}
	}

	public void jihuo() {
		if(checkSecondPass()&&checkSafePass()){
			setHeader();
			setAttr("user",getFrontUser());
			setAttr("skip",getPara("skip"));
			setAttr("userlevel",Constants.config.getStringArray("system.userlevel"));
			String s[]=Constants.config.getStringArray("system.userlevelmoney");
			List<Long> ss=new ArrayList<Long>();
			for(String s1:s){
				ss.add(Long.valueOf(s1));
			}
			setAttr("userlevelmoney",ss);

			AccountEntity accc= (AccountEntity) getSession().getAttribute(Constants.FRONT_LOGIN_USER);
			Pagination pager = getPager();
			Page<AccountEntity> page = null;
			Map<String,String> p= ParamUtils.translateMap(getParaMap());
			p.put("tui_id",accc.getLong("id").toString());
			page = AccountEntity.dao.getPagerOfDaiJiHuo(pager,p);
			setAttr("page", page);


			render("/themes/"+getSite().get("theme")+"/user/hy_zhanghaojihuo.html");
		}
	}

	public void doJihuoOther(){
		AccountEntity accc= (AccountEntity) getSession().getAttribute(Constants.FRONT_LOGIN_USER);
		accc=AccountEntity.dao.findById(accc.get("id"));
		AccountEntity other= AccountEntity.dao.findById(getParaToLong("otherUID"));
		try {
				long money=0;
				long selectXiang=getParaToLong("selectXiang");
				money=selectXiang;
				int levelMoney=0;
				String[] moneys=Constants.config.getStringArray("system.userlevelmoney");
				for(int i=0;i<moneys.length;i++){
					if(Long.valueOf(moneys[i])==selectXiang){
						levelMoney=i;
					}
				}
				int level=accc.getInt("level");
				long oldMoney=0;
				if(level!=0){
					oldMoney=other.getLong("level_money");
				}
				money=money-oldMoney;
				boolean canAdd=true;
				if(other.getDate("ji_time")!=null){
					Calendar firstJiC = Calendar.getInstance();
					firstJiC.setTime(other.getDate("ji_time"));
					firstJiC.add(Calendar.DAY_OF_YEAR,Constants.config.getInt("system.updateLeveldate"));
					if(new Date().before(firstJiC.getTime())){
						canAdd=true;
					}else{
						canAdd=false;
					}
				}else{
					canAdd=true;
				}
				if(accc.getLong("b2")<money){
					addError("资金不足!");
					keepPara();
					setAttr("skip",1);
					forwardAction("/user/jihuo");
				}else if(accc.getInt("level")==2&&!canAdd){
					addError("已过激活日期!");
					keepPara();
					setAttr("skip",1);
					forwardAction("/user/jihuo");
				}else{

					MoneyEntity moneyEntity=new MoneyEntity();
					moneyEntity.put("userid",accc.getLong("id"));
					moneyEntity.put("create_time",Calendar.getInstance().getTime());
					moneyEntity.put("type",MoneyTypeEnum.ACTIVE.getCode());
					moneyEntity.put("remark","激活会员");
					moneyEntity.put("price",-money);
					moneyEntity.put("biType",2);
					moneyEntity.put("yuPrice",accc.getLong("b2")-money);
					moneyEntity.save();

					accc.set("b2",accc.getLong("b2")-money);
					other.set("level",levelMoney+2);
					other.set("level_money",selectXiang);
					other.set("ji_time",Calendar.getInstance().getTime());
					Calendar cal = Calendar.getInstance();
					cal.add(Calendar.DAY_OF_MONTH,Constants.config.getInt("system.fandate") );
					other.set("ji_end_time",cal.getTime());
					other.update();
					accc.update();
					AccountEntity.dao.jihuoJiangli(other.getLong("id"),money);
					accc=AccountEntity.dao.findById(accc.get("id"));
					getSession().setAttribute(Constants.FRONT_LOGIN_USER, accc);
					redirect("/user");
				}
		} catch (Exception e) {
			logger.error("激活他人失败，" + e.getMessage(), e);
			addError("激活他人失败!");
			keepPara();
			setAttr("skip",1);
			forwardAction("/user/jihuo");
		}
	}


	public void doJihuo(){
		String secondPass = getPara("safePass");
		AccountEntity accc= (AccountEntity) getSession().getAttribute(Constants.FRONT_LOGIN_USER);
		accc=AccountEntity.dao.findById(accc.get("id"));
		try {
			if(!com.hfxb.app.core.utils.DigestUtils.md5Hex(secondPass).equals(accc.getStr("safePass"))){
				addError("资金密码错误!");
				keepPara();
				setAttr("skip",1);
				forwardAction("/user/jihuo");
			}else{
				long money=0;
				long selectXiang=getParaToLong("selectXiang");
				money=selectXiang;
				int levelMoney=0;
				String[] moneys=Constants.config.getStringArray("system.userlevelmoney");
				for(int i=0;i<moneys.length;i++){
					if(Long.valueOf(moneys[i])==selectXiang){
						levelMoney=i;
					}
				}
				int level=accc.getInt("level");
				long oldMoney=0;
				if(level!=0){
					oldMoney=accc.getLong("level_money");
				}
				money=money-oldMoney;
				boolean canAdd=true;
				if(accc.getDate("ji_time")!=null){
					Calendar firstJiC = Calendar.getInstance();
					firstJiC.setTime(accc.getDate("ji_time"));
					firstJiC.add(Calendar.DAY_OF_YEAR,Constants.config.getInt("system.updateLeveldate"));
					if(new Date().before(firstJiC.getTime())){
						canAdd=true;
					}else{
						canAdd=false;
					}
				}else{
					canAdd=true;
				}
				if(accc.getLong("b2")<money){
					addError("资金不足!");
					keepPara();
					setAttr("skip",1);
					forwardAction("/user/jihuo");
				}else if(accc.getInt("level")==2&&!canAdd){
					addError("已过激活日期!");
					keepPara();
					setAttr("skip",1);
					forwardAction("/user/jihuo");
				}else{
					MoneyEntity moneyEntity=new MoneyEntity();
					moneyEntity.put("userid",accc.getLong("id"));
					moneyEntity.put("create_time",Calendar.getInstance().getTime());
					moneyEntity.put("type",MoneyTypeEnum.ACTIVE.getCode());
					moneyEntity.put("remark","激活会员");
					moneyEntity.put("price",-money);
					moneyEntity.put("biType",2);
					moneyEntity.put("yuPrice",accc.getLong("b2")-money);
					moneyEntity.save();

					accc.set("b2",accc.getLong("b2")-money);
					accc.set("level",levelMoney+2);
					accc.set("level_money",selectXiang);
					accc.set("ji_time",Calendar.getInstance().getTime());
					Calendar cal = Calendar.getInstance();
					cal.add(Calendar.DAY_OF_MONTH,Constants.config.getInt("system.fandate") );
					accc.set("ji_end_time",cal.getTime());
					accc.update();
					accc=AccountEntity.dao.findById(accc.get("id"));
					AccountEntity.dao.jihuoJiangli(accc.getLong("id"),money);
					getSession().setAttribute(Constants.FRONT_LOGIN_USER, accc);
					redirect("/user");
				}
			}
		} catch (Exception e) {
			logger.error("激活失败，" + e.getMessage(), e);
			addError("激活失败!");
			keepPara();
			setAttr("skip",1);
			forwardAction("/user/jihuo");
		}
	}


	public void doProfile(){
		AccountEntity accountEntity=getModel(AccountEntity.class);
		AccountEntity accc= (AccountEntity) getSession().getAttribute(Constants.FRONT_LOGIN_USER);
		accc=AccountEntity.dao.findById(accc.get("id"));
		try {
			accc.set("bite_link",accountEntity.get("bite_link"));
			accc.set("alipay",accountEntity.get("alipay"));
			accc.set("wechat",accountEntity.get("wechat"));
			accc.set("email",accountEntity.get("email"));
			accc.update();
			getSession().setAttribute(Constants.FRONT_LOGIN_USER, accc);


			setHeader();
			addError("修改个人资料成功!");
			setAttr("user",getFrontUser());
			setAttr("accountEntity",accountEntity);
			setAttr("skip",1);
			render("/themes/"+getSite().get("theme")+"/user/zh_gerengziliao.html");

		} catch (Exception e) {
			logger.error("修改个人资料 [" + accc.getStr("login_name") + "] 失败，" + e.getMessage(), e);
			addError("修改个人资料 [" +  accc.getStr("login_name")  + "] 失败!");

			keepPara();
			setHeader();
			setAttr("user",getFrontUser());
			setAttr("accountEntity",accountEntity);
			setAttr("skip",1);
			render("/themes/"+getSite().get("theme")+"/user/zh_gerengziliao.html");
		}

	}
	public  void tuiList(){
		setHeader();
		AccountEntity accc= (AccountEntity) getSession().getAttribute(Constants.FRONT_LOGIN_USER);
		Pagination pager = getPager();
		Page<AccountEntity> page = null;
		Map<String,String> p= ParamUtils.translateMap(getParaMap());
		p.put("tui_id",accc.getLong("id").toString());
		page = AccountEntity.dao.getPager(pager,p);
		setAttr("page", page);
		setAttr("accountStateMap", AccountStateEnums.toMap());

		List<AccountEntity>  tuiedList=AccountEntity.dao.getAllTuied(accc.getLong("id"));
		setAttr("tuiedList", tuiedList);
		render("/themes/"+getSite().get("theme")+"/user/hy_huiyuanliebiao.html");
	}

	public void goFrontUpdatePwd(){
		String param = getPara("p");
		try {
			param = DesUtils.decode(param, Constants.config.getString("des.key"));
			if(StringUtils.isNotBlank(param)) {
				String email = param.split("##")[0];
				String time = param.split("##")[1];
				long now = Calendar.getInstance().getTimeInMillis();
				if(now - Long.parseLong(time) > Constants.config.getInt("lostpwd.url.period", 24) * 60 * 60 *1000) {
					setAttr("flag", 2);
					addMessage("链接已失效");
				} else {
					setAttr("email", email);
					setAttr("flag", 1);
					setHeader();
				}
				render("/themes/"+getSite().get("theme")+"/user/newpwd2.html");
			} else {
				renderError(404);
			}
		} catch(Exception e) {
			logger.error(e.getMessage(), e);
			renderError(500);
		}
	}
	
	public void goUpdatePwd(){
		setHeader();
		render("/themes/"+getSite().get("theme")+"/user/newpwd.html");
	}
	
	public void logout() {
		getSession().invalidate();
		redirect("/user/login");
	}
}
