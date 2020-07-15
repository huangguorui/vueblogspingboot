package com.markerhub.controller;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.markerhub.common.lang.Result;
import com.markerhub.entity.Blog;
import com.markerhub.entity.Permission;
import com.markerhub.entity.Role;
import com.markerhub.entity.RolePermission;
import com.markerhub.handleData.roleData;
import com.markerhub.service.PermissionService;
import com.markerhub.service.RolePermissionService;
import com.markerhub.service.RoleService;
import com.markerhub.shiro.ShiroUtil;
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
 * @since 2020-07-14
 */
@RestController
@RequestMapping("/role")
public class RoleController {


    @Autowired
    RoleService roleService;
    @Autowired
    RolePermissionService rolePermissionService;
    @Autowired
    PermissionService permissionService;

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


//        List<Role> records = pageData.getRecords();
//        for(Role item:records){
//            //获取角色id，在关联表查询
//            System.out.println(item.getId());
//
//            List<RolePermission> rolePermissionList = rolePermissionService.list(new QueryWrapper<RolePermission>().orderByDesc("roleId").eq("roleId", item.getId()).select());
//            for(RolePermission row:rolePermissionList){
//                System.out.println("permissionId:"+row.getPermissionId());
//
//                //通过id查询出资源列表
//                List<Permission> prmissionList = permissionService.list(new QueryWrapper<Permission>().orderByDesc("id").eq("id", row.getPermissionId()).select());
////                Role role = new Role();
////                role.setPermissions(prmissionList);
//
//
//                System.out.println(prmissionList);
////                item.setPermissionList(prmissionList);
//            }
//
//        }


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
    @PostMapping("/delete")
    public Result delete(@Validated @RequestBody Integer[] ids) {

        System.out.println(ids);
        for (Integer id : ids) {
            roleService.removeById(id);
        }

        return Result.succ(null);
    }


}
