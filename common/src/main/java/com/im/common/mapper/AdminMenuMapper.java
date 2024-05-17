package com.im.common.mapper;

import com.im.common.entity.AdminMenu;
import com.im.common.util.mybatis.mapper.MyBatisPlusMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 系统菜单和权限表 Mapper 接口
 *
 * @author Barry
 * @date 2019-11-06
 */
@Repository
public interface AdminMenuMapper extends MyBatisPlusMapper<AdminMenu> {
    /**
     * 根据用户ID查找对应菜单权限列表
     *
     * @param adminId 管理员用户ID
     * @return 菜单权限列表
     */
    List<AdminMenu> listUserMenus(long adminId);

    /**
     * 根据角色ID查找对应菜单权限列表
     *
     * @param roleId 角色ID
     * @return 菜单权限列表
     */
    List<AdminMenu> listRoleMenus(long roleId);

    /**
     * 批量修改排序号
     *
     * @param parentId  父ID
     * @param startSort 起始排序号
     * @param endSort   结束排序号
     * @param addSort   加减值
     */
    void updateSort(@Param("parentId") long parentId, @Param("startSort") int startSort, @Param("endSort") int endSort, @Param("addSort") int addSort);
}
