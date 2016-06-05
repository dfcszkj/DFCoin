package com.hfxb.app.web.web.entity;

import com.hfxb.app.core.annotation.TableBind;
import com.hfxb.app.core.model.Pagination;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by gravel on 16/5/11.
 */
@TableBind(name="f_web")
public class WebEntity extends Model<WebEntity> {

    private static final String TABLE_NAME = "f_web";
    private static final long serialVersionUID = -791636789189757819L;

    public static WebEntity dao = new WebEntity();

    public  List<WebEntity> getAll(){
        String sql = "select * from " + TABLE_NAME + " where 1=1 order by create_time desc";
        List<WebEntity> list = dao.find(sql);
        return list;
    }
    public  List<WebEntity> getEnable(){
        String sql = "select * from " + TABLE_NAME + " where state=1 order by create_time desc";
        List<WebEntity> list = dao.find(sql);
        return list;
    }

    public Page<WebEntity> getPager(Pagination pager, Map<String, String> p) {
        String select = "select * ";
        StringBuffer where = new StringBuffer("from "+TABLE_NAME+" where 1=1 ");
        List<Object> params = new ArrayList<Object>();
        if(StringUtils.isNotBlank(p.get("type"))) {
            where.append(" and type = ?");
            params.add(Integer.parseInt(p.get("type")));
        }
        where.append(" order by create_time desc,id desc");
        return dao.paginate(pager.getPn(), pager.getSize(), select, where.toString(), params.toArray());
    }

    public void delete(String i) {
        dao.deleteById(new Object[]{i});
    }
    public void delete(String[] i) {
        dao.deleteById((Object[])i);
    }
}
