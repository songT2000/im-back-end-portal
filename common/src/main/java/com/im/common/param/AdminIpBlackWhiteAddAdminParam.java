package com.im.common.param;

import com.im.common.entity.enums.BlackWhiteTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 新增后台IP黑白名单
 *
 * @author Barry
 * @date 2021-04-20
 */
@Data
@NoArgsConstructor
@ApiModel
public class AdminIpBlackWhiteAddAdminParam {
    @NotBlank
    @ApiModelProperty(value = "IP", required = true, position = 1)
    private String ip;

    @NotNull
    @ApiModelProperty(value = "黑白名单", required = true, position = 2)
    private BlackWhiteTypeEnum blackWhite;

    @NotBlank
    @ApiModelProperty(value = "备注", position = 3)
    private String remark;

    /**
     * 如果用户绑定了谷歌，并且系统开启了谷歌，则需要输入验证码
     */
    @ApiModelProperty(value = "谷歌验证码，6位数字，通过/api/{seller/buyer/admin}/user/has-bound-google来获取用户是否绑定了谷歌", position = 4)
    private Integer googleCode;
}
