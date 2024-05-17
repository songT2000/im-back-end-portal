package com.im.common.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * 新增系统默认头像
 *
 * @author Barry
 * @date 2022-04-07
 */
@Data
@NoArgsConstructor
@ApiModel
public class SysDefaultAvatarAddParam {
    @NotBlank
    @ApiModelProperty(value = "URL", required = true, position = 1)
    private String url;
}
