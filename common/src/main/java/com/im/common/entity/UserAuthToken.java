package com.im.common.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.im.common.entity.base.BaseEntity;
import com.im.common.entity.enums.DeviceTypeEnum;
import com.im.common.entity.enums.PortalTypeEnum;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 前台登录token表
 *
 * @author Barry
 * @date 2019-10-11
 */
@Data
@NoArgsConstructor
public class UserAuthToken extends BaseEntity implements Serializable {
    private static final long serialVersionUID = -3974237614533003484L;

    /**
     * 分布式ID
     **/
    @TableId
    private Long id;

    /**
     * 用户ID
     **/
    private Long userId;

    /**
     * 门户类型
     */
    private PortalTypeEnum portalType;

    /**
     * token
     **/
    private String token;

    /**
     * 设备标识
     **/
    private String deviceId;

    /**
     * 设备类型
     **/
    private DeviceTypeEnum deviceType;

    /**
     * 过期时间
     **/
    private LocalDateTime expireTime;
}
