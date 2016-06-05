package com.hfxb.app.core.helper;

import com.hfxb.app.core.common.Constants;
import com.hfxb.app.core.utils.CookieUtils;
import com.hfxb.app.web.account.entity.AccountEntity;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import net.minidev.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DuoshuoHelper {
	
	private static final Logger logger = LoggerFactory.getLogger(DuoshuoHelper.class);
	
	public static void token(AccountEntity account, HttpServletRequest request, HttpServletResponse response) {
		JSONObject userInfo = new JSONObject();
        
        userInfo.put("short_name", Constants.config.getString("duoshuo.short_name"));//必须项
        userInfo.put("user_key", account.get("id"));//必须项
        userInfo.put("name", account.get("login_name"));//可选项
        
        Payload payload = new Payload(userInfo);
        
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);
        
        // Create JWS object
        JWSObject jwsObject = new JWSObject(header, payload);
        try {
        	// Create HMAC signer
        	JWSSigner signer = new MACSigner(Constants.config.getString("duoshuo.secret").getBytes());
            jwsObject.sign(signer);
            // Serialise JWS object to compact format
            String token = jwsObject.serialize();
            CookieUtils.addCookie("duoshuo_token", token, request.getServerName(), Constants.config.getInt("cookie.period", 3600), response);
        } catch (JOSEException e) {
        	logger.error("产生duoshuo本地身份token出错", e);
            return;
        }        
        
	}

}
