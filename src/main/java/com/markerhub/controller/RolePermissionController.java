package com.markerhub.controller;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.markerhub.common.lang.Result;
import com.markerhub.entity.Role;
import com.markerhub.entity.RolePermission;
import com.markerhub.service.RolePermissionService;
import com.markerhub.service.RoleService;
import com.markerhub.shiro.ShiroUtil;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 关注公众号：MarkerHub
 * @since 2020-07-14
 */
@RestController
@RequestMapping("/rolePermission")
public class RolePermissionController {
    @Autowired
    RolePermissionService permissionService;

    @GetMapping("/list")
    public Result list(@RequestParam(defaultValue = "1") Integer currentPage, @RequestParam(defaultValue = "10") Integer size) {

        Page page =  new Page(currentPage, size);
        IPage pageData = permissionService.page(page, new QueryWrapper<RolePermission>().orderByDesc("id"));
        return Result.succ(pageData);
    }
    @RequiresAuthentication
    @PostMapping("/save")
    public Result edit(@Validated @RequestBody RolePermission permission) {

        RolePermission temp = null;
        System.out.println("permission.getId()==="+permission.getId());
        if(permission.getId() != null) {
            temp = permissionService.getById(permission.getId());
            System.out.println(ShiroUtil.getProfile().getId());
//            Assert.isTrue(temp.getId().longValue() == ShiroUtil.getProfile().getId().longValue(), "没有权限编辑");
        } else {
            temp = new RolePermission();
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
}
