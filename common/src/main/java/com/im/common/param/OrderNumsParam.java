package com.im.common.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.validation.constraints.Size;
import java.util.Set;

/**
 * @author Barry
 * @date 2020-05-26
 */
@Data
@NoArgsConstructor
@ApiModel
public class OrderNumsParam {

    @NonNull
    @Size(min = 1)
    @ApiModelProperty(value = "订单号", required = true, position = 1)
    private Set<String> orderNums;
}
