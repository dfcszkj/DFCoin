package com.hfxb.app.web.account.entity;

import com.hfxb.app.core.annotation.TableBind;
import com.hfxb.app.core.model.Pagination;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@TableBind(name = "f_chushou")
public class ChuShouEntity extends Model<ChuShouEntity>{
	
	private static final long serialVersionUID = -4368208647272740379L;
	
	private static final String TABLE_NAME = "f_chushou";
	
	public static final ChuShouEntity dao = new ChuShouEntity();
	
	/**
	 * get account by loginname and password
	 * @return
	 */
	public List<ChuShouEntity> getAll(){
		String sql = "select * from " + TABLE_NAME + " where  status!=2 order by id desc";
		List<ChuShouEntity> list = dao.find(sql);
		return list;
	}

	public Page<ChuShouEntity> getPager(Pagination pager, Map<String, String> p) {
		String select = "select *,(select login_name  from f_account a where a.id=f.userid ) as login_name ";
		StringBuffer where = new StringBuffer("from " + TABLE_NAME + " f where 1=1 ");
		List<Object> params = new ArrayList<Object>();
		if(StringUtils.isNotBlank(p.get("state"))){
			where.append(" and state = ? ");
			params.add(p.get("state"));
		}if(StringUtils.isNotBlank(p.get("notstate"))){
			where.append(" and state != ? ");
			params.add(p.get("notstate"));
		}
		if(StringUtils.isNotBlank(p.get("userid"))){
			where.append(" and userid = ? ");
			params.add(p.get("userid"));
		}
		if(StringUtils.isNotBlank(p.get("notuserid"))){
			where.append(" and userid != ? ");
			params.add(p.get("notuserid"));
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
