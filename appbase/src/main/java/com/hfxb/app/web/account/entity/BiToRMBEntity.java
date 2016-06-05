package com.hfxb.app.web.account.entity;

import com.hfxb.app.core.annotation.TableBind;
import com.hfxb.app.core.model.Pagination;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@TableBind(name = "f_bitormb")
public class BiToRMBEntity extends Model<BiToRMBEntity>{
	
	private static final long serialVersionUID = -4368208647272740379L;
	
	private static final String TABLE_NAME = "f_bitormb";
	
	public static final BiToRMBEntity dao = new BiToRMBEntity();
	
	/**
	 * get account by loginname and password
	 * @return
	 */
	public List<BiToRMBEntity> getNear12(){
		String sql = "select * from `f_bitormb` order by create_time asc";
		List<BiToRMBEntity> list = dao.find(sql);
		return list;
	}

	public  BiToRMBEntity findLast(){
		String sql = "select * from "+TABLE_NAME+" order by create_time desc limit 1";
		return findFirst(sql);
	}

	public Page<BiToRMBEntity> getPager(Pagination pager, Map<String, String> p) {
		String select = "select * ";
		StringBuffer where = new StringBuffer("from " + TABLE_NAME + " where 1=1 ");
		List<Object> params = new ArrayList<Object>();
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
