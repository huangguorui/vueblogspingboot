package com.markerhub.config;

import com.markerhub.entity.User;
import com.markerhub.service.UserService;
import com.markerhub.shiro.AccountProfile;
import com.markerhub.shiro.AccountRealm;
import com.markerhub.shiro.JwtFilter;
import com.markerhub.util.JwtUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.SecurityManager; // //需要注册这个包才可以成功，不然报错
import org.apache.shiro.mgt.SessionsSecurityManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {

    @Autowired
    JwtFilter jwtFilter;



    @Bean
    public SessionManager sessionManager(RedisSessionDAO redisSessionDAO) {
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();

        // inject redisSessionDAO
        sessionManager.setSessionDAO(redisSessionDAO);
        return sessionManager;
    }

    @Bean
    public SessionsSecurityManager securityManager(AccountRealm accountRealm,
                                                   SessionManager sessionManager,
                                                   RedisCacheManager redisCacheManager) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager(accountRealm);

        //inject sessionManager
        securityManager.setSessionManager(sessionManager);

        // inject redisCacheManager
        securityManager.setCacheManager(redisCacheManager);
        return securityManager;
    }

    @Bean
    public ShiroFilterChainDefinition shiroFilterChainDefinition() {
        DefaultShiroFilterChainDefinition chainDefinition = new DefaultShiroFilterChainDefinition();
        Map<String, String> filterMap = new LinkedHashMap<>();
        System.out.println("----执行---");

        //获取ID，查询资源列表
//        Subject subject = SecurityUtils.getSubject();
//        AccountProfile accountProfile = (AccountProfile) subject.getPrincipal();
//        System.out.println(accountProfile.getId());
//        filterMap.put("/permission/list", "perms[permission+list]");
        filterMap.put("/permission/save", "perms[permission+save]");
        filterMap.put("/permission/addRoleAndPermission", "perms[permission+addRoleAndPermission]");
        filterMap.put("/permission/delete", "perms[permission+delete]");
        filterMap.put("/user/list", "perms[user:list]");


      filterMap.put("/uploads/**", "anon");//img      //允许图片访问  springboot+shiro放开目录权限
//       filterMap.put("/static/**", "anon");//img      //允许图片访问  springboot+shiro放开目录权限


//        拦截了所有的
        filterMap.put("/**", "jwt"); // 主要通过注解方式校验权限
        chainDefinition.addPathDefinitions(filterMap);
        return chainDefinition;
    }
    @Bean("shiroFilterFactoryBean")
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager,
                                                         ShiroFilterChainDefinition shiroFilterChainDefinition) {
        ShiroFilterFactoryBean shiroFilter = new ShiroFilterFactoryBean();
        System.out.println();
        //        shiroFilter.();
        //设置登录页面
        shiroFilter.setLoginUrl("/role/noLogin");
//        ShiroFilterFactoryBean shiroFilterFactoryBean  = new ShiroFilterFactoryBean();
//        filterChainDefinitionMap.put("/img/**", "anon");//img
//        filterChainDefinitionMap.put("/js/**", "anon");//js
//        filterChainDefinitionMap.put("/css/**", "anon");//css

        //设置未授权页面
        shiroFilter.setUnauthorizedUrl("/role/noAuth");
        shiroFilter.setSecurityManager(securityManager);

        Map<String, Filter> filters = new HashMap<>();
        filters.put("jwt", jwtFilter);
        shiroFilter.setFilters(filters);
        Map<String, String> filterMap = shiroFilterChainDefinition.getFilterChainMap();
        shiroFilter.setFilterChainDefinitionMap(filterMap);
        return shiroFilter;
    }





}
