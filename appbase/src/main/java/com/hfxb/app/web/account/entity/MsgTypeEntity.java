package com.hfxb.app.web.account.entity;

import com.hfxb.app.core.annotation.TableBind;
import com.hfxb.app.core.model.Pagination;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@TableBind(name = "f_msg_type")
public class MsgTypeEntity extends Model<MsgTypeEntity>{
	
	private static final long serialVersionUID = -4368208647272740379L;
	
	private static final String TABLE_NAME = "f_msg_type";
	
	public static final MsgTypeEntity dao = new MsgTypeEntity();
	
	/**
	 * get account by loginname and password
	 * @return
	 */
	public List<MsgTypeEntity> getAll(){
		String sql = "select * from " + TABLE_NAME + " where state=1 order by id desc";
		List<MsgTypeEntity> list = dao.find(sql);
		return list;
	}

	public Page<MsgTypeEntity> getPager(Pagination pager, Map<String, String> p) {
		String select = "select * ";
		StringBuffer where = new StringBuffer("from " + TABLE_NAME + " where state=1");
		List<Object> params = new ArrayList<Object>();
		where.append(" order by id desc");
		return dao.paginate(pager.getPn(), pager.getSize(), select, where.toString(), params.toArray());
	}


	public void delete(String i) {
		dao.deleteById(new Object[]{i});
	}
	public void delete(String[] i) {
		dao.deleteById((Object[])i);
	}

}
