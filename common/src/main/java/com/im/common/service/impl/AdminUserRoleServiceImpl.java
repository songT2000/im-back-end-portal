package com.im.common.service.impl;

import com.im.common.entity.AdminUserRole;
import com.im.common.mapper.AdminUserRoleMapper;
import com.im.common.service.AdminUserRoleService;
import com.im.common.util.CollectionUtil;
import com.im.common.util.mybatis.service.MyBatisPlusServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

/**
 * 用户角色表 服务实现类
 *
 * @author Barry
 * @date 2019-11-06
 */
@Service
public class AdminUserRoleServiceImpl
        extends MyBatisPlusServiceImpl<AdminUserRoleMapper, AdminUserRole>
        implements AdminUserRoleService {
    @Override
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public List<AdminUserRole> listUserRoles(List<Long> userIds) {
        return lambdaQuery()
                .in(AdminUserRole::getAdminId, userIds)
                .list();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean adjustUserRoles(long adminId, Collection<Long> roleIds) {
        // 先删除用户的所有角色
        lambdaUpdate().eq(AdminUserRole::getAdminId, adminId).remove();

        if (CollectionUtil.isEmpty(roleIds)) {
            return true;
        }

        // 去重
        List<Long> distinctRoleIds = CollectionUtil.distinct(roleIds);

        // 再添加角色
        List<AdminUserRole> adminUserRoles = CollectionUtil.toList(distinctRoleIds, distinctRoleId -> new AdminUserRole(adminId, distinctRoleId));

        return saveBatch(adminUserRoles);
    }
}
