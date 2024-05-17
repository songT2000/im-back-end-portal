package com.im.common.util.api.pay.base.recharge;

import com.im.common.entity.ApiRechargeConfig;
import com.im.common.entity.BankCardRechargeConfig;
import com.im.common.entity.enums.RechargeConfigGroupEnum;
import com.im.common.entity.enums.RechargeConfigSourceEnum;
import com.im.common.util.NumberUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 充值配置前台VO
 *
 * @author Barry
 * @date 2021-03-21
 */
@Data
@NoArgsConstructor
@ApiModel
public class RechargeConfigGroupConfigPortalVO {
    public RechargeConfigGroupConfigPortalVO(BankCardRechargeConfig config) {
        this.group = RechargeConfigGroupEnum.BANK_CARD;
        this.configSource = RechargeConfigSourceEnum.BANK_CARD_RECHARGE_CONFIG;

        this.configId = config.getId();
        this.name = config.getName();
        this.amountRange = config.getAmountRange();
        this.amountMaxPrecision = config.getAmountMaxPrecision();
        this.serviceChargePercent = config.getServiceChargePercent();
        this.serviceChargePercentStr = NumberUtil.pointToStr(config.getServiceChargePercent());
        this.needInputUserCardName = config.getNeedInputUserCardName();
    }

    public RechargeConfigGroupConfigPortalVO(ApiRechargeConfig config) {
        this.group = config.getGroup();
        this.configSource = RechargeConfigSourceEnum.API_RECHARGE_CONFIG;

        this.configId = config.getId();
        this.name = config.getPortalName();
        this.amountRange = config.getAmountRange();
        this.amountMaxPrecision = config.getAmountMaxPrecision();
        this.serviceChargePercent = config.getServiceChargePercent();
        this.serviceChargePercentStr = NumberUtil.pointToStr(config.getServiceChargePercent());
        this.needInputUserCardName = config.getNeedInputUserCardName();
    }

    @ApiModelProperty(value = "分组", position = 1)
    private RechargeConfigGroupEnum group;

    @ApiModelProperty(value = "配置来源，返回什么请求充值就传什么", position = 2)
    private RechargeConfigSourceEnum configSource;

    @ApiModelProperty(value = "配置ID", position = 3)
    private Long configId;

    @ApiModelProperty(value = "名称", position = 4)
    private String name;

    @ApiModelProperty(value = "金额范围，100~30000为任意值(展示输入框，并展示快捷金额按钮)，100,200为选项值(展示快捷金额按钮，没有输入框)", position = 5)
    private String amountRange;

    @ApiModelProperty(value = "金额最多小数位", position = 6)
    private Integer amountMaxPrecision;

    @ApiModelProperty(value = "手续费百分比，0.01就是%1，0就是不收取手续费", position = 7)
    private BigDecimal serviceChargePercent;

    @ApiModelProperty(value = "手续费百分比，0.01就是%1，0就是不收取手续费", position = 8)
    private String serviceChargePercentStr;

    @ApiModelProperty(value = "是否需要输入付款人", position = 9)
    private Boolean needInputUserCardName;
}
