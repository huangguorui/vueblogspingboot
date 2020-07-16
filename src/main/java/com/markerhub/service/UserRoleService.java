package com.markerhub.service;

import com.markerhub.entity.Role;
import com.markerhub.entity.RolePermission;
import com.markerhub.entity.UserRole;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 关注公众号：MarkerHub
 * @since 2020-07-16
 */
public interface UserRoleService extends IService<UserRole> {
    List<UserRole> selectByUserId(int userId);

}
