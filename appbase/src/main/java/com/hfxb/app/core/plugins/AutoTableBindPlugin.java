package com.hfxb.app.core.plugins;

import com.hfxb.app.core.annotation.TableBind;
import com.hfxb.app.core.enums.TableNameStyleEnum;
import com.hfxb.app.core.helper.AnnotationHelper;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.IDataSourceProvider;
import com.jfinal.plugin.activerecord.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.util.List;

public class AutoTableBindPlugin extends ActiveRecordPlugin {
    
    private static final Logger logger = LoggerFactory.getLogger(AutoTableBindPlugin.class);
 
    private TableNameStyleEnum tableNameStyle;
    
    public AutoTableBindPlugin(DataSource dataSource) {
        super(dataSource);
    }
 
    public AutoTableBindPlugin(IDataSourceProvider dataSourceProvider, TableNameStyleEnum tableNameStyle) {
        super(dataSourceProvider);
        this.tableNameStyle = tableNameStyle;
    }
 
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public boolean start() {
        try {
            List<Class> modelClasses = AnnotationHelper.findClasses(Model.class);
            logger.debug("modelClasses.size  {}",modelClasses.size());
            TableBind tb = null;
            for (Class modelClass : modelClasses) {
                tb = (TableBind) modelClass.getAnnotation(TableBind.class);
                if (tb == null) {
                    this.addMapping(tableName(modelClass), modelClass);
                    logger.debug("auto bindTable: addMapping({}, {})", tableName(modelClass), modelClass.getName());
                } else {
                    if (StrKit.notBlank(tb.pk())) {
                        this.addMapping(tb.name(), tb.pk(), modelClass);
                        logger.debug("auto bindTable: addMapping({}, {},{})", new Object[]{tb.name(),tb.pk(), modelClass.getName()});
                    } else {
                        this.addMapping(tb.name(), modelClass);
                        logger.debug("auto bindTable: addMapping({}, {})", tb.name(), modelClass.getName());
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return super.start();
    }
 
    @Override
    public boolean stop() {
        return super.stop();
    }
 
    private String tableName(Class<?> clazz) {
        String tableName = clazz.getSimpleName();
        if (tableNameStyle == TableNameStyleEnum.UP) {
            tableName = tableName.toUpperCase();
        } else if (tableNameStyle == TableNameStyleEnum.LOWER) {
            tableName = tableName.toLowerCase();
        } else {
            tableName = StrKit.firstCharToLowerCase(tableName);
        }
        return tableName;
    }
}