package com.hfxb.app.web.init;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Created by gravel on 16/6/2.
 */
public class NFDFlightDataTaskListener implements ServletContextListener {

    public void contextInitialized(ServletContextEvent event) {
        new TimerManager();
    }

    public void contextDestroyed(ServletContextEvent event) {
    }

}
