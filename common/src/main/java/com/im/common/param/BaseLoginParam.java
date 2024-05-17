package com.im.common.param;

import com.im.common.entity.enums.DeviceTypeEnum;
import com.im.common.util.ip.RequestIp;
import com.im.common.util.url.RequestWebsite;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 自动登录参数
 *
 * @author Barry
 * @date 2019-10-12
 */
@Data
public abstract class BaseLoginParam {
    /**
     * 登录IP，不需要传
     **/
    @RequestIp
    @ApiModelProperty(hidden = true)
    private String ip;

    /**
     * 登录设备标识，原生传唯一设备ID，网页传UserAgent
     **/
    @NotBlank
    @ApiModelProperty(value = "登录设备标识，原生传唯一设备ID，网页传UserAgent", required = true, position = 5)
    private String deviceId;

    /**
     * 登录设备类型
     **/
    @NotNull
    @ApiModelProperty(value = "登录设备类型", required = true, position = 6)
    private DeviceTypeEnum deviceType;

    /**
     * 登录URL，不需要传
     **/
    @RequestWebsite
    @ApiModelProperty(hidden = true)
    private String url;
}
