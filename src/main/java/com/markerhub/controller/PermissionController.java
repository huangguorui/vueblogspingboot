package com.markerhub.controller;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.markerhub.common.lang.Result;
import com.markerhub.entity.Blog;
import com.markerhub.entity.Permission;
import com.markerhub.entity.RolePermission;
import com.markerhub.service.BlogService;
import com.markerhub.service.PermissionService;
import com.markerhub.service.RolePermissionService;
import com.markerhub.shiro.ShiroUtil;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 关注公众号：MarkerHub
 * @since 2020-07-13
 */

@RestController
@RequestMapping("/permission")
public class PermissionController {


    @Autowired
    PermissionService permissionService;
    @Autowired
    RolePermissionService rolePermissionService;
    @GetMapping("/list")
    public Result list(@RequestParam(defaultValue = "1") Integer currentPage, @RequestParam(defaultValue = "10") Integer size,String permissionName) {

       Page page =  new Page(currentPage, size);
        IPage pageData=null;
        if(permissionName!=null){
             pageData = permissionService.page(page, new QueryWrapper<Permission>().orderByDesc("id").eq("permissionName",permissionName));

        }else{
             pageData = permissionService.page(page, new QueryWrapper<Permission>().orderByDesc("id"));

        }

        return Result.succ(pageData);
    }
    @RequiresAuthentication
    @PostMapping("/save")
    public Result edit(@Validated @RequestBody Permission permission) {

        Permission temp = null;
        System.out.println("permission.getId()==="+permission.getId());
        if(permission.getId() != null) {
            temp = permissionService.getById(permission.getId());
            // 只能编辑自己的文章
            System.out.println(ShiroUtil.getProfile().getId());
//            Assert.isTrue(temp.getId().longValue() == ShiroUtil.getProfile().getId().longValue(), "没有权限编辑");

        } else {

            temp = new Permission();
//            temp.setUserId(ShiroUtil.getProfile().getId());
//            temp.setCreated(LocalDateTime.now());
//            temp.setStatus(0);
        }

        BeanUtil.copyProperties(permission, temp, "id");
        permissionService.saveOrUpdate(temp);

        return Result.succ(null);
    }


    @RequiresAuthentication
    @PostMapping("/delete")
    public Result delete(@Validated @RequestBody Integer[] ids) {

        System.out.println(ids);
        for (Integer id:ids){
            permissionService.removeById(id);
        }

        return Result.succ(null);
    }


//    @RequiresAuthentication
    @PostMapping("/addRoleAndPermission")
    public Result addRoleAndPermission(@Validated @RequestBody Map<String, int[]> request) {
        int[] roleId= request.get("roleId");
        int[] permissionIds =  request.get("permissionIds");
        System.out.println(roleId[0]);
        System.out.println(permissionIds);
        RolePermission temp = new RolePermission();
        for (Integer permissionId:permissionIds){
               temp.setPermissionId(permissionId);
               temp.setRoleId(roleId[0]);
               rolePermissionService.save(temp);
        }
        return Result.succ(null);
    }

    //给角色添加资源
//    @RequiresAuthentication
//    @PostMapping("/addRoleAndPermission")
//    public Result addRoleAndPermission(Integer[] ids) {
//
//        for (Integer id:ids){
//            System.out.println(id);
//        }
//
////         Integer[] roleIds,Integer permissionId
//        //Integer[] roleIds,Integer permissionId
//             RolePermission temp = new RolePermission();
//
////        for (Integer roleId:roleIds){
//////            temp.setPermissionId(permissionId);
//////            temp.setRoleId(roleId);
////            System.out.println(roleId);
////        }
//
////        System.out.println(permissionId);
//        //
////        RolePermission temp = new RolePermission();
////        for (Integer roleId:roleIds){
////            temp.setPermissionId(permissionId);
////            temp.setRoleId(roleId);
////
////            permissionService.saveOrUpdate(temp);
////
////        }
//
//        return Result.succ(null);
//    }
}
