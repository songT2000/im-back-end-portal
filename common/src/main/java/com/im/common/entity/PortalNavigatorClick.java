package com.im.common.entity;

import com.im.common.entity.base.BaseEnableEntity;
import com.im.common.entity.base.BaseEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 前台导航点击数据
 *
 * @author mozzie
 * @date 2022-04-08
 */
@Data
@NoArgsConstructor
public class PortalNavigatorClick extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 7698519714114979619L;
    /**
     * 用户ID
     **/
    private Long userId;

    /**
     * 前台导航ID
     */
    private Long portalNavigatorId;
}
