package com.hfxb.app.web.account.entity;

import com.hfxb.app.core.annotation.TableBind;
import com.hfxb.app.core.model.Pagination;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@TableBind(name = "f_banner")
public class BannerEntity extends Model<BannerEntity>{
	
	private static final long serialVersionUID = -4368208647272740379L;
	
	private static final String TABLE_NAME = "f_banner";
	
	public static final BannerEntity dao = new BannerEntity();
	
	/**
	 * get account by loginname and password
	 * @return
	 */
	public List<BannerEntity> getAll(){
		String sql = "select * from " + TABLE_NAME + " order by id desc";
		List<BannerEntity> list = dao.find(sql);
		return list;
	}

	public Page<BannerEntity> getPager(Pagination pager, Map<String, String> p) {
		String select = "select * ";
		StringBuffer where = new StringBuffer(" from " + TABLE_NAME + "  where 1=1");
		List<Object> params = new ArrayList<Object>();
		if(StringUtils.isNotBlank(p.get("userid"))){
			where.append(" and m.userid = ? ");
			params.add(p.get("userid"));
		}
		where.append(" order by m.create_time desc");
		return dao.paginate(pager.getPn(), pager.getSize(), select, where.toString(), params.toArray());
	}


	public void delete(String i) {
		dao.deleteById(new Object[]{i});
	}
	public void delete(String[] i) {
		dao.deleteById((Object[])i);
	}

}
