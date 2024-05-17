package com.im.common.service;

import com.im.common.entity.AdminUserRole;
import com.im.common.util.mybatis.service.MyBatisPlusService;

import java.util.Collection;
import java.util.List;

/**
 * 用户角色表 服务类
 *
 * @author Barry
 * @date 2019-11-06
 */
public interface AdminUserRoleService extends MyBatisPlusService<AdminUserRole> {
    /**
     * 查询所有用户的所有角色，外部请用adminId字段进行区分
     *
     * @param userIds 用户ID列表
     * @return 用户角色列表
     */
    List<AdminUserRole> listUserRoles(List<Long> userIds);

    /**
     * 为管理员用户调整角色，先删除所有用户角色，然后按照新的角色列表进行添加
     * 同时会将roleIds去重
     *
     * @param adminId 管理员用户ID
     * @param roleIds 角色ID列表
     * @return boolean
     */
    boolean adjustUserRoles(long adminId, Collection<Long> roleIds);
}
