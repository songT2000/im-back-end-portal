package com.im.common.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 人工提现
 *
 * @author Barry
 * @date 2021-03-24
 */
@Data
@NoArgsConstructor
@ApiModel
public class WithdrawOrderAdminAddParam {
    @NotBlank
    @ApiModelProperty(value = "用户名", required = true, position = 1)
    private String username;

    @NotNull
    @DecimalMin(value = "0", message = "Minimum 0")
    @ApiModelProperty(value = "金额，不可以负数", required = true, position = 2)
    private BigDecimal amount;

    @ApiModelProperty(value = "备注", position = 3)
    private String remark;
}
