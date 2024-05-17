package com.im.common.mapper;

import com.im.common.entity.AdminRole;
import com.im.common.util.mybatis.mapper.MyBatisPlusMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 系统角色表 Mapper 接口
 *
 * @author Barry
 * @date 2019-11-06
 */
@Repository
public interface AdminRoleMapper extends MyBatisPlusMapper<AdminRole> {
    /**
     * 根据用户ID查找对应角色列表
     *
     * @param adminId 管理员用户ID
     * @param enabled 有值生效，无值无效
     * @return 角色列表
     */
    List<AdminRole> listUserRoles(@Param("adminId") long adminId, @Param("enabled") Boolean enabled);
}
