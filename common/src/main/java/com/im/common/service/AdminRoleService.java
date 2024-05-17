package com.im.common.service;

import com.im.common.entity.AdminRole;
import com.im.common.param.AdminRoleAddParam;
import com.im.common.param.AdminRoleEditMenuParam;
import com.im.common.param.AdminRoleEditParam;
import com.im.common.param.IdEnableDisableParam;
import com.im.common.response.RestResponse;
import com.im.common.util.CollectionUtil;
import com.im.common.util.mybatis.service.MyBatisPlusService;
import com.im.common.vo.AdminMenuVO;
import com.im.common.vo.AdminSessionUser;
import lombok.NonNull;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 系统角色表 服务类
 *
 * @author Barry
 * @date 2019-11-06
 */
public interface AdminRoleService extends MyBatisPlusService<AdminRole> {
    /**
     * 判断角色是否是超级管理员角色（上级ID等于0就是超级管理员）
     *
     * @param adminRole 角色
     * @return boolean
     */
    boolean isAdminRole(AdminRole adminRole);

    /**
     * 判断角色中是否具有超级管理员角色（上级ID等于0就是超级管理员）
     *
     * @param adminRoles 角色列表
     * @return boolean
     */
    boolean hasAdminRole(List<AdminRole> adminRoles);

    /**
     * 列出用户自己的角色以及所有下级角色
     *
     * @param adminId 用户ID
     * @return
     */
    Collection<AdminRole> listUserRolesAndLowerRoles(long adminId);

    /**
     * 根据用户ID查找对应角色列表
     * 忽略enabled
     *
     * @param adminId 管理员用户ID
     * @return 角色列表
     */
    default List<AdminRole> listUserRoles(long adminId) {
        return listUserRoles(adminId, null);
    }

    /**
     * 根据用户ID查找对应角色列表
     *
     * @param adminId 管理员用户ID
     * @param enabled 有值生效，无值无效
     * @return 角色列表
     */
    List<AdminRole> listUserRoles(long adminId, Boolean enabled);

    /**
     * 根据用户ID查找对应角色列表
     * 忽略enabled字段
     *
     * @param adminIds 用户ID列表
     *                 * @return 角色列表
     * @return 角色列表，key=用户ID，value=角色列表
     */
    Map<Long, List<AdminRole>> listUserRoles(List<Long> adminIds);

    /**
     * 查询每个角色对应的所有下级角色列表
     * 忽略enabled字段
     *
     * @param roleIds 角色ID列表
     * @return 角色列表，已去重
     */
    List<AdminRole> listLowerRoles(List<Long> roleIds);

    /**
     * 检查指定的角色ID列表是否是admin的合法下级
     *
     * @param adminId      要检查的用户ID
     * @param checkRoleIds 要检查的角色ID列表
     * @return
     */
    default RestResponse isLowerRoleById(long adminId, long checkRoleIds) {
        return isLowerRolesById(adminId, CollectionUtil.toList(checkRoleIds));
    }

    /**
     * 检查指定的角色ID列表是否是admin的合法下级
     *
     * @param adminId      要检查的用户ID
     * @param checkRoleIds 要检查的角色ID列表
     * @return
     */
    RestResponse isLowerRolesById(long adminId, Collection<Long> checkRoleIds);

    /**
     * 检查指定的角色ID列表是否是admin的合法下级
     *
     * @param adminId    要检查的用户ID
     * @param checkRoles 要检查的角色列表
     * @return
     */
    RestResponse isLowerRoles(long adminId, Collection<AdminRole> checkRoles);

    /**
     * 是否是他的下级，如果lower没有角色，则认为是合法下级（防止异常数据）
     *
     * @param upperAdminId 上级ID
     * @param lowerAdminId 下级ID
     * @return
     */
    RestResponse isLowerUser(long upperAdminId, long lowerAdminId);

    /**
     * 判断某个角色是否具有某个角色，或者是他的下级角色
     *
     * @param adminId 用户ID
     * @param roleId  角色ID
     * @return
     */
    boolean isSelfRoleOrLowerRole(long adminId, long roleId);

    /**
     * 判断某个角色是否具有某个角色，或者是他的下级角色
     *
     * @param adminId 用户ID
     * @param role    角色
     * @return
     */
    boolean isSelfRoleOrLowerRole(long adminId, AdminRole role);

    /**
     * 检查角色名是否已经存在
     *
     * @param name  用户名
     * @param notId 不为空时，则不等于此ID
     * @return true：已存在；false：不存在
     */
    boolean isExists(String name, Long notId);

    /**
     * 新增下级角色，父角色必须是自己的，或者下级
     *
     * @param sessionUser 当前登录用户
     * @param param       参数，参数规范请参考param中注解
     * @return 返回OK表示成功
     */
    RestResponse add(AdminSessionUser sessionUser,
                     AdminRoleAddParam param);

    /**
     * 编辑下级角色，父角色必须是自己的，或者下级
     *
     * @param sessionUser 当前登录用户
     * @param param       参数，参数规范请参考param中注解
     * @return 返回OK表示成功
     */
    RestResponse edit(AdminSessionUser sessionUser,
                      AdminRoleEditParam param);

    /**
     * 启用/禁用下级角色，只能禁用下级，不能禁用自己的角色
     *
     * @param sessionUser 当前登录用户
     * @param param       参数
     * @return 返回OK表示成功
     */
    RestResponse enableDisable(AdminSessionUser sessionUser,
                               IdEnableDisableParam param);

    /**
     * 编辑下级角色权限
     *
     * @param sessionUser 当前登录用户
     * @param param       参数
     * @return 返回OK表示成功
     */
    RestResponse editMenu(AdminSessionUser sessionUser,
                          AdminRoleEditMenuParam param);

    /**
     * 列出角色的菜单权限列表
     *
     * @param sessionUser 当前登录用户
     * @param roleId      角色ID，必须是自己的角色或下级角色
     * @return List<AdminMenuVO>
     */
    List<AdminMenuVO> listSelfOrLowerMenu(AdminSessionUser sessionUser, long roleId);
}
