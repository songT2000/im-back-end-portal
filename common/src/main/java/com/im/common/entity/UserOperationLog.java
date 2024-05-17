package com.im.common.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.im.common.entity.base.BaseEntity;
import com.im.common.entity.enums.DeviceTypeEnum;
import com.im.common.entity.enums.PortalTypeEnum;
import com.im.common.entity.enums.UserOperationLogTypeEnum;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 用户操作日志
 *
 * @author Barry
 * @date 2020-05-25
 */
@Data
@NoArgsConstructor
public class UserOperationLog extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 5108487428177522231L;

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
     * 操作类型
     */
    private UserOperationLogTypeEnum operationType;

    /**
     * IP
     **/
    private String ip;

    /**
     * 区域
     **/
    private String area;

    /**
     * 设备标识，浏览器就是UserAgent，原生设备就是设备ID
     **/
    private String deviceId;

    /**
     * 设备类型
     */
    private DeviceTypeEnum deviceType;

    /**
     * 请求参数
     **/
    private String requestParam;
}
