package com.hfxb.app.web.account.entity;

import com.hfxb.app.core.annotation.TableBind;
import com.hfxb.app.core.common.Constants;
import com.hfxb.app.core.enums.AccountLevelEnums;
import com.hfxb.app.core.enums.AccountStateEnums;
import com.hfxb.app.core.enums.MoneyTypeEnum;
import com.hfxb.app.core.enums.RoleEnum;
import com.hfxb.app.core.model.Pagination;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authc.Account;

import java.util.*;

@TableBind(name = "f_account")
public class AccountEntity extends Model<AccountEntity>{
	
	private static final long serialVersionUID = -4368208647272740379L;
	
	private static final String TABLE_NAME = "f_account";

	private  static  String checkNot2=" and ( lockstate is  null or (lockstate is not null and   lockstate!=1) )";
	public static final AccountEntity dao = new AccountEntity();
	
	/**
	 * get account by loginname and password
	 * @return
	 */
	public List<AccountEntity> getAll(){
		String sql = "select * from " + TABLE_NAME + " order by id desc";
		List<AccountEntity> list = dao.find(sql);
		return list;
	}

	public List<AccountEntity> getByTuiJiang(String tui_id){
		String sql = "select * from " + TABLE_NAME + " where FIND_IN_SET(tui_id,?) "+checkNot2+"   order by id desc";
		List<AccountEntity> list = dao.find(sql,tui_id);
		return list;
	}



	public void jihuoJiangli(long userid,long money){
		AccountEntity entity=dao.findById(userid);
		long toMoney=money*Constants.config.getLong("system.jinglibili")/100;
		dao.jinglijiang(userid,userid,toMoney);
		Integer hasGuoQi=entity.getInt("has_guoqi");

		boolean canadd=false;
		if(entity.getLong("tui_id")==null){
			canadd=false;
		}else if(entity.getDate("ji_time")==null){
			canadd=true;
		}else if(entity.get("has_guoqi")!=null){
			canadd=false;
		}else{
			Calendar firstJiC = Calendar.getInstance();
			firstJiC.setTime(entity.getDate("ji_time"));
			firstJiC.add(Calendar.DAY_OF_YEAR,Constants.config.getInt("system.updateLeveldate"));
			if(new Date().before(firstJiC.getTime())){
				canadd=true;
			}else{
				canadd=false;
			}
		}
		if(canadd){
			AccountEntity parentEntity=dao.findById(entity.getLong("tui_id"));
			if(parentEntity!=null){
				long zhituibi=Constants.config.getLong("system.zhituibi");
				long zhituibaodanbi=Constants.config.getLong("system.zhituibaodanbi");
				long monney1=money*zhituibaodanbi/100;
				money=money*(parentEntity.getInt("state")==1?zhituibi:(zhituibi+zhituibaodanbi))/100;
				long dongjieMoney=money*Constants.config.getLong("system.jiangdongjie")/100;

				if(parentEntity.getInt("level")!=1){
					MoneyEntity moneyEntity=new MoneyEntity();
					moneyEntity.put("userid",parentEntity.getLong("id"));
					moneyEntity.put("create_time", Calendar.getInstance().getTime());
					moneyEntity.put("type", MoneyTypeEnum.DIRECT.getCode());
					moneyEntity.put("remark","推广会员"+entity.getStr("login_name")+"");
					moneyEntity.put("price",money);
					moneyEntity.put("biType",2);
					moneyEntity.put("yuPrice",parentEntity.getLong("b2")+dongjieMoney);

					moneyEntity.save();
					parentEntity.set("b2",parentEntity.getLong("b2")+dongjieMoney);
					if(dao.checkShangXian(parentEntity.getLong("id"),money-dongjieMoney))
						parentEntity.set("b1",parentEntity.getLong("b1")+money-dongjieMoney);
					parentEntity.update();
				}
				if(parentEntity.getInt("state")==1&&parentEntity.get("tui_id")!=null){
					AccountEntity baodanEntity=findBaoDan(parentEntity.getLong("tui_id"));
					if(baodanEntity!=null){
						long dongjieMoney1=monney1*Constants.config.getLong("system.jiangdongjie")/100;
						MoneyEntity moneyEntity=new MoneyEntity();
						moneyEntity.put("userid",baodanEntity.getLong("id"));
						moneyEntity.put("create_time", Calendar.getInstance().getTime());
						moneyEntity.put("type", MoneyTypeEnum.BAODAN.getCode());
						moneyEntity.put("remark","报单中心奖"+entity.getStr("login_name"));
						moneyEntity.put("price",monney1);
						moneyEntity.put("biType",2);
						moneyEntity.put("yuPrice",baodanEntity.getLong("b2")+dongjieMoney1);
						moneyEntity.save();
						baodanEntity.set("b2",baodanEntity.getLong("b2")+dongjieMoney1);
						if(dao.checkShangXian(baodanEntity.getLong("id"),monney1-dongjieMoney))
							baodanEntity.set("b1",baodanEntity.getLong("b1")+monney1-dongjieMoney1);
						baodanEntity.update();
					}
				}
			}
		}
	}

