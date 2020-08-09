package com.markerhub.controller;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.markerhub.common.lang.Result;
import com.markerhub.entity.Custom;
import com.markerhub.entity.Link;
import com.markerhub.entity.Permission;
import com.markerhub.service.CustomService;
import com.markerhub.service.LinkService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 关注公众号：MarkerHub
 * @since 2020-08-06
 */
@RestController
@RequestMapping("/link")
public class LinkController {
    @Autowired
    LinkService linkService  ;


    @GetMapping("/ulist")
    public Result ulist( String code) {
        int currentPage=1;
        int size=1;
        Page page =  new Page(currentPage, size);
        IPage pageData=null;
        System.out.println(code);
        if(code==null){
            code="AAA";
            pageData = linkService.page(page, new QueryWrapper<Link>().orderByDesc("id").eq("code",code));
        }else{
            pageData = linkService.page(page, new QueryWrapper<Link>().orderByDesc("id").eq("code",code));

        }

        return Result.succ(pageData);

    }


    @GetMapping("/list")
    public Result list(@RequestParam(defaultValue = "1") Integer currentPage, @RequestParam(defaultValue = "10") Integer size,String blogId , String code) {

        Page page =  new Page(currentPage, size);
        IPage pageData=null;
        System.out.println(blogId);
        System.out.println(code);

      
            if(code!=null){
                //eq精确匹配  like模糊匹配
                pageData = linkService.page(page, new QueryWrapper<Link>().orderByDesc("id").eq("code",code));
            }else{
                pageData = linkService.page(page, new QueryWrapper<Link>().orderByDesc("id"));

            }

        return Result.succ(pageData);

    }


    @RequiresAuthentication
    @PostMapping("/save")
    public Result edit(@Validated @RequestBody Link link  ) {

        Link temp = null;
        if(link.getId() != null) {
            temp = linkService.getById(link.getId());
        } else {
            temp = new Link();
        }
        BeanUtil.copyProperties(link, temp, "id");
        linkService.saveOrUpdate(temp);
        return Result.succ(null);

    }

    @RequiresAuthentication
    @PostMapping("/delete")
    public Result delete(@Validated @RequestBody Integer[] ids) {

        for (Integer id:ids){
            linkService.removeById(id);
        }

        return Result.succ(null);
    }
}
