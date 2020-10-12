package com.markerhub.controller;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.markerhub.common.lang.Result;
import com.markerhub.entity.*;
import com.markerhub.handleData.roleData;
import com.markerhub.service.PermissionService;
import com.markerhub.service.RolePermissionService;
import com.markerhub.service.RoleService;
import com.markerhub.service.UserRoleService;
import com.markerhub.shiro.ShiroUtil;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 关注公众号：MarkerHub
 * @since 2020-07-14
 */
@RestController
//@RequiresAuthentication
@RequestMapping("/role")
public class RoleController {


    @Autowired
    RoleService roleService;
    @Autowired
    RolePermissionService rolePermissionService;
    @Autowired
    PermissionService permissionService;
    @Autowired
    UserRoleService userRoleService;
    @RequiresAuthentication
    @GetMapping("/list")
    public Result list(@RequestParam(defaultValue = "1") Integer currentPage, @RequestParam(defaultValue = "10") Integer size) {

        Page page = new Page(currentPage, size);
        LambdaQueryWrapper<Role> d = new LambdaQueryWrapper<Role>();
        d.orderByAsc(Role::getId);
        IPage pageData = roleService.page(page, d);

        List<roleData> result = new ArrayList<>();
//        List<Role> roleList=roleService.list();
        List<Role> roleList = pageData.getRecords();


        for (Role role : roleList) {
            roleData add = new roleData();
            add.setId(role.getId());
            add.setRoleDesc(role.getRoleDesc());
            add.setRoleName(role.getRoleName());
            List<Permission> permissions = new ArrayList<>();

            List<RolePermission> rolePermissions = rolePermissionService.selectByRoleId(role.getId());
            for (RolePermission rolePermission : rolePermissions) {
                Permission permission = permissionService.getById(rolePermission.getPermissionId());
                permissions.add(permission);
            }
            add.setPermissions(permissions);
            result.add(add);
        }
        pageData.setRecords(result);

        return Result.succ(pageData);
    }

    @RequiresAuthentication
    @PostMapping("/save")
    public Result edit(@Validated @RequestBody Role permission) {

        Role temp = null;
        System.out.println("permission.getId()===" + permission.getId());
        if (permission.getId() != null) {
            temp = roleService.getById(permission.getId());
            System.out.println(ShiroUtil.getProfile().getId());
//            Assert.isTrue(temp.getId().longValue() == ShiroUtil.getProfile().getId().longValue(), "没有权限编辑");

        } else {

            temp = new Role();

        }

        BeanUtil.copyProperties(permission, temp, "id");
        roleService.saveOrUpdate(temp);

        return Result.succ(null);
    }

    @RequiresAuthentication
    @PostMapping("/addUserAndRole")
    public Result addUserAndRole(@Validated @RequestBody Map<String, int[]> request) {
        System.out.println(request);
       int[] userId= request.get("userId");

       int[] roleIds =  request.get("roleIds");
//        for (Integer roleId : roleIds) {
//            System.out.println(roleId);
//        }

        UserRole temp = new UserRole();
        for (Integer roleId : roleIds) {
                temp.setUserId(userId[0]); //获取传入的用户ID
                temp.setRoleId(roleId);
                userRoleService.save(temp);
        }
        return Result.succ(null);

    }
    @GetMapping("/noLogin")
    public Result noLogin() {

        return Result.fail(401,"请登录",null);
    }
    @GetMapping("/noAuth")
    public Result noAuth() {

        return Result.fail(401,"未经授权，无法访问",null);
    }



    @RequiresAuthentication
    @PostMapping("/delete")
    public Result delete(@Validated @RequestBody Integer[] ids) {

        System.out.println(ids);
        for (Integer id : ids) {
            roleService.removeById(id);
        }

        return Result.succ(null);
    }



}
