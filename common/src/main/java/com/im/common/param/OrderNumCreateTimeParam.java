package com.im.common.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * @author Barry
 * @date 2020-05-26
 */
@Data
@NoArgsConstructor
@ApiModel
public class OrderNumCreateTimeParam {
    @NotBlank
    @ApiModelProperty(value = "订单号", required = true, position = 1)
    private String orderNum;

    @NotNull
    @ApiModelProperty(value = "订单号", required = true, position = 2)
    private LocalDateTime createTime;
}
