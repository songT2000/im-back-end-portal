package com.im.common.util.redis;

import com.im.common.entity.enums.DeviceTypeEnum;
import com.im.common.entity.enums.PortalTypeEnum;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>用户SessionUser，保存一些不经常变化的数据</p>
 *
 * <p>像用户的基础属性，如真实类型、用户类型等这些发生变化，应当踢出当前在线用户</p>
 *
 * @author Barry
 * @date 2020-05-23
 */
@Data
@NoArgsConstructor
public abstract class RedisSessionUser implements Serializable {
    /**
     * 用户ID
     **/
    protected Long id;

    /**
     * 门户类型
     */
    protected PortalTypeEnum portalType;

    /**
     * 用户名
     **/
    protected String username;

    /**
     * token
     **/
    protected String token;

    /**
     * 本次登录时间
     **/
    protected LocalDateTime loginTime;

    /**
     * 本次登录IP
     **/
    protected String loginIp;

    /**
     * 本次登录区域
     **/
    protected String loginArea;

    /**
     * 本次登录设备标识
     **/
    protected String loginDeviceId;

    /**
     * 本次登录设备来源
     **/
    protected DeviceTypeEnum loginDeviceType;
}
