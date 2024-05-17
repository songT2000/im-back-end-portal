package com.im.common.param;

import com.im.common.entity.enums.AppTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 新增应用版本
 */
@Data
@NoArgsConstructor
@ApiModel
public class AppVersionAddParam {

    @NotNull
    @ApiModelProperty(value = "应用类型，iOS或者android", required = true, position = 2)
    private AppTypeEnum appType;

    @NotBlank
    @ApiModelProperty(value = "版本号，字符型", required = true, position = 3)
    private String versionName;

    @NotNull
    @ApiModelProperty(value = "版本数字代号，用于对比", required = true, position = 4)
    private Integer versionCode;

    @NotNull
    @ApiModelProperty(value = "是否强制升级", required = true, position = 5)
    private Boolean compulsory = Boolean.FALSE;

    @NotBlank
    @ApiModelProperty(value = "版本说明", required = true, position = 6)
    private String note;

    @NotBlank
    @ApiModelProperty(value = "下载地址", required = true, position = 7)
    private String downloadUrl;
}
