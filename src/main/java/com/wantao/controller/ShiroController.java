package com.wantao.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ShiroController {
	private static final Logger logger = LoggerFactory.getLogger(ShiroController.class);

	@PostMapping("/login")
	public String login(String username, String password) {
		// 1.创建Subject实例
		Subject subject = SecurityUtils.getSubject();
		// 2.判断当前用户是否登录
		if (subject.isAuthenticated() == false) {
			// 3.将用户名和密码封装
			UsernamePasswordToken token = new UsernamePasswordToken(username, password);
			try {
				// 4.登录
				subject.login(token);
				logger.info("登录成功");
			} catch (AuthenticationException e) {
				logger.info("登录失败");
				return "error";
			}
		}
		return "success";
	}
}
