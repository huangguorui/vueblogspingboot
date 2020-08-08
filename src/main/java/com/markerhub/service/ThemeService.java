package com.markerhub.service;

import com.markerhub.entity.RolePermission;
import com.markerhub.entity.Theme;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 关注公众号：MarkerHub
 * @since 2020-08-08
 */
public interface ThemeService extends IService<Theme> {
    List<Theme> selectByThemeId(int themeId);

}
