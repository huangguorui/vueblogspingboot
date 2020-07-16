package com.markerhub.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.markerhub.entity.Role;
import com.markerhub.entity.RolePermission;
import com.markerhub.entity.UserRole;
import com.markerhub.mapper.UserRoleMapper;
import com.markerhub.service.UserRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 关注公众号：MarkerHub
 * @since 2020-07-16
 */
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {


    @Override
    public List<UserRole> selectByUserId(int userId) {
        return list(new LambdaQueryWrapper<UserRole>().eq(UserRole::getUserId,userId));
    }
}
