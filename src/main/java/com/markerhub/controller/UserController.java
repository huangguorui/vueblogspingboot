package com.markerhub.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.markerhub.common.lang.Result;
import com.markerhub.entity.*;
import com.markerhub.handleData.roleData;
import com.markerhub.handleData.userData;
import com.markerhub.service.*;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 关注公众号：MarkerHub
 * @since 2020-07-10
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;
    @Autowired
    RoleService roleService;


    @Autowired
    UserRoleService userRoleService;


    @RequiresAuthentication
    @GetMapping("/list")
    public Result list(@RequestParam(defaultValue = "1") Integer currentPage, @RequestParam(defaultValue = "10") Integer size) {

        Page page = new Page(currentPage, size);
        LambdaQueryWrapper<User> d = new LambdaQueryWrapper<User>();

        d.orderByAsc(User::getId);
        IPage pageData = userService.page(page, d);

        List<userData> result = new ArrayList<>();
//        List<Role> roleList=roleService.list();
        List<User> userList = pageData.getRecords();


        for (User user : userList) {
            userData add = new userData();
            add.setId(user.getId());
            add.setUsername(user.getUsername());
            add.setEmail(user.getEmail());
            List<Role> roles = new ArrayList<>();

            List<UserRole> userRoles = userRoleService.selectByUserId(user.getId());
            for (UserRole userRole : userRoles) {
                //获取当前用户的所有全新
                Role role = roleService.getById(userRole.getRoleId());
                roles.add(role);
            }
            add.setRoles(roles);
            result.add(add);
        }
        pageData.setRecords(result);

        return Result.succ(pageData);
    }

    @RequiresAuthentication
    @GetMapping("/index")
    public Object index() {
        return Result.succ(userService.getById(1l));

    }
    @RequiresAuthentication
    @PostMapping("/save")
    public Result save(@Validated @RequestBody User user) {
        return Result.succ(user);

    }
    @RequiresAuthentication
    @PostMapping("/delete")
    public Result delete(@Validated @RequestBody Integer[] ids) {

        for (Integer id:ids){
            userService.removeById(id);
        }

        return Result.succ(null);
    }

}
