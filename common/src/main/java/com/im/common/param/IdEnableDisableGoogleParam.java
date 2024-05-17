package com.im.common.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * 启用/禁用
 *
 * @author Barry
 * @date 2019-11-15
 */
@Data
@NoArgsConstructor
@ApiModel
public class IdEnableDisableGoogleParam {
    @NotNull
    @ApiModelProperty(value = "ID", required = true)
    private Long id;

    @NotNull
    @ApiModelProperty(value = "启用/禁用", required = true)
    private Boolean enable;

    /**
     * 如果用户绑定了谷歌，并且系统开启了谷歌，则需要输入验证码
     */
    @ApiModelProperty(value = "谷歌验证码，6位数字，通过/api/{portal/agent/admin}/user/has-bound-google来获取用户是否绑定了谷歌", position = 3)
    private Integer googleCode;
}
