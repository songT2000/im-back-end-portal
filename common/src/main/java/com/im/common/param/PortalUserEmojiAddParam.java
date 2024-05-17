package com.im.common.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 新增用户自定义表情包
 */
@Data
@NoArgsConstructor
@ApiModel
public class PortalUserEmojiAddParam {
    @NotBlank
    @ApiModelProperty(value = "表情地址", required = true)
    private String url;
}
