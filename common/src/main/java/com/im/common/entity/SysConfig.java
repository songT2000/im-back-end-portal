package com.im.common.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.im.common.entity.base.BaseEntity;
import com.im.common.entity.enums.SysConfigGroupEnum;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 系统配置表
 *
 * @author Barry
 * @date 2018/6/8
 */
@Data
@NoArgsConstructor
public class SysConfig extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 7607526955662350872L;

    /**
     * 配置组
     **/
    @TableField("`group`")
    private SysConfigGroupEnum group;

    /**
     * 配置项
     **/
    private String item;

    /**
     * 配置值
     **/
    @TableField("`value`")
    private String value;

    /**
     * 是否是高级配置
     **/
    @TableField("`is_advance`")
    private Boolean advance;

    /**
     * 是否开放编辑
     **/
    @TableField("`is_editable`")
    private Boolean editable;

    /**
     * 配置项名称
     */
    private String name;

    /**
     * 备注
     **/
    private String remark;
}
