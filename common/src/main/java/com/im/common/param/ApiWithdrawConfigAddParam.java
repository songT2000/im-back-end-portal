package com.im.common.param;

import com.im.common.entity.enums.ApiWithdrawConfigCodeEnum;
import com.im.common.entity.enums.WithdrawConfigSourceEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 三方充值配置添加参数
 *
 * @author max.stark
 */
@Data
@NoArgsConstructor
@ApiModel
public class ApiWithdrawConfigAddParam {
    @NotBlank
    @ApiModelProperty(value = "名称", required = true, position = 1)
    private String name;

    @NotNull
    @ApiModelProperty(value = "编码，每种编码都有不同的处理类", required = true, position = 2)
    private ApiWithdrawConfigCodeEnum code;

    @NotNull
    @ApiModelProperty(value = "适用提现方式", required = true, position = 3)
    private WithdrawConfigSourceEnum withdrawConfigSource;

    @ApiModelProperty(value = "三方配置，密钥，URL什么的，JSON格式", required = true, position = 4)
    private String thirdConfig;

    @ApiModelProperty(value = "三方回调IP白名单", required = true, position = 5)
    private String thirdCallbackWhitelistIp;

    @ApiModelProperty(value = "排序号，越小排越前面", required = true, position = 6)
    private Integer sort;

    @NotNull
    @ApiModelProperty(value = "启用/禁用", required = true, position = 7)
    private Boolean enabled;

    @ApiModelProperty(value = "谷歌验证码，6位数字，通过/api/{portal/agent/admin}/user/has-bound-google来获取用户是否绑定了谷歌", position = 8)
    private Integer googleCode;
}
