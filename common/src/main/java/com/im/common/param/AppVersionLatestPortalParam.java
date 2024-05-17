package com.im.common.param;

import com.im.common.entity.enums.AppTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 获取app最新版本参数
 */
@Data
@NoArgsConstructor
@ApiModel
public class AppVersionLatestPortalParam {

    @NotNull
    @ApiModelProperty(value = "应用类型,android或者iOS", required = true, position = 2)
    private AppTypeEnum appType;
}
