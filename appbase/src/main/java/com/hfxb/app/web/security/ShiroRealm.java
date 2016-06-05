package com.hfxb.app.web.security;

import com.hfxb.app.core.common.Constants;
import com.hfxb.app.core.enums.AccountStateEnums;
import com.hfxb.app.core.enums.RoleEnum;
import com.hfxb.app.core.exceptions.BaseException;
import com.hfxb.app.web.account.entity.AccountEntity;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.DbKit;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;

import javax.sql.DataSource;

public class ShiroRealm extends JdbcRealm {

	/**
	 * authorization
	 */
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principal) {
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		
		String loginname = String.valueOf(principal.iterator().next());
		AccountEntity user = AccountEntity.dao.getByName(loginname);
		
		if(user != null && RoleEnum.MANAGER.getCode().equals(user.getStr("role"))){
			//one user has one role only
			info.addRole(user.getStr("role"));
		}
		
		return info;
	}

	/**
	 * login authentication
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token)  {
		
		UsernamePasswordToken upt = (UsernamePasswordToken) token ;
		
		try {
			String loginName = upt.getUsername().trim();
			String password = String.valueOf(upt.getPassword());
			
			//determine whether an agent exists
			AccountEntity account = AccountEntity.dao.getByName(loginName);
			
			//if exists, determine whether the password is right or not
			if(account != null && RoleEnum.MANAGER.getCode().equals(account.getStr("role"))){
				// if password is blank or password not equal to passwd parameter, it declare passwd parameter wrong
				if(StrKit.isBlank(password) || !password.equals(account.getStr("passwd"))){
					throw new IncorrectCredentialsException("password error!");
				} else {
					Session session = SecurityUtils.getSubject().getSession();
					session.setAttribute(Constants.KEY_LOGIN_ACCOUNT, account);
					return new SimpleAuthenticationInfo(loginName, password,  getName());
				}
			}else{
				throw new UnknownAccountException("loginname: " + loginName + " not found!");
			}
		} catch(BaseException e) {
			throw new BaseException("username or password error!");
		}
		
	}

	@Override
	public void setDataSource(DataSource dataSource) {
		super.setDataSource(DbKit.getConfig().getDataSource());
	}

	
	
}
