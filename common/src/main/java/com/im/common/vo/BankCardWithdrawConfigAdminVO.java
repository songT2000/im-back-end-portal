package com.im.common.vo;

import com.im.common.entity.BankCardWithdrawConfig;
import com.im.common.util.NumberUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.beans.BeanCopier;

import java.math.BigDecimal;

/**
 * 银行卡提现配置VO
 *
 * @author max.stark
 */
@Data
@NoArgsConstructor
@ApiModel
public class BankCardWithdrawConfigAdminVO {
    public static final BeanCopier BEAN_COPIER = BeanCopier.create(BankCardWithdrawConfig.class, BankCardWithdrawConfigAdminVO.class, false);

    public BankCardWithdrawConfigAdminVO(BankCardWithdrawConfig e) {
        BEAN_COPIER.copy(e, this, null);

        this.serviceChargePercentStr = NumberUtil.pointToStr(e.getServiceChargePercent());
    }

    @ApiModelProperty(value = "id", position = 1)
    private Long id;

    @ApiModelProperty(value = "名称", position = 2)
    private String name;

    @ApiModelProperty(value = "金额范围", position = 3)
    private String amountRange;

    @ApiModelProperty(value = "金额最多小数位", position = 4)
    private Integer amountMaxPrecision;

    @ApiModelProperty(value = "手续费百分比", position = 5)
    private BigDecimal serviceChargePercent;

    @ApiModelProperty(value = "手续费百分比", position = 6)
    private String serviceChargePercentStr;

    @ApiModelProperty(value = "排序号", position = 7)
    private Integer sort;

    @ApiModelProperty(value = "启用/禁用", position = 8)
    private Boolean enabled;

    @ApiModelProperty(value = "删除", position = 9)
    private Boolean deleted;
}
