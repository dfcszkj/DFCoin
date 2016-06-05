package com.hfxb.app.web.account;

import com.hfxb.app.core.annotation.Action;
import com.hfxb.app.core.common.Constants;
import com.hfxb.app.core.model.Pagination;
import com.hfxb.app.core.utils.ParamUtils;
import com.hfxb.app.web.account.entity.AccountEntity;
import com.hfxb.app.web.account.entity.MsgEntity;
import com.hfxb.app.web.account.entity.MsgTypeEntity;
import com.hfxb.app.web.base.BaseController;
import com.hfxb.app.web.news.entity.NewsEntity;
import com.jfinal.plugin.activerecord.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

@Action(action = "/msg")
public class MsgController extends BaseController {

	private static final Logger logger = LoggerFactory.getLogger(MsgController.class);

	public void index() {
		setHeader();
		AccountEntity accc = (AccountEntity) getSession().getAttribute(Constants.FRONT_LOGIN_USER);
		List<MsgTypeEntity> msgTypeList=MsgTypeEntity.dao.getAll();
		Pagination pager = getPager();
		Page<MsgEntity> page = null;
		Map<String,String> p=ParamUtils.translateMap(getParaMap());
		p.put("userid",accc.getLong("id").toString());
		page = MsgEntity.dao.getPager(pager,p );
		setAttr("page", page);
		setAttr("typeList", msgTypeList);
		setAttr("site", getSite());
		render("/themes/"+getSite().get("theme")+"/user/xx_zaixianliuyan.html");
	}

	public void  save(){
		MsgEntity entity = getModel(MsgEntity.class);
		try {
			AccountEntity accc = (AccountEntity) getSession().getAttribute(Constants.FRONT_LOGIN_USER);
			entity.put("userid", accc.get("id"));
			entity.put("create_time", Calendar.getInstance().getTime());
			entity.save();
			redirect("/msg");
		} catch (Exception e) {
			logger.error("提交问题失败，" + e.getMessage(), e);
			addError("提交失败!");
			keepPara();
			forwardAction("/msg");
		}
	}
}
