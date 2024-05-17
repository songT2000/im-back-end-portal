package com.im.common.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 群组权限配置 启/禁
 */
@Data
@NoArgsConstructor
@ApiModel
public class TimGroupSettingParam {
    @NotBlank
    @ApiModelProperty(value = "群组ID", required = true, position = 1)
    private String groupId;

    @NotNull
    @ApiModelProperty(value = "true=启用，false=禁止", required = true, position = 2)
    private Boolean enable;
}
