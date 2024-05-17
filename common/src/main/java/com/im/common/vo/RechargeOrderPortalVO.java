package com.im.common.vo;

import com.im.common.entity.RechargeOrder;
import com.im.common.entity.enums.RechargeOrderStatusEnum;
import com.im.common.util.NumberUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.beans.BeanCopier;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 充值订单前台VO
 *
 * @author Barry
 * @date 2021-06-26
 */
@Data
@NoArgsConstructor
@ApiModel
public class RechargeOrderPortalVO {
    public static final BeanCopier BEAN_COPIER = BeanCopier.create(RechargeOrder.class, RechargeOrderPortalVO.class, false);

    public RechargeOrderPortalVO(RechargeOrder e) {
        BEAN_COPIER.copy(e, this, null);
        this.serviceChargePercentStr = NumberUtil.pointToStr(this.serviceChargePercent);
    }

    @ApiModelProperty(value = "ID", position = 1)
    private Long id;

    @ApiModelProperty(value = "订单号", position = 2)
    private String orderNum;

    @ApiModelProperty(value = "提单金额，提交订单时发送的金额", position = 3)
    private BigDecimal requestAmount;

    @ApiModelProperty(value = "实付金额，经过确认后实际支付的金额", position = 4)
    private BigDecimal payAmount;

    @ApiModelProperty(value = "到账金额，扣除手续费后实际到达用户账户的金额", position = 5)
    private BigDecimal receiveAmount;

    @ApiModelProperty(value = "手续费比例", position = 6)
    private BigDecimal serviceChargePercent;

    @ApiModelProperty(value = "手续费比例", position = 7)
    private String serviceChargePercentStr;

    @ApiModelProperty(value = "手续费金额", position = 8)
    private BigDecimal serviceCharge;

    @ApiModelProperty(value = "状态", position = 9)
    private RechargeOrderStatusEnum status;

    @ApiModelProperty(value = "时间", position = 10)
    private LocalDateTime createTime;
}
