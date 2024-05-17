package com.im.common.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author Barry
 * @date 2020-05-26
 */
@Data
@NoArgsConstructor
@ApiModel
public class IdAmountParam {
    @NotNull
    @ApiModelProperty(value = "ID", required = true)
    private Long id;

    @NotNull
    @ApiModelProperty(value = "金额，正数加，负数减", required = true)
    private BigDecimal amount;
}
