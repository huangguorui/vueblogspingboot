package com.markerhub.controller;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.markerhub.common.lang.Result;
import com.markerhub.entity.Tags;
import com.markerhub.entity.Theme;
import com.markerhub.service.TagsService;
import com.markerhub.service.ThemeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 关注公众号：MarkerHub
 * @since 2020-08-08
 */
@RestController
@RequestMapping("/theme")
public class ThemeController {
    @Autowired
    ThemeService themeService;

    @GetMapping("/list")
    public Result blogs(@RequestParam(defaultValue = "1") Integer currentPage, @RequestParam(defaultValue = "10") Integer size) {

        Page page =  new Page(currentPage, size);
        IPage pageData = themeService.page(page, new QueryWrapper<Theme>().orderByDesc("id"));

        return Result.succ(pageData);
    }



    @PostMapping("/save")
    public Result edit(@Validated @RequestBody Theme theme) {

        Theme temp = null;
        temp = new Theme();
        if(theme.getId() != null) {
            temp = themeService.getById(theme.getId());
        } else {
            temp = new Theme();
        }
        BeanUtil.copyProperties(theme, temp, "id");
        themeService.saveOrUpdate(temp);
        return Result.succ(null);


    }


    @PostMapping("/delete")
    public Result delete(@Validated @RequestBody Integer[] ids) {

        System.out.println(ids);
        for (Integer id:ids){
            themeService.removeById(id);
        }

        return Result.succ(null);
    }
}
