package com.im.common.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;

/**
 * 银行卡充值配置添加参数
 *
 * @author max.stark
 */
@Data
@NoArgsConstructor
@ApiModel
public class BankCardRechargeConfigAddParam {
    @NotBlank
    @ApiModelProperty(value = "配置名称", required = true, position = 1)
    private String name;

    @NotBlank
    @ApiModelProperty(value = "金额范围,100~30000为任意值，100,200为选项值，100为最小值且无上限", required = true, position = 2)
    private String amountRange;

    @NotNull
    @ApiModelProperty(value = "金额最多小数位", required = true, position = 3)
    private Integer amountMaxPrecision;

    @NotNull
    @ApiModelProperty(value = "手续费百分比，0.01就是%1，手续费将从到账金额中扣除", required = true, position = 4)
    private BigDecimal serviceChargePercent;

    @NotNull
    @ApiModelProperty(value = "是否启用", required = true, position = 5)
    private Boolean enabled;

    @ApiModelProperty(value = "启用时间段，格式HH:mm:ss~HH:mm:ss", position = 6)
    private String enableTime;

    @NotNull
    @ApiModelProperty(value = "是否需要输入付款人", required = true, position = 9)
    private Boolean needInputUserCardName;

    @NotNull
    @ApiModelProperty(value = "排序", required = true, position = 10)
    private Integer sort;

    @ApiModelProperty(value = "谷歌验证码，6位数字，通过/api/{portal/agent/admin}/user/has-bound-google来获取用户是否绑定了谷歌", position = 11)
    private Integer googleCode;

    @NotNull
    @Size(min = 1)
    @ApiModelProperty(value = "银行卡配置", required = true, position = 12)
    private List<ConfigCardParam> cardList;

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class ConfigCardParam {
        @ApiModelProperty(value = "id", required = true, position = 1)
        private Long id;

        @NotNull
        @ApiModelProperty(value = "银行ID", required = true, position = 2)
        private Long bankId;

        @NotNull
        @ApiModelProperty(value = "卡姓名", required = true, position = 3)
        private String cardName;

        @NotNull
        @ApiModelProperty(value = "卡号", required = true, position = 4)
        private String cardNum;

        @ApiModelProperty(value = "支行", position = 5)
        private String cardBranch;

        @NotNull
        @ApiModelProperty(value = "是否启用", required = true, position = 6)
        private Boolean enabled;
    }
}
