package com.markerhub.controller;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.markerhub.common.lang.Result;
import com.markerhub.entity.Blog;
import com.markerhub.entity.Tags;
import com.markerhub.service.BlogService;
import com.markerhub.service.TagsService;
import com.markerhub.shiro.ShiroUtil;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 关注公众号：MarkerHub
 * @since 2020-08-05
 */
@RestController
@RequiresAuthentication
@RequestMapping("/tags")
public class TagsController {


    @Autowired
    TagsService tagsService;

    @GetMapping("/list")
    public Result blogs(@RequestParam(defaultValue = "1") Integer currentPage, @RequestParam(defaultValue = "10") Integer size) {

        Page page =  new Page(currentPage, size);
        IPage pageData = tagsService.page(page, new QueryWrapper<Tags>().orderByDesc("id"));

        return Result.succ(pageData);
    }



    @PostMapping("/save")
    public Result edit(@Validated @RequestBody Tags tags) {

        Tags temp = null;
         temp = new Tags();
        BeanUtil.copyProperties(tags, temp, "id");
        tagsService.saveOrUpdate(temp);

        return Result.succ(null);
    }


    @PostMapping("/delete")
    public Result delete(@Validated @RequestBody Integer[] ids) {

        System.out.println(ids);
        for (Integer id:ids){
            tagsService.removeById(id);
        }

        return Result.succ(null);
    }

}
