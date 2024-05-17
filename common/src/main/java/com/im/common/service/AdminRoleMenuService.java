package com.im.common.service;

import com.im.common.entity.AdminRoleMenu;
import com.im.common.util.mybatis.service.MyBatisPlusService;

import java.util.Collection;

/**
 * 角色菜单表 服务类
 *
 * @author Barry
 * @date 2019-11-06
 */
public interface AdminRoleMenuService extends MyBatisPlusService<AdminRoleMenu> {
    /**
     * 重新调角色菜单，先删除所有角色菜单，然后按照新的权限列表进行添加
     * 同时会将menuIds去重
     *
     * @param roleId  角色ID
     * @param menuIds 菜单ID列表
     * @return boolean
     */
    boolean adjustRoleMenus(long roleId, Collection<Long> menuIds);
}
