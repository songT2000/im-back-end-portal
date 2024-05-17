package com.im.common.service;

import com.im.common.entity.AdminMenu;
import com.im.common.param.AdminMenuEditParam;
import com.im.common.param.IdSortParam;
import com.im.common.param.IdEnableDisableParam;
import com.im.common.response.RestResponse;
import com.im.common.util.mybatis.service.MyBatisPlusService;
import lombok.NonNull;

import java.util.List;

/**
 * 系统菜单和权限表 服务类
 *
 * @author Barry
 * @date 2019-11-06
 */
public interface AdminMenuService extends MyBatisPlusService<AdminMenu> {
    /**
     * 根据用户ID查找对应菜单权限列表
     * 如果用户具有管理员角色,则返回所有数据库菜单记录
     *
     * @param adminId      管理员用户ID
     * @param hasAdminRole 用户角色中是否存在最高权限账户，有则返回所有权限数据（自动拥有最高权限）
     * @return 菜单权限列表
     */
    List<AdminMenu> listUserMenus(long adminId, boolean hasAdminRole);

    /**
     * 根据角色ID查找对应菜单权限列表
     *
     * @param roleId      角色ID
     * @param isAdminRole 是否是最高权限账户，有则返回所有权限数据（自动拥有最高权限）
     * @return 菜单权限列表
     */
    List<AdminMenu> listRoleMenus(long roleId, boolean isAdminRole);

    /**
     * 编辑菜单，没有数据权限
     *
     * @param param 参数
     * @return 返回OK表示成功
     */
    RestResponse edit(AdminMenuEditParam param);

    /**
     * 启用/禁用菜单，没有数据权限
     *
     * @param param 参数
     * @return 返回OK表示成功
     */
    RestResponse enableDisable(IdEnableDisableParam param);

    /**
     * 编辑菜单排序号，没有数据权限
     *
     * @param param 参数
     * @return 返回OK表示成功
     */
    RestResponse editSort(IdSortParam param);
}
