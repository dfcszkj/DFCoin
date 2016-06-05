package com.hfxb.app.web.base;

import com.hfxb.app.core.common.Constants;
import com.hfxb.app.core.enums.PostStateEnum;
import com.hfxb.app.core.model.Pagination;
import com.hfxb.app.core.model.SystemInfo;
import com.hfxb.app.web.account.entity.AccountEntity;
import com.hfxb.app.web.sys.entity.SiteEntity;
import com.jfinal.core.Controller;
import com.jfinal.plugin.ehcache.CacheKit;
import com.jfinal.plugin.ehcache.IDataLoader;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Controller基类， 放一些Controller中的公共方法
 * @author qq
 *
 */
public class BaseController extends Controller {
	
	private static final Logger logger = LoggerFactory.getLogger(BaseController.class);
	
	/** http get请求 **/
	protected static final String METHOD_GTE = "get";
	/** http post请求 **/
	protected static final String METHOD_POST = "post";
	
	/** 请求处理成功 */
	protected static final String SUCCESS = "1";
	
	/** 请求处理失败 */
	protected static final String ERROR = "0";
	
	protected Map<String, Object> blocks = new HashMap<String, Object>();
	
	public SiteEntity getSite() {
		SiteEntity site = CacheKit.get(Constants.CACHE_KEY_FOREVER, "site");
		if(site == null) {
			site = SiteEntity.dao.getSite();
		}
		return site;
	}

	public  boolean checkSecondPass(){
		AccountEntity accc= (AccountEntity) getSession().getAttribute(Constants.FRONT_LOGIN_USER);
		String secondPass=accc.getStr("secondPass");
		String urls=getRequest().getRequestURL().toString();
		redirect("/user/secondPass?urls="+ URLEncoder.encode(urls));
		return secondPass!=null;
	}

	public  boolean checkSafePass(){
		AccountEntity accc= (AccountEntity) getSession().getAttribute(Constants.FRONT_LOGIN_USER);
		String secondPass=accc.getStr("safePass");
		String urls=getRequest().getRequestURL().toString();
		redirect("/user/safePass?urls="+ URLEncoder.encode(urls));
		return secondPass!=null;
	}

	/**
	 * 在request对象中添加返回信息
	 * @param msg		返回前台的信息
	 */
	protected void addMessage(String msg){
		setAttr("msg", msg);
	}
	/**
	 * 在request对象中添加返回信息
	 * @param msg		返回前台的信息
	 * @param params	参数
	 */
	protected void addMessage(String msg, Object... params) {
		msg = String.format(msg, params);
		addMessage(msg);
	}
	/**
	 * 在request对象中添加返回前台的错误信息
	 * @param msg
	 */
	protected void addError(String msg){
		setAttr("error", msg);
	}
	/**
	 * 在request对象中添加返回信息
	 * @param msg		返回前台的信息
	 * @param params	参数
	 */
	protected void addError(String msg, Object... params) {
		msg = String.format(msg, params);
		addError(msg);
	}
	
	/**
	 * 跳转到全局错误页面
	 */
	public void error() {
		render("error.html");
	}
	
	/**
	 * 获取客户端请求方式，如get和post等
	 * @return
	 */
	protected final String getHttpMethod(){
		return getRequest().getMethod();
	}
	
	/**
	 * 是否是get方法访问
	 * @return
	 */
	protected boolean isGetMethod(){
		return METHOD_GTE.equalsIgnoreCase(getHttpMethod());
	}
	
	/**
	 * 是否是post方法访问
	 * @return
	 */
	protected boolean isPostMethod(){
		return METHOD_POST.equalsIgnoreCase(getHttpMethod());
	}
	
	protected Pagination getPager() {
		int pageNumber = StringUtils.isBlank(getPara("pageNumber")) ? 1 : getParaToInt("pageNumber");
		int pageSize = StringUtils.isBlank(getPara("pageSize")) ? Constants.DEFAULT_PAGESIZE : getParaToInt("pageSize");
		return new Pagination(pageNumber, pageSize);
	}
	
	protected AccountEntity getCurrentUser() {
		return (AccountEntity) SecurityUtils.getSubject().getSession(true).getAttribute(Constants.KEY_LOGIN_ACCOUNT);
	}
	
	public AccountEntity getFrontUser() {
		AccountEntity accc=getRequest() == null ? null : (AccountEntity) getSession(true).getAttribute(Constants.FRONT_LOGIN_USER);
		if(accc!=null){
			accc=AccountEntity.dao.findById(accc.getLong("id"));
			accc.put("tui_count",AccountEntity.dao.getChildCount(accc.getLong("id")));
			accc=AccountEntity.dao.getMoneyInfo(accc);
		}
		return accc;
	}

	public SystemInfo getSystemInfo(){
		SystemInfo systemInfo=new SystemInfo();
		systemInfo.setCopyright(Constants.config.getString("system.copyright"));
		systemInfo.setWeburl(Constants.config.getString("system.weburl"));
		systemInfo.setName( Constants.config.getString("system.webname"));
		systemInfo.setTel( Constants.config.getString("system.tel"));
		systemInfo.setLogo( Constants.config.getString("system.logo"));
		return  systemInfo;
	}

	
	public void setHeader() {
		AccountEntity entity=getFrontUser();
		setAttr("site", getSite());
		setAttr("system", getSystemInfo());
		setAttr("user",entity);
		if(entity!=null){
			setAttr("myUserList",AccountEntity.dao.findMyUser(entity.getStr("phone"),entity.getStr("card_id")));
		}
		setAttr("stateMap", CacheKit.get(Constants.CACHE_KEY_FOREVER, "stateMap", new IDataLoader() {
			@Override
			public Object load() {
				return PostStateEnum.toMap();
			}
		}));
	}
}
