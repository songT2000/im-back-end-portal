package com.im.common.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 充值订单补单
 *
 * @author Barry
 * @date 2021-03-24
 */
@Data
@NoArgsConstructor
@ApiModel
public class RechargeOrderPatchAdminParam {
    @NotNull
    @ApiModelProperty(value = "订单ID", required = true, position = 1)
    private Long id;

    @NotNull
    @ApiModelProperty(value = "实付", required = true, position = 2)
    @DecimalMin(value = "0", message = "最小0")
    private BigDecimal payAmount;

    @NotNull
    @ApiModelProperty(value = "手续费比例", required = true, position = 4)
    @DecimalMin(value = "0", message = "最小0")
    private BigDecimal serviceChargePercent;

    @ApiModelProperty(value = "备注，仅管理员可见", position = 5)
    private String remark;
}
