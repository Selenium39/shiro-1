package com.wantao.test;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;

/**
 * @author wantao
 * @date 2019-02-08 21:41
 * @description: 1.获取安全管理器 2.获取用户 3.用户登录验证 4.权限管理 5.角色 管理 6.session
 */
public class Shiro1Test {
	private static final Logger logger = LoggerFactory.getLogger(Shiro1Test.class);

	public static void main(String[] args) {
		// 1.获取安全管理器
		Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro.ini");
		SecurityManager securityManager = factory.getInstance();
		// 2.设置安全管理器
		SecurityUtils.setSecurityManager(securityManager);
		// 3.获取subject对象,即要登录的对象
		Subject subject = SecurityUtils.getSubject();
		Session session = subject.getSession();

		session.setAttribute("name", "selenuim");
		String value = (String) session.getAttribute("name");
		if (value != null) {
			System.out.println("shiro已经获取的session中的值" + value);
		}
		// 用户登录管理
		if (subject.isAuthenticated() == false) {// 是否登录
			UsernamePasswordToken token = new UsernamePasswordToken("selenium", "123456");
			logger.info("principal:"+token.getPrincipal());
			logger.info("credentials:"+token.getCredentials());
			token.setRememberMe(true);
			try {
				subject.login(token);
				logger.info("用户名密码正确,登录成功");
			} catch (UnknownAccountException e) {
				logger.info("账号不存在");
			} catch (IncorrectCredentialsException e) {
				logger.info("密码错误");
			} catch (LockedAccountException e) {
				logger.info("用户已经锁死");
			} catch (AuthenticationException e) {
				logger.info("认证异常");
			}
		}
		// 判断用户是否有角色
		if (subject.hasRole("role1")) {
			logger.info("有指定的角色");
		} else {
			logger.info("不拥有指定的角色");
		}
		// 判断用户是否有权限
		if (subject.isPermitted("user:create")) {
			logger.info("用户有指定的权限");
		} else {
			logger.info("当前用户没有指定的权限");
		}
		subject.logout();
	}
}
