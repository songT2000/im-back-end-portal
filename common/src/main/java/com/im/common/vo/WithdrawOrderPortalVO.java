package com.im.common.vo;

import com.im.common.entity.WithdrawOrder;
import com.im.common.entity.enums.WithdrawOrderStatusEnum;
import com.im.common.util.NumberUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.beans.BeanCopier;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 兑换提现订单前台VO
 *
 * @author Barry
 * @date 2021-06-26
 */
@Data
@NoArgsConstructor
@ApiModel
public class WithdrawOrderPortalVO {
    public static final BeanCopier BEAN_COPIER = BeanCopier.create(WithdrawOrder.class, WithdrawOrderPortalVO.class, false);

    public WithdrawOrderPortalVO(WithdrawOrder e) {
        BEAN_COPIER.copy(e, this, null);
        this.serviceChargePercentStr = NumberUtil.pointToStr(this.serviceChargePercent);
    }

    @ApiModelProperty(value = "ID", position = 1)
    private Long id;

    @ApiModelProperty(value = "订单号", position = 2)
    private String orderNum;

    @ApiModelProperty(value = "提单金额", position = 4)
    private BigDecimal requestAmount;

    @ApiModelProperty(value = "到账金额", position = 5)
    private BigDecimal receiveAmount;

    @ApiModelProperty(value = "手续费比例", position = 6)
    private BigDecimal serviceChargePercent;

    @ApiModelProperty(value = "手续费比例", position = 7)
    private String serviceChargePercentStr;

    @ApiModelProperty(value = "手续费", position = 8)
    private BigDecimal serviceCharge;

    @ApiModelProperty(value = "状态", position = 9)
    private WithdrawOrderStatusEnum status;

    @ApiModelProperty(value = "时间", position = 10)
    private LocalDateTime createTime;

    @ApiModelProperty(value = "备注", position = 11)
    private String remark;
}
