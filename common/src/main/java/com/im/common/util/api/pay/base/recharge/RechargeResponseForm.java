package com.im.common.util.api.pay.base.recharge;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @author Barry
 * @date 2021-03-23
 */
@Data
@NoArgsConstructor
@ApiModel
public class RechargeResponseForm {
    public RechargeResponseForm(String method, String action, Map<String, String> param) {
        this.method = method;
        this.action = action;
        this.param = param;
    }

    @ApiModelProperty(value = "form的method，post/get等", position = 1)
    private String method;

    @ApiModelProperty(value = "form的action", position = 2)
    private String action;

    @ApiModelProperty(value = "参数，MAP结构", position = 3)
    private Map<String, String> param;
}
