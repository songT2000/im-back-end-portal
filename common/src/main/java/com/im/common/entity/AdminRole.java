package com.im.common.entity;

import com.im.common.entity.base.BaseEnableEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 系统角色表
 *
 * @author Barry
 * @date 2019-11-06
 */
@Data
@NoArgsConstructor
public class AdminRole extends BaseEnableEntity implements Serializable, Comparable<AdminRole> {
    private static final long serialVersionUID = 9204802216761520691L;

    /**
     * 角色名称
     **/
    private String name;

    /**
     * 上级角色ID，0则没有，0表示拥有系统最高权限（自动拥有所有权限，不检查权限）
     **/
    private Long parentId;

    /**
     * 排序号
     */
    private Integer sort;

    /**
     * 名称
     **/
    private String remark;

    @Override
    public int compareTo(AdminRole o) {
        return Long.compare(this.getSort(), o.getSort());
    }
}
