package com.markerhub.shiro;

import cn.hutool.core.bean.BeanUtil;
import com.markerhub.entity.User;
import com.markerhub.service.UserService;
import com.markerhub.util.JwtUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/*不加mapperscan报错      最终错误原因 少了一个Component
* Action:

Consider defining a bean of type 'com.markerhub.shiro.AccountRealm' in your configuration.
* */
@Component
public class AccountRealm extends AuthorizingRealm {
    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    UserService userService;



    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
        //        return super.supports(token);
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        //获取当前登录的对象
        Subject subject = SecurityUtils.getSubject();
        AccountProfile accountProfile = (AccountProfile) subject.getPrincipal();
        System.out.println(accountProfile);
//
//        User user = userService.getById(accountProfile.getId());
//        System.out.println("user");
//        System.out.println(user);

//        List<RoleResourceModule> roleResourceModules=serviceRRM.getUserResource(currentUser.getId());
        //accountProfile

//        User currentUser = (User) subject.getSession().getAttribute(Constant.USER_PRIMARY);
        //设置权限
//        List<RoleResourceModule> roleResourceModules=serviceRRM.getUserResource(currentUser.getId());
       // for(RoleResourceModule item:roleResourceModules){
//        info.addStringPermission("permission+list");//设置用户权限
        info.addStringPermission("permission+save");
        info.addStringPermission("permission+delete");
        info.addStringPermission("permission+addRoleAndPermission");
      //  }
//        LOGGER.info("用户[{}]获取了授权",currentUser.getUserName());
        System.out.println("-------------------------");
        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        JwtToken jwtToken=(JwtToken)token;
        String userId = jwtUtils.getClaimByToken((String) jwtToken.getPrincipal()).getSubject();

        User user = userService.getById(Long.valueOf(userId));
        if(user==null){
            throw new UnknownAccountException("账户不存在");
        }
        if(user.getStatus()==-1){
            throw new LockedAccountException("账户已锁定");
//            throw new UnknownAccountException("账户不存在");
        }

        AccountProfile profile = new AccountProfile();

        BeanUtil.copyProperties(user,profile);

        return new SimpleAuthenticationInfo(profile,jwtToken.getCredentials(),getName());
    }


}
