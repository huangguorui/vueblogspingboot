package com.markerhub.controller;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.markerhub.common.lang.Result;
import com.markerhub.entity.Custom;
import com.markerhub.entity.RolePermission;
import com.markerhub.entity.Tags;
import com.markerhub.service.CustomService;
import com.markerhub.service.TagsService;
import com.markerhub.shiro.ShiroUtil;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/custom")
public class CustomController {
    @Autowired
    CustomService customService ;

    @GetMapping("/list")
    public Result blogs(@RequestParam(defaultValue = "1") Integer currentPage, @RequestParam(defaultValue = "10") Integer size) {

        Page page =  new Page(currentPage, size);

        IPage pageData = customService.page(page, new QueryWrapper<Custom>().orderByDesc("id"));

        return Result.succ(pageData);
    }



    @PostMapping("/save")
    public Result edit(@Validated @RequestBody Custom custom ) {

        Custom temp = null;
        if(custom.getId() != null) {
            temp = customService.getById(custom.getId());
        } else {
            temp = new Custom();
            temp.setCreated(LocalDateTime.now());
            temp.setStatus(1);
        }
        BeanUtil.copyProperties(custom, temp, "id","created");
        customService.saveOrUpdate(temp);
        return Result.succ(null);

    }


    @PostMapping("/delete")
    public Result delete(@Validated @RequestBody Integer[] ids) {

        for (Integer id:ids){
            customService.removeById(id);
        }

        return Result.succ(null);
    }

}
