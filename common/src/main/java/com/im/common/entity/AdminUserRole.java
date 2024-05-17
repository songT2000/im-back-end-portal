package com.im.common.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.im.common.entity.base.BaseEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 用户角色表
 *
 * @author Barry
 * @date 2019-11-06
 */
@Data
@NoArgsConstructor
public class AdminUserRole extends BaseEntity implements Serializable {
    private static final long serialVersionUID = -161883223961164621L;

    public AdminUserRole(Long adminId, Long roleId) {
        this.adminId = adminId;
        this.roleId = roleId;
    }

    /**
     * 分布式ID
     **/
    @TableId
    private Long id;

    /**
     * 管理用户ID
     **/
    private Long adminId;

    /**
     * 角色ID
     **/
    private Long roleId;
}
