package com.hfxb.app.web.account.entity;

import com.hfxb.app.core.annotation.TableBind;
import com.hfxb.app.core.model.Pagination;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@TableBind(name = "f_money")
public class MoneyEntity extends Model<MoneyEntity>{
	
	private static final long serialVersionUID = -4368208647272740379L;
	
	private static final String TABLE_NAME = "f_money";
	
	public static final MoneyEntity dao = new MoneyEntity();
	
	/**
	 * get account by loginname and password
	 * @return
	 */
	public List<MoneyEntity> getAll(){
		String sql = "select * from " + TABLE_NAME + " where state=1 order by id desc";
		List<MoneyEntity> list = dao.find(sql);
		return list;
	}

	public Page<MoneyEntity> getPager(Pagination pager, Map<String, String> p) {
		String select = "select * ";
		StringBuffer where = new StringBuffer("from " + TABLE_NAME + " where 1=1 ");
		List<Object> params = new ArrayList<Object>();
		if(StringUtils.isNotBlank(p.get("userid"))){
			where.append("and userid = ?");
			params.add(p.get("userid"));
		}
		where.append(" order by create_time desc,id desc");
		return dao.paginate(pager.getPn(), pager.getSize(), select, where.toString(), params.toArray());
	}


	public Page<MoneyEntity> getPagerGroup(Pagination pager, Map<String, String> p) {
		String select = "select sum(price) as price,create_time," +
				"(select sum(price) from `f_money` f1 where DATE_FORMAT(f.create_time,'%Y-%m-%d')=DATE_FORMAT(f1.create_time,'%Y-%m-%d')" +
				" and type=2 and f1.userid="+p.get("userid")+") as price1," +
				"(select sum(price) from `f_money` f2 where DATE_FORMAT(f.create_time,'%Y-%m-%d')=DATE_FORMAT(f2.create_time,'%Y-%m-%d')" +
				" and type=4 and f2.userid="+p.get("userid")+") as price2," +
				"(select sum(price) from `f_money` f3 where DATE_FORMAT(f.create_time,'%Y-%m-%d')=DATE_FORMAT(f3.create_time,'%Y-%m-%d')" +
				" and type=3 and f3.userid="+p.get("userid")+") as price3";
		StringBuffer where = new StringBuffer("from " + TABLE_NAME + " f where 1=1 ");
		List<Object> params = new ArrayList<Object>();
		if(StringUtils.isNotBlank(p.get("userid"))){
			where.append("and f.userid = ?");
			params.add(p.get("userid"));
		}
		where.append("   group by DATE_FORMAT(create_time,'%Y-%m-%d') order by  DATE_FORMAT(create_time,'%Y-%m-%d') desc ");
		return dao.paginate(pager.getPn(), pager.getSize(), select, where.toString(), params.toArray());
	}

	public void delete(String i) {
		dao.deleteById(new Object[]{i});
	}
	public void delete(String[] i) {
		dao.deleteById((Object[])i);
	}

}
