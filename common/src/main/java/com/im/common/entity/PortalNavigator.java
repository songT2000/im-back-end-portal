package com.im.common.entity;

import com.im.common.entity.base.BaseEnableEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 前台导航
 *
 * @author Barry
 * @date 2022-03-25
 */
@Data
@NoArgsConstructor
public class PortalNavigator extends BaseEnableEntity implements Serializable {
    private static final long serialVersionUID = 8410161023606757154L;

    /**
     * 名称
     **/
    private String name;

    /**
     * 链接
     */
    private String url;

    /**
     * 排序号
     */
    private Integer sort;
}
