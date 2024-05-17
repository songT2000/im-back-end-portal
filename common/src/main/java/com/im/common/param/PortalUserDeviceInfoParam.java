package com.im.common.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 设备信息参数
 */
@Data
@NoArgsConstructor
@ApiModel
public class PortalUserDeviceInfoParam {

    @ApiModelProperty(value = "手机厂商", position = 1)
    private String deviceBrand;

    @ApiModelProperty(value = "手机型号", position = 2)
    private String systemModel;

    @ApiModelProperty(value = "系统版本", position = 3)
    private String systemVersion;

    @ApiModelProperty(value = "app版本", position = 4)
    private String appVersion;

    @ApiModelProperty(value = "设备唯一标识符", position = 5)
    private String deviceUnique;
}
