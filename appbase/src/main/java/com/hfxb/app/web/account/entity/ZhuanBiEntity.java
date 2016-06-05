package com.hfxb.app.web.account.entity;

import com.hfxb.app.core.annotation.TableBind;
import com.hfxb.app.core.model.Pagination;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@TableBind(name = "f_zhuanbi")
public class ZhuanBiEntity extends Model<ZhuanBiEntity>{
	
	private static final long serialVersionUID = -4368208647272740379L;
	
	private static final String TABLE_NAME = "f_zhuanbi";
	
	public static final ZhuanBiEntity dao = new ZhuanBiEntity();
	
	/**
	 * get account by loginname and password
	 * @return
	 */
	public List<ZhuanBiEntity> getAll(){
		String sql = "select * from " + TABLE_NAME + " where state=1 order by id desc";
		List<ZhuanBiEntity> list = dao.find(sql);
		return list;
	}

	public Page<ZhuanBiEntity> getPager(Pagination pager, Map<String, String> p) {
		String select = "select *,(select truename from f_account a where a.id=touserid) totruename ";
		StringBuffer where = new StringBuffer("from " + TABLE_NAME + " where 1=1 ");
		List<Object> params = new ArrayList<Object>();
		if(StringUtils.isNotBlank(p.get("userid"))){
			where.append("and userid = ?");
			params.add(p.get("userid"));
		}
		where.append(" order by create_time desc");
		return dao.paginate(pager.getPn(), pager.getSize(), select, where.toString(), params.toArray());
	}

	public void delete(String i) {
		dao.deleteById(new Object[]{i});
	}
	public void delete(String[] i) {
		dao.deleteById((Object[])i);
	}

}
