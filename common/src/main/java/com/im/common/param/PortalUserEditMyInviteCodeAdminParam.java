package com.im.common.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 编辑我的邀请码
 *
 * @author Barry
 * @date 2022-04-05
 */
@Data
@NotNull
@ApiModel
public class PortalUserEditMyInviteCodeAdminParam {
    @NotBlank
    @ApiModelProperty(value = "用户名", required = true, position = 1)
    private String username;

    @NotBlank
    @ApiModelProperty(value = "我的邀请码", required = true, position = 2)
    private String myInviteCode;
}
