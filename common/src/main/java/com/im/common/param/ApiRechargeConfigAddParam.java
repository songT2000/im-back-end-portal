package com.im.common.param;

import com.im.common.entity.enums.ApiRechargeConfigCodeEnum;
import com.im.common.entity.enums.RechargeConfigGroupEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 三方充值配置添加参数
 * @author max.stark
 */
@Data
@NoArgsConstructor
@ApiModel
public class ApiRechargeConfigAddParam {

    @NotNull
    @ApiModelProperty(value = "分组，只是用来做前台展示归类，不影响逻辑", required = true, position = 1)
    private RechargeConfigGroupEnum group;

    @NotBlank
    @ApiModelProperty(value = "后台名称", required = true, position = 2)
    private String adminName;

    @NotBlank
    @ApiModelProperty(value = "前台名称", required = true, position = 3)
    private String portalName;

    @NotNull
    @ApiModelProperty(value = "编码，每种编码都有不同的处理类", required = true, position = 4)
    private ApiRechargeConfigCodeEnum code;

    @NotBlank
    @ApiModelProperty(value = "金额范围", required = true, position = 5)
    private String amountRange;

    @NotNull
    @ApiModelProperty(value = "金额最多小数位", required = true, position = 6)
    private Integer amountMaxPrecision;

    @ApiModelProperty(value = "启用时间段，格式HH:mm:ss~HH:mm:ss", required = true, position = 7)
    private String enableTime;

    @NotNull
    @ApiModelProperty(value = "是否启用", required = true, position = 7)
    private Boolean enabled;

    @NotNull
    @ApiModelProperty(value = "手续费百分比，0.01就是%1", required = true, position = 10)
    private BigDecimal serviceChargePercent;

    @NotNull
    @ApiModelProperty(value = "是否需要输入付款人，针对像卡转卡这种", required = true, position = 11)
    private Boolean needInputUserCardName;

    @ApiModelProperty(value = "三方配置，密钥，URL什么的，JSON格式", position = 12)
    private String thirdConfig;

    @ApiModelProperty(value = "三方回调IP白名单，多个用英文逗号分隔", position = 13)
    private String thirdCallbackWhitelistIp;

    @ApiModelProperty(value = "排序号，越小排越前面", required = true, position = 14)
    private Integer sort;

    @ApiModelProperty(value = "谷歌验证码，6位数字，通过/api/{portal/agent/admin}/user/has-bound-google来获取用户是否绑定了谷歌", position = 11)
    private Integer googleCode;
}
