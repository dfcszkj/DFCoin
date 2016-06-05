package com.hfxb.app.web.init;

import com.hfxb.app.core.common.Constants;
import com.hfxb.app.core.enums.MoneyTypeEnum;
import com.hfxb.app.core.enums.RoleEnum;
import com.hfxb.app.core.model.Pagination;
import com.hfxb.app.web.account.entity.AccountEntity;
import com.hfxb.app.web.account.entity.MoneyEntity;
import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.*;

/**
 * Created by gravel on 16/6/2.
 */
public class NFDFlightDataTimerTask  extends TimerTask {

    private static Logger log = Logger.getLogger(NFDFlightDataTimerTask.class);

    @Override
    public void run() {
        try
        {
            Db.update("update f_account set state=1,level=1,ji_time=null,level_money=0,has_guoqi=1 where CURDATE()>=ji_end_time");
            long chanbidongjie= Constants.config.getLong("system.chanbidongjie");
            Pagination pager=new Pagination(1,1000000000);
            Map<String, String> p =new HashMap<String, String>();
            p.put("role", RoleEnum.REGISTER_USER.getCode());
            Page<AccountEntity> page=AccountEntity.dao.getPager(pager,p);
            String[] tuanduis=Constants.config.getStringArray("system.tuandui");
            if(page!=null){
                for(AccountEntity account:page.getList()){
                    try {
                        if(account.getInt("level")>1){
                            long money=account.getLong("level_money")*Constants.config.getLong("system.moneyfanbi")/100;
                            if(AccountEntity.dao.checkShangXian(account.getLong("id"),money*(100-chanbidongjie)/100)){
                                MoneyEntity moneyEntity=new MoneyEntity();
                                moneyEntity.put("userid",account.getLong("id"));
                                moneyEntity.put("create_time", Calendar.getInstance().getTime());
                                moneyEntity.put("type", MoneyTypeEnum.MINING.getCode());
                                moneyEntity.put("remark","采矿奖");
                                moneyEntity.put("price",money);
                                moneyEntity.put("biType",0);
                                moneyEntity.put("yuPrice",account.getLong("b1")+account.getLong("b2")+account.getLong("b3")+money);
                                moneyEntity.save();
                                account.set("b2",account.getLong("b2")+money*chanbidongjie/100);
                                account.set("b1",account.getLong("b1")+money*(100-chanbidongjie)/100);
                                if(account.getInt("state")>=2){
                                    String tuandui=tuanduis[account.getInt("level")-2];
                                    String[] jiang=tuandui.split("\\|");
                                    double moneys=tuiJiang(0,account.getLong("id").toString(),jiang,0);
                                    double tuanduijiang=moneys*Constants.config.getLong("system.moneyfanbi")/100;

                                    Long tuanduiend=Long.valueOf((tuanduijiang*chanbidongjie/100+"").replaceAll("\\..*",""))+Long.valueOf((tuanduijiang*(100-chanbidongjie)/100+"").replaceAll("\\..*",""));
                                    MoneyEntity moneyEntity1=new MoneyEntity();
                                    moneyEntity1.put("userid",account.getLong("id"));
                                    moneyEntity1.put("create_time", Calendar.getInstance().getTime());
                                    moneyEntity1.put("type", MoneyTypeEnum.TEAM.getCode());
                                    moneyEntity1.put("remark","团队奖");
                                    moneyEntity1.put("price",tuanduiend);
                                    moneyEntity1.put("biType",0);
                                    moneyEntity1.put("yuPrice",account.getLong("b1")+account.getLong("b2")+tuanduiend);
                                    moneyEntity1.save();
                                    account.set("b2",account.getLong("b2")+Long.valueOf((tuanduijiang*chanbidongjie/100+"").replaceAll("\\..*","")));
                                    account.set("b1",account.getLong("b1")+Long.valueOf((tuanduijiang*(100-chanbidongjie)/100+"").replaceAll("\\..*","")));
                                }
                                account.update();
                            }
                        }
                    }catch (Exception e){
                    }
                }
            }
        } catch (Exception e) {
        }
    }

    public  double tuiJiang(int level,String tui_id,String[] jiang,double oldMoney){
        List<AccountEntity> list= AccountEntity.dao.getByTuiJiang(tui_id);
        double money=0;
        StringBuilder s=new StringBuilder();
        int index=0;
        for(AccountEntity account:list){
            s.append(account.getLong("id"));
            if(index!=list.size()-1)
                    s.append(",");
            if(account.getInt("level")>1)
                money+=account.getLong("level_money");
        }
        String s1=jiang[level];
        double ss=(money)*Integer.valueOf(s1)/100;
        return (level>=jiang.length-1||tui_id.length()==0)?(oldMoney+ss):tuiJiang(level+1,s.toString(),jiang,oldMoney+ss);
    }
}