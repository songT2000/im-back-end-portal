package com.im.common.util.api.pay.base.withdraw;

import com.im.common.entity.BankCardWithdrawConfig;
import com.im.common.entity.enums.WithdrawConfigGroupEnum;
import com.im.common.entity.enums.WithdrawConfigSourceEnum;
import com.im.common.util.NumberUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 提现配置前台VO
 *
 * @author Barry
 * @date 2021-03-21
 */
@Data
@NoArgsConstructor
@ApiModel
public class WithdrawConfigGroupConfigPortalVO {
    public WithdrawConfigGroupConfigPortalVO(BankCardWithdrawConfig config) {
        this.group = WithdrawConfigGroupEnum.BANK_CARD;
        this.configSource = WithdrawConfigSourceEnum.BANK_CARD_WITHDRAW_CONFIG;

        this.configId = config.getId();
        this.name = config.getName();
        this.amountRange = config.getAmountRange();
        this.amountMaxPrecision = config.getAmountMaxPrecision();
        this.serviceChargePercent = config.getServiceChargePercent();
        this.serviceChargePercentStr = NumberUtil.pointToStr(config.getServiceChargePercent());

        this.needChooseBankCard = true;
    }

    @ApiModelProperty(value = "分组", position = 1)
    private WithdrawConfigGroupEnum group;

    @ApiModelProperty(value = "配置来源，返回什么请求充值就传什么", position = 2)
    private WithdrawConfigSourceEnum configSource;

    @ApiModelProperty(value = "配置ID", position = 3)
    private Long configId;

    @ApiModelProperty(value = "名称", position = 4)
    private String name;

    @ApiModelProperty(value = "金额范围，100~30000为任意值(展示输入框，并展示快捷金额按钮)，100,200为选项值(展示快捷金额按钮，没有输入框)", position = 8)
    private String amountRange;

    @ApiModelProperty(value = "金额最多小数位", position = 9)
    private Integer amountMaxPrecision;

    @ApiModelProperty(value = "手续费百分比，0.01就是%1，0就是不收取手续费", position = 10)
    private BigDecimal serviceChargePercent;

    @ApiModelProperty(value = "手续费百分比，0.01就是%1，0就是不收取手续费", position = 11)
    private String serviceChargePercentStr;

    @ApiModelProperty(value = "是否需要选择银行卡", position = 12)
    private Boolean needChooseBankCard;
}
