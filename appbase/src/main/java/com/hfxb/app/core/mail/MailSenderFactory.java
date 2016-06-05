package com.hfxb.app.core.mail;

import com.hfxb.app.core.common.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * 发件箱工厂
 * 
 * @author masque.java@gmail.com
 * 
 */
public class MailSenderFactory {
	
	private static final Logger logger = LoggerFactory.getLogger(MailSenderFactory.class);
 
    /**
     * 服务邮箱
     */
    private static SimpleMailSender serviceSms = null;
 
    /**
     * 获取邮箱
     * 
     * @param type
     *            邮箱类型
     * @return 符合类型的邮箱
     * @throws IOException
     */
    public static SimpleMailSender getSender() throws IOException {
        if (initUser() == null) {
        	logger.error("初始化邮件组件错误！");
        }
        return serviceSms;
    }
 
    private static SimpleMailSender initUser() throws IOException {
    	serviceSms = new SimpleMailSender(Constants.config.getString("email.username"), Constants.config.getString("email.password"));
    	return serviceSms;
    }
}