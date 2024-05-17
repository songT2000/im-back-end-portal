package com.im.common.entity;

import com.im.common.entity.base.BaseEnableEntity;
import com.im.common.entity.enums.AdminMenuTypeEnum;
import com.im.common.util.mybatis.typehandler.i18n.I18nString;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 系统菜单和权限表
 *
 * @author Barry
 * @date 2019-11-06
 */
@Data
@NoArgsConstructor
public class AdminMenu extends BaseEnableEntity implements Serializable {
    private static final long serialVersionUID = 3868262390948851261L;

    /**
     * 权限编码
     **/
    private String code;

    /**
     * 菜单类型
     **/
    private AdminMenuTypeEnum type;

    /**
     * 上级菜单ID，0则没有
     **/
    private Long parentId;

    /**
     * 名称
     **/
    private I18nString name;

    /**
     * 菜单链接
     **/
    private String url;

    /**
     * 图标，仅类型=菜单时需要填写
     */
    private String icon;

    /**
     * 排序号
     */
    private Integer sort;
}
