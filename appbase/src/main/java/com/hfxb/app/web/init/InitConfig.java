package com.hfxb.app.web.init;

import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.wall.WallFilter;
import com.hfxb.app.core.annotation.Action;
import com.hfxb.app.core.beetl.ShiroBeetl;
import com.hfxb.app.core.beetl.YiBeetlRenderFactory;
import com.hfxb.app.core.enums.TableNameStyleEnum;
import com.hfxb.app.core.handler.BaseHandler;
import com.hfxb.app.core.helper.AnnotationHelper;
import com.hfxb.app.core.interceptor.FrontSessionInterceptor;
import com.hfxb.app.core.interceptor.SessionInterceptor;
import com.hfxb.app.core.plugins.AutoTableBindPlugin;
import com.hfxb.app.core.utils.FileUtils;
import com.hfxb.app.core.utils.PropertiesUtils;
import com.hfxb.app.core.utils.StringUtils;
import com.hfxb.app.web.base.BaseController;
import com.jfinal.config.*;
import com.jfinal.ext.interceptor.SessionInViewInterceptor;
import com.jfinal.i18n.I18n;
import com.jfinal.i18n.I18nInterceptor;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.dialect.MysqlDialect;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.plugin.ehcache.EhCachePlugin;
import org.apache.commons.configuration.ConfigurationException;
import org.beetl.core.GroupTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * JFinal初始化配置
 * @author qq
 *
 */
public class InitConfig extends JFinalConfig {
	
	private static final Logger logger = LoggerFactory.getLogger(InitConfig.class);
	
	@Override
	public void configConstant(Constants me) {
		
		try {
			com.hfxb.app.core.common.Constants.config = PropertiesUtils.load(
					FileUtils.locateAbsolutePathFromClasspath("main.properties").getName(), "utf-8");
		} catch (ConfigurationException e) {
			logger.error("load properties error: " + e.getMessage(), e);
		}
		
		YiBeetlRenderFactory brf = new YiBeetlRenderFactory();
		GroupTemplate gt = YiBeetlRenderFactory.groupTemplate;
		//beetl template call: if(shiro.isGuest())
		gt.registerFunctionPackage("shiro",new ShiroBeetl());
		gt.registerFunctionPackage("str",new StringUtils());
		
		me.setMainRenderFactory(brf);
		me.setDevMode(com.hfxb.app.core.common.Constants.config.getBoolean("dev", false));
		
		//未授权：登录失败
		me.setError401View("/error/401.html");
		//禁止：禁止执行访问
		me.setError403View("/error/403.html");
		//找不到 
		me.setError404View("/error/404.html");
		//内部服务器错误
		me.setError500View("/error/500.html");
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void configRoute(final Routes me) {
		List<Class> modelClasses = AnnotationHelper.findClasses(BaseController.class);
        Action action = null;
        for (Class modelClass : modelClasses) {
        	action = (Action) modelClass.getAnnotation(Action.class);
            if (action != null) {
            	//if view path attribute exist, add it
                if (StrKit.notBlank(action.view())) {
                    me.add(action.action(), modelClass, action.view());
                    logger.debug("auto config route, addRoute({}, {}, {})", new Object[]{action.action(), modelClass, action.view()});
                } else {
                	me.add(action.action(), modelClass);
                	logger.debug("auto config route, addRoute({}, {})", new Object[]{action.action(), modelClass});
                }
            }
        }
	}

	@Override
	public void configPlugin(Plugins me) {
		
		// 加载数据库配置
		loadPropertyFile("jdbc.properties");
				
		DruidPlugin dp=new DruidPlugin(getProperty("jdbc.url"), 
				getProperty("jdbc.username"), getProperty("jdbc.password"),getProperty("jdbc.driverClassName"));
		
		dp.addFilter(new StatFilter());
		
		dp.setInitialSize(getPropertyToInt("db.initialSize", 10));
		dp.setMinIdle(getPropertyToInt("db.minIdle", 10));
		dp.setMaxActive(getPropertyToInt("max.active", 100));
		dp.setMaxWait(getPropertyToInt("db.maxWait", 60000));
		dp.setTimeBetweenEvictionRunsMillis(getPropertyToInt("db.timeBetweenEvictionRunsMillis", 120000));
		dp.setMinEvictableIdleTimeMillis(getPropertyToInt("db.minEvictableIdleTimeMillis", 120000));
		
		WallFilter wall = new WallFilter();
		wall.setDbType(getProperty("db.type"));
		dp.addFilter(wall);
		me.add(dp);	
		
		//auto bind tables, TableNameStyleEnum is useless
        AutoTableBindPlugin autoTableBindPlugin = new AutoTableBindPlugin(dp, TableNameStyleEnum.LOWER);
        autoTableBindPlugin.setShowSql(com.hfxb.app.core.common.Constants.config.getBoolean("show_sql", false));
        autoTableBindPlugin.setDialect(new MysqlDialect());
        me.add(autoTableBindPlugin);
        
        me.add(new EhCachePlugin());
	}

	@Override
	public void configInterceptor(Interceptors me) {
		
		//判断用户是否登陆
		me.add(new SessionInterceptor());
		me.add(new FrontSessionInterceptor());
		
		//将httpsession转换成jfinalsession， 用于解决获取不到httpsession的问题
		me.add(new SessionInViewInterceptor());
		
		I18n.setDefaultBaseName("message");
		me.add(new I18nInterceptor());
	}

	@Override
	public void configHandler(Handlers me) {
		me.add(new BaseHandler());
//		me.add(new YiFakeStaticHandler());
	}

	@Override
	public void afterJFinalStart() {
		/*String xml = FileUtils.readFile(FileUtils.locateAbsolutePathFromClasspath("fake.xml"));
		org.yi.core.common.Constants.fake = FakeUtils.convery2Model(xml, FakeModel.class);*/
		super.afterJFinalStart();
	}
	
}
