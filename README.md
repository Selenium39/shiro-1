# shiro-1
```
javase环境下搭建shiro
         1.导入jar包
          2.配置文件:存储临时文件
                 shiro.ini文件:存储数据,用户名,密码,角色,权限
          3.代码
               // 1.获取安全管理器
		Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro.ini");
		SecurityManager securityManager = factory.getInstance();
		// 2.设置安全管理器
		SecurityUtils.setSecurityManager(securityManager);
		// 3.获取subject对象,即要登录的对象
		Subject subject = SecurityUtils.getSubject();
spring与shiro集成
             1.导入jar包
              2.搭建好spring,springmvc
              3.搭建shiro环境
                    1.web.xml
                                     <filter>
		                           <filter-name>shiroFilter</filter-name>
		                          <filter-class>org.springframework.web.filter.DelegatingFilterProxy</filter-class>
		                           <init-param>
			                      <param-name>targetFilterLifecycle</param-name>
			                      <param-value>true</param-value>
		                        </init-param>
	                               </filter>
	                               <filter-mapping>
		                              <filter-name>shiroFilter</filter-name>
		                              <url-pattern>/*</url-pattern>
	                               </filter-mapping>
                          
                 2.在spring.xml中进行相关配置

shiro配置的核心:spring.xml欧中配置的一个bean<bean class="org.apache.shiro.spring.web.ShiroFilterFactoryBean"></bean>

web.xml中DelegatingFilterProxy作用:入口,完成servlet容器到ioc容器的关联,到spring容器中找filter-name相同名字的bean实例
如果没有配置targetBeanName属性,则filter-name必须与bean的name相同

表单提交的数据封装到 usernamePasswordToken 数据库查询到的数据封装到SimpleAuthenticationInfo

加密:MD5, SHA1
1.存储数据时需要将用户输入的明文继续加密(Hibernate当中涉及md5加密)  (Ream.java)
                        //将存入数据库的密码进行MD5加密
			SimpleHash sh = new SimpleHash("md5", credentials, null, 2019);
2.前端用户输入的值,进行加密,string字符串经过md5加密(spring.xml)
                      <!-- 将前端的输入的密码进行md5的加密 -->
		      <property name="credentialsMatcher">
			  <bean
				class="org.apache.shiro.authc.credential.HashedCredentialsMatcher">
				<property name="hashAlgorithmName" value="md5"></property>
				<property name="hashIterations" value="2019"></property>
			  </bean>
		     </property>
盐值加密:原有加密的基础上加密(两个相同密码在数据库存储的值还是不一样)
        1.前端token当中获取的密码应该进行盐值加密
              ByteSource salt = ByteSource.Util.bytes(principal);
              new SimpleAuthenticationInfo(principal, sh, salt, realmName);
        2.数据库中的值进行盐值加密
               new SimpleHash("md5", credentials, salt, 2019);
多Realm获取数据:将一个密码存放在多个数据库中,建立多个Realm,并且每个realm使用不同的加密方式,在subject.login中会进行多个realm的调用,执行多次验证,提高数据安全性
         1.第一种方法:
            <!-- 多个realm -->
		<property name="realms">
			<list>
				<ref bean="jdbcRealm" />
				<ref bean="jdbcRealm1" />
			</list>
		</property>
           2.第二种方法:通过ModularRealmAuthenticator认证器

shiro认证策略:多个realm访问多个数据库,如何判断是否登录成功
  1.FirstSucessfulStrategy    第一个realm认证成功就算成功
  2.AtLeastOneSuccessfulStrategy 多个realm认证时,一个成功代表成功(默认)
  3.AllSuccessfulStrategy 多个realm认证时,全部成功才代表成功

授权:控制哪一个用户可以访问哪一个web资源
     1.编程式
         if(subect.hasRole){
                  //有权限
        }else{
                 //没有权限
         }
     2.注解式:通过在执行的java方法上注解
          @RequireRoles("admin")
          public void hello(){
             //有权限
          }
     3.jsp/gsp标签
    <shiro:hasRole name="admin">有权限</shiro:hasRole>
1.在FilterChainDefinitions中定义web资源对应角色
      web资源=roles[角色名称]
       /admin.jsp=roles[admin]
2.一个用户进行登录认证成功之后,之中还是要查找认证成功的角色拥有什么角色.realm可以和数据库交互,获取指定认证成功的用户对应的角色
        1.我们可以自定义一个Realm,继承AuthorizingRealm,认证和授权
        2.在ioc容器配置指定的Realm的bean实例(授权器)
        3.告知securityManager使用哪一个授权管理器
```
参考我<a src="https://ke.qq.com/webcourse/index.html#cid=197393&term_id=100233954&taid=1223524513678097&vid=x14177geywk" target="_blank">点击我</a>
