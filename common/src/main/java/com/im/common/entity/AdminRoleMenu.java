package com.im.common.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.im.common.entity.base.BaseEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 角色菜单表
 *
 * @author Barry
 * @date 2019-11-06
 */
@Data
@NoArgsConstructor
public class AdminRoleMenu extends BaseEntity implements Serializable {
    private static final long serialVersionUID = -5421571264504504697L;

    public AdminRoleMenu(Long roleId, Long menuId) {
        this.roleId = roleId;
        this.menuId = menuId;
    }

    /**
     * 分布式ID
     **/
    @TableId
    private Long id;

    /**
     * 角色ID
     **/
    private Long roleId;

    /**
     * 菜单ID
     **/
    private Long menuId;
}
