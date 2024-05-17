package com.im.common.entity;

import com.im.common.entity.base.BaseEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 用户分组表
 *
 * @author Barry
 * @date 2022-03-12
 */
@Data
@NoArgsConstructor
public class UserGroup extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 3713724135348813153L;

    /**
     * 组名
     **/
    private String name;

    /**
     * 备注
     **/
    private String remark;
}
