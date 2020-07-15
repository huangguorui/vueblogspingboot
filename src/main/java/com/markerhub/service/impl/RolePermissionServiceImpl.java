package com.markerhub.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.markerhub.entity.Permission;
import com.markerhub.entity.RolePermission;
import com.markerhub.mapper.RolePermissionMapper;
import com.markerhub.service.PermissionService;
import com.markerhub.service.RolePermissionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 关注公众号：MarkerHub
 * @since 2020-07-14
 */

@Service
public class RolePermissionServiceImpl extends ServiceImpl<RolePermissionMapper, RolePermission> implements RolePermissionService {

//    @Autowired
//    RolePermissionService rolePermissionService;
//    RolePermissionServiceImpl ddd=new RolePermissionServiceImpl();
    @Override
    public List<RolePermission> selectByRoleId(int roleId) {
          return list(new LambdaQueryWrapper<RolePermission>().eq(RolePermission::getRoleId,roleId));
    }



}
