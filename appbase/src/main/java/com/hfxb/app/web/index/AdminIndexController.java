package com.hfxb.app.web.index;

import com.hfxb.app.core.annotation.Action;
import com.hfxb.app.web.base.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by gravel on 16/5/10.
 */

@Action(action = "/admin")
public class AdminIndexController  extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(AdminIndexController.class);

    public void index() {
        redirect("/admin/system");
    }

    public void login(){
        forwardAction("/admin/account/login");
    }
}