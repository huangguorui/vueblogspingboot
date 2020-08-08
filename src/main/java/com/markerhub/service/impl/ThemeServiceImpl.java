package com.markerhub.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.markerhub.entity.RolePermission;
import com.markerhub.entity.Theme;
import com.markerhub.mapper.ThemeMapper;
import com.markerhub.service.ThemeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 关注公众号：MarkerHub
 * @since 2020-08-08
 */
@Service
public class ThemeServiceImpl extends ServiceImpl<ThemeMapper, Theme> implements ThemeService {


    @Override
    public List<Theme> selectByThemeId(int themeId) {
        return list(new LambdaQueryWrapper<Theme>().eq(Theme::getId,themeId));

    }
}
