package com.im.common.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.im.common.entity.base.BaseEntity;
import com.im.common.entity.enums.DeviceTypeEnum;
import com.im.common.entity.enums.PortalTypeEnum;
import com.im.common.entity.enums.UserLoginTypeEnum;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 用户登录日志
 *
 * @author Barry
 * @date 2019-11-06
 */
@Data
@NoArgsConstructor
public class UserLoginLog extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 2268633216749579143L;

    /**
     * 分布式ID
     **/
    @TableId
    private Long id;

    /**
     * 各种类型的用户ID
     **/
    private Long userId;

    /**
     * 门户类型
     */
    private PortalTypeEnum portalType;

    /**
     * 登录IP
     **/
    private String ip;

    /**
     * 登录区域
     **/
    private String area;

    /**
     * 登录设备标识
     **/
    private String deviceId;

    /**
     * 登录设备类型
     */
    private DeviceTypeEnum deviceType;

    /**
     * 登录方式
     */
    private UserLoginTypeEnum loginType;

    /**
     * 登录URL
     **/
    private String url;
}
