package com.markerhub.controller;


//import cn.hutool.db.Page;
//import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import com.baomidou.mybatisplus.core.metadata.IPage;
//import com.markerhub.common.lang.Result;
//import com.markerhub.entity.Blog;
//import com.markerhub.service.BlogService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.validation.annotation.Validated;
//import org.springframework.web.bind.annotation.*;
//
//import javax.management.Query;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.markerhub.common.lang.Result;
import com.markerhub.entity.Blog;
import com.markerhub.entity.Role;
import com.markerhub.service.BlogService;
//import com.markerhub.util.ShiroUtil;
import com.markerhub.shiro.ShiroUtil;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

//研发权限控制系统
/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 关注公众号：MarkerHub
 * @since 2020-07-10
 */
@ResponseBody
@RestController
@RequestMapping("/article")
public class BlogController {

    @Autowired
    BlogService blogService;




    @GetMapping("/list")
    public Result blogs(@RequestParam(defaultValue = "1") Integer currentPage,@RequestParam(defaultValue = "10") Integer size) {

        Page page =  new Page(currentPage, size);
        IPage pageData = blogService.page(page, new QueryWrapper<Blog>().orderByDesc("created"));
//          List recordsList=  pageData.getRecords();
//        for (Object records : recordsList) {
//            records.
//        }
//         pageData.setRecords()

        return Result.succ(pageData);
    }

    @GetMapping("/{id}")
    public Result detail(@PathVariable(name = "id") Long id){
        Blog blog = blogService.getById(id);
        Assert.notNull(blog,"该博客已被删除");
        return Result.succ(blog);
    }

    @RequiresAuthentication
    @PostMapping("/save")

//    @RequiresPermissions(value={"blog+edit"})
//    /blog/edit
//
//    /blog/edit  blog+edit
    //@RequiresPermissions(value={"user:a", "user:b"}, logical= Logical.AND)

    public Result edit(@Validated @RequestBody Blog blog) {

        Blog temp = null;
        if(blog.getId() != null) {
            temp = blogService.getById(blog.getId());
            // 只能编辑自己的文章
            Assert.isTrue(temp.getUserId().longValue() == ShiroUtil.getProfile().getId().longValue(), "没有权限编辑");

        } else {

            temp = new Blog();
            temp.setUserId(ShiroUtil.getProfile().getId());
            temp.setCreated(LocalDateTime.now());
            temp.setStatus(1);
            BeanUtil.copyProperties(blog, temp, "id", "userId", "created", "status");
            blogService.saveOrUpdate(temp);

            return Result.succ(null);
        }

        BeanUtil.copyProperties(blog, temp, "id", "userId", "created");//, "status"
        blogService.saveOrUpdate(temp);

        return Result.succ(null);
    }

    @PostMapping("/delete")
    public Result delete(@Validated @RequestBody Integer[] ids) {

        System.out.println(ids);
        for (Integer id:ids){
            blogService.removeById(id);
        }

        return Result.succ(null);
    }

}
