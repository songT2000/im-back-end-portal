package com.im.common.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 银行卡提现配置编辑参数
 *
 * @author max.stark
 */
@Data
@NoArgsConstructor
@ApiModel
public class BankCardWithdrawConfigEditParam {
    @NotNull
    @ApiModelProperty(value = "id", required = true, position = 1)
    private Long id;

    @NotBlank
    @ApiModelProperty(value = "名称", required = true, position = 1)
    private String name;

    @NotBlank
    @ApiModelProperty(value = "金额范围", required = true, position = 3)
    private String amountRange;

    @NotNull
    @ApiModelProperty(value = "金额最多小数位", required = true, position = 4)
    private Integer amountMaxPrecision;

    @NotNull
    @ApiModelProperty(value = "手续费百分比", required = true, position = 5)
    private BigDecimal serviceChargePercent;

    @NotNull
    @ApiModelProperty(value = "排序号", required = true, position = 7)
    private Integer sort;

    @NotNull
    @ApiModelProperty(value = "启用/禁用", required = true, position = 7)
    private Boolean enabled;

    @ApiModelProperty(value = "谷歌验证码，6位数字，通过/api/{portal/agent/admin}/user/has-bound-google来获取用户是否绑定了谷歌", position = 8)
    private Integer googleCode;
}
