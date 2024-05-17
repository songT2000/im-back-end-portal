package com.im.common.vo;

import com.im.common.entity.BankCardRechargeConfig;
import com.im.common.util.NumberUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.beans.BeanCopier;

import java.math.BigDecimal;
import java.util.List;

/**
 * 银行卡充值配置
 *
 * @author max.stark
 */
@Data
@NoArgsConstructor
@ApiModel
public class BankCardRechargeConfigAdminVO {

    public static final BeanCopier BEAN_COPIER = BeanCopier.create(BankCardRechargeConfig.class, BankCardRechargeConfigAdminVO.class, false);

    /**
     * @param e 不能为空
     */
    public BankCardRechargeConfigAdminVO(BankCardRechargeConfig e) {
        BEAN_COPIER.copy(e, this, null);

        this.serviceChargePercentStr = NumberUtil.pointToStr(this.serviceChargePercent);
    }

    @ApiModelProperty(value = "ID", position = 1)
    private Long id;

    @ApiModelProperty(value = "配置名称", position = 2)
    private String name;

    @ApiModelProperty(value = "金额范围,100~30000为任意值，100,200为选项值，100为最小值且无上限", position = 3)
    private String amountRange;

    @ApiModelProperty(value = "金额最多小数位", position = 4)
    private Integer amountMaxPrecision;

    @ApiModelProperty(value = "手续费百分比，0.01就是%1，手续费将从到账金额中扣除", position = 5)
    private BigDecimal serviceChargePercent;

    @ApiModelProperty(value = "手续费百分比，0.01就是%1，手续费将从到账金额中扣除", position = 6)
    private String serviceChargePercentStr;

    @ApiModelProperty(value = "是否启用", position = 7)
    private Boolean enabled;

    @ApiModelProperty(value = "启用时间段，格式HH:mm:ss~HH:mm:ss", position = 8)
    private String enableTime;

    @ApiModelProperty(value = "是否需要输入付款人", position = 9)
    private Boolean needInputUserCardName;

    @ApiModelProperty(value = "排序", position = 10)
    private Integer sort;

    @ApiModelProperty(value = "是否删除", position = 11)
    private Boolean deleted;

    @ApiModelProperty(value = "银行卡配置", position = 12)
    private List<BankCardRechargeConfigCardAdminVO> cardList;
}
