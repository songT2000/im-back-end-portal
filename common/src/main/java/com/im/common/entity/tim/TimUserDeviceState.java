package com.im.common.entity.tim;

import com.im.common.entity.base.BaseEntity;
import com.im.common.entity.enums.ActionEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 用户设备上线状态
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TimUserDeviceState extends BaseEntity implements Serializable {
    private static final long serialVersionUID = -6469217865471201433L;
    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 用户上线或者下线的动作，Login 表示上线，Logout 表示下线，Disconnect 表示网络断开
     */
    private ActionEnum action;

    /**
     * 用户上下线触发的原因，取值有Login，Logout，Disconnect，Timeout
     */
    private String reason;

    /**
     * 用户客户端类型，可能的取值有"iOS", "Android", "Web", "Windows", "iPad", "Mac", "Linux"
     */
    private String deviceType;

    /**
     * 被踢下线的设备的平台类型，多个使用逗号分割
     */
    private String kickedDevice;

    /**
     * 客户端IP
     */
    private String clientIp;

}