	public void jinglijiang(long oldid,long userid,long money){
		AccountEntity entity=dao.findById(userid);
		if(entity!=null){
			if(entity.getInt("state")==3&&entity.getInt("level")!=1&&oldid!=userid){
				long dongjieMoney1=money*Constants.config.getLong("system.jiangdongjie")/100;
				MoneyEntity moneyEntity=new MoneyEntity();
				moneyEntity.put("userid",entity.getLong("id"));
				moneyEntity.put("create_time", Calendar.getInstance().getTime());
				moneyEntity.put("type", MoneyTypeEnum.MANAGE.getCode());
				moneyEntity.put("remark",dao.findById(oldid).getStr("login_name")+"激活,获得经理奖");
				moneyEntity.put("price",money);
				moneyEntity.put("biType",2);
				moneyEntity.put("yuPrice",entity.getLong("b2")+money);
				moneyEntity.save();
				entity.set("b2",entity.getLong("b2")+dongjieMoney1);
				if(dao.checkShangXian(entity.getLong("id"),money-dongjieMoney1))
					entity.set("b1",entity.getLong("b1")+money-dongjieMoney1);
				entity.update();
			}
			if(entity.getLong("tui_id")!=null)dao.jinglijiang(oldid,entity.getLong("tui_id"),money);
		}
	}

	public boolean checkShangXian(long userid,double tianMoney){
		AccountEntity  entity=dao.findById(userid);
		long money=entity.getLong("b1");
		String[] manmoneys=Constants.config.getStringArray("system.manmoney");
		int level=entity.getInt("level")-2;
		if(level==-1){
			return true;
		}
		if(manmoneys.length>level){	//0,
			if(money+tianMoney>Double.valueOf(manmoneys[level])){
				return false;
			}else{
				return  true;
			}
		}else{
			return false;
		}
	}

	public AccountEntity findBaoDan(long userid){
		AccountEntity entity=dao.findById(userid);
		if(entity==null){
			return null;
		}
		return  entity.getInt("state")>1?entity:findBaoDan(entity.getLong("tui_id"));
	}

	public List<AccountEntity> getAllTuied(long userid){
		String sql = "select * from " + TABLE_NAME + " where FIND_IN_SET(id, getChildAccount(?)) "+checkNot2+"   order by id desc";
		List<AccountEntity> list = dao.find(sql, userid);
		return list;
	}
	/**
	 * 获取用户推广数量
	 * @param userId
	 * @return
     */
	public long getChildCount(long userId){
		String sql="select count(id) from f_account where FIND_IN_SET(id, getChildAccount(?))  "+checkNot2;
		return Db.queryLong(sql, userId);
	}


	public AccountEntity findByDF(String DF) {
		String sql = "select * from " + TABLE_NAME + " where id=?  "+checkNot2;
		DF=DF.replaceAll("F","").replaceAll("D","");
		return findFirst(sql,DF);
	}
	/**
	 * 根据邮箱和密码获取用户
	 * @param loginname
	 * @param pw
	 * @return
	 */
	public AccountEntity getByNamePwd(String loginname, String pw) {
		String sql = "select * from " + TABLE_NAME + " where login_name = ? and passwd = ?  "+checkNot2;
		return findFirst(sql,loginname);
	}
	
	public AccountEntity findBySSO(String ssoId, String ssoType) {
		String sql = "select * from " + TABLE_NAME + " where sso_id = ? and sso_type = ?  "+checkNot2;
		return findFirst(sql, ssoId, ssoType);
	}
	/**
	 * 判断用户是否存在， 存在返回true， 否则返回false
	 * @param loginname
	 * @return
	 */
	public AccountEntity findByLoginName(String loginname) {
		String sql = "select * from " + TABLE_NAME + " where login_name = ?   "+checkNot2;
		return findFirst(sql, loginname);
	}


