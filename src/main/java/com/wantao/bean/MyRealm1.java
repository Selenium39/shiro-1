package com.wantao.bean;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.realm.AuthenticatingRealm;
import org.apache.shiro.util.ByteSource;

public class MyRealm1 extends AuthenticatingRealm {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.shiro.realm.AuthenticatingRealm#doGetAuthenticationInfo(org.apache
	 * .shiro.authc.AuthenticationToken)
	 * 1.获取认证消息.如果数据库中没有数据,返回null,如果得到正确的用户名和密码,返回指定类型的对象 2.AuthenticationInfo
	 * 可以使用SimpleAuthenticationInfo实现类,返回给你正确的用户名和密码 3.token参数,就是我们需要的token
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		SimpleAuthenticationInfo info = null;
		// 1.将AuthenticationToken token转换为UserNamePasswordToken
		UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) token;
		// 2.获取用户名
		String username = usernamePasswordToken.getUsername();
		// 3.查询数据库看是否存在指定的用户名和密码
		try {
			System.out.println("myRealm1被调用了");
			Object principal = "Selenium";// 用户
			Object credentials = "123456";// 密码
			String realmName = this.getName();
			// ByteSource salt值加密
			ByteSource salt = ByteSource.Util.bytes(principal);
			// 将存入数据库的密码进行MD5加密
			SimpleHash sh = new SimpleHash("sha1", credentials, salt, 2019);
			// info = new SimpleAuthenticationInfo(principal, sh, realmName);
			info = new SimpleAuthenticationInfo(principal, sh, salt, realmName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 4.如果查询到了,封装查询结果,返回给我们调用
		// 5.没有查询到,返回异常
		return info;
	}

}
