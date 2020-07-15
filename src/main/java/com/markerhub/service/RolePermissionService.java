package com.markerhub.service;

import com.markerhub.entity.RolePermission;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 关注公众号：MarkerHub
 * @since 2020-07-14
 */
public interface RolePermissionService extends IService<RolePermission> {
     List<RolePermission> selectByRoleId(int roleId);
}