	/**
	 * 判断用户是否存在， 存在返回true， 否则返回false
	 * @param loginname
	 * @return
	 */
	public boolean userExist(String loginname) {
		String sql = "select count(*) from " + TABLE_NAME + " where login_name = ?  "+checkNot2;
		return Db.queryLong(sql, loginname) == 0;
	}

	public long userCountByPhone(String phone,String card_id){
		String sql = "select  * from (select count(*) as count from f_account where phone=?  "+checkNot2+
				"union  " +
				" select count(*) as count from f_account where card_id=?  "+checkNot2+") c order by  c.count desc limit 1 ";
		Long count= Db.queryLong(sql, phone,card_id);
		return count;
	}


	/**
	 * 根据用户名获取用户
	 * @param loginname
	 * @return
	 */
	public AccountEntity getByName(String loginname){
		String sql = "select * from " + TABLE_NAME + " where login_name = ?  "+checkNot2;
		return findFirst(sql,loginname);
	}

	public Map<String, String> getIdTitleMap() {
		Map<String, String> map = new HashMap<String, String>();
		String sql = "select * from f_account where   lockstate!=2";
		List<AccountEntity> list = dao.find(sql);
		for(AccountEntity a: list) {
			map.put("a"+a.getLong("id"), a.getStr("login_name"));
		}
		return map;
	}

	public Page<AccountEntity> getPagerOfDaiJiHuo(Pagination pager, Map<String, String> p) {
		String select = "select * ";
		StringBuffer where = new StringBuffer("from f_account where 1=1  "+checkNot2+" and FIND_IN_SET(id, getChildAccount("+p.get("tui_id")+")) and level!="+(Constants.config.getStringArray("system.userlevelmoney").length+1)+"  ");

		List<Object> params = new ArrayList<Object>();

		if(StringUtils.isNotBlank(p.get("createTimeMin"))){
			where.append(" and create_time >= ? ");
			params.add(p.get("createTimeMin"));
		}
		if(StringUtils.isNotBlank(p.get("createTimeMax"))){
			where.append(" and create_time < ? ");
			params.add(p.get("createTimeMax"));
		}
		where.append(" order by create_time desc");
		return dao.paginate(pager.getPn(), pager.getSize(), select, where.toString(), params.toArray());
	}


	public List<AccountEntity> findMyUser(String phone,String idCard) {
		String sql = "select * from " + TABLE_NAME + " where  phone=? or card_id=? and role=?  "+checkNot2+" order by id desc";
		List<AccountEntity> list = dao.find(sql,phone,idCard,RoleEnum.REGISTER_USER.getCode());
		return list;
	}

	public Page<AccountEntity> getPager(Pagination pager, Map<String, String> p) {
		String select = "select * ";
		StringBuffer where = new StringBuffer("from f_account where 1=1  "+checkNot2);
		List<Object> params = new ArrayList<Object>();
		if(StringUtils.isNotBlank(p.get("login_name"))) {
			where.append(" and login_name = ? ");
			params.add(p.get("login_name"));
		}
		if(StringUtils.isNotBlank(p.get("role"))){
			where.append(" and role = ? ");
			params.add(p.get("role"));
		}
		if(StringUtils.isNotBlank(p.get("state"))){
			where.append(" and state = ? ");
			params.add(p.get("state"));
		}
		if(StringUtils.isNotBlank(p.get("tui_id"))){
			where.append(" and tui_id = ? ");
			params.add(p.get("tui_id"));
		}
		if(StringUtils.isNotBlank(p.get("createTimeMin"))){
			where.append(" and create_time >= ? ");
			params.add(p.get("createTimeMin"));
		}
		if(StringUtils.isNotBlank(p.get("createTimeMax"))){
			where.append(" and create_time < ? ");
			params.add(p.get("createTimeMax"));
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


	public AccountEntity getMoneyInfo(AccountEntity entity){
		try {
			int level=entity.getInt("level");
			level=level-2;
			String[] userlevel=Constants.config.getStringArray("system.userlevel");
			entity.put("levelCount",userlevel.length);
			entity.put("userlevel",userlevel[level]);
			String[] userlevelmoney= Constants.config.getStringArray("system.userlevelmoney");
			entity.put("userlevelmoney",userlevelmoney[level]);
		}catch (Exception e){

		}finally {
			 return entity;
		}
	}
}
