package com.im.common.entity;

import com.im.common.entity.base.BaseEnableEntity;
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
public class PortalNavigatorStatistic {

    /**
     * 点击次数
     **/
    private Long clickCount;

    /**
     * 前台导航ID
     */
    private Long portalNavigatorId;
}
