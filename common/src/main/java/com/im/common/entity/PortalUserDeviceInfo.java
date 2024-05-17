package com.im.common.entity;

import com.im.common.entity.base.BaseEntity;
import com.im.common.entity.enums.DeviceTypeEnum;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 用户设备信息表
 */
@Data
@NoArgsConstructor
public class PortalUserDeviceInfo extends BaseEntity implements Serializable {
    private static final long serialVersionUID = -8787993130973445707L;

    /**
     * 用户ID
     **/
    private Long userId;

    /**
     * 设备类型
     */
    private DeviceTypeEnum deviceType;

    /**
     * 手机厂商
     */
    private String deviceBrand;

    /**
     * 手机型号
     */
    private String systemModel;

    /**
     * 系统版本
     */
    private String systemVersion;

    /**
     * app版本
     */
    private String appVersion;

    /**
     * 设备唯一标识符
     */
    private String deviceUnique;
}
