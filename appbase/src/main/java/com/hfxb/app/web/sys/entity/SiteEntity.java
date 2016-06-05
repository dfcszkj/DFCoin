package com.hfxb.app.web.sys.entity;

import com.hfxb.app.core.annotation.TableBind;
import com.jfinal.plugin.activerecord.Model;

@TableBind(name="f_site")
public class SiteEntity extends Model<SiteEntity>{
	
	private static final long serialVersionUID = -791636789189757819L;
	
	public static SiteEntity dao = new SiteEntity();

	public SiteEntity getSite() {
		String sql = "select * from f_site";
		return findFirst(sql);
	}

}
