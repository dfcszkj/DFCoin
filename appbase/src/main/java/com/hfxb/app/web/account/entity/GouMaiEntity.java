package com.hfxb.app.web.account.entity;

import com.hfxb.app.core.annotation.TableBind;
import com.hfxb.app.core.model.Pagination;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@TableBind(name = "f_goumai")
public class GouMaiEntity extends Model<GouMaiEntity>{
	
	private static final long serialVersionUID = -4368208647272740379L;
	
	private static final String TABLE_NAME = "f_goumai";
	
	public static final GouMaiEntity dao = new GouMaiEntity();
	
	/**
	 * get account by loginname and password
	 * @return
	 */
	public List<GouMaiEntity> getAll(){
		String sql = "select * from " + TABLE_NAME + " where  order by id desc";
		List<GouMaiEntity> list = dao.find(sql);
		return list;
	}

	public Page<GouMaiEntity> getPager(Pagination pager, Map<String, String> p) {
		String select = "select *,(select truename  from f_account a where a.id=f.userid ) as truename ";
		StringBuffer where = new StringBuffer("from " + TABLE_NAME + " f where 1=1 ");
		List<Object> params = new ArrayList<Object>();
		if(StringUtils.isNotBlank(p.get("state"))){
			where.append("and state = ?");
			params.add(p.get("state"));
		}
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
