package com.im.common.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

/**
 * 人工充值
 *
 * @author Barry
 * @date 2021-03-24
 */
@Data
@NoArgsConstructor
@ApiModel
public class RechargeOrderAdminAddParam {
    @NotBlank
    @ApiModelProperty(value = "用户名", required = true, position = 1)
    private String username;

    @ApiModelProperty(value = "金额，不可以负数", required = true, position = 2)
    private BigDecimal amount;

    @ApiModelProperty(value = "赠送金额，不可以负数，赠送金额会计入报表充值字段", position = 3)
    private BigDecimal giveAmount;

    @ApiModelProperty(value = "备注，仅管理员可见", position = 4)
    private String remark;
}
