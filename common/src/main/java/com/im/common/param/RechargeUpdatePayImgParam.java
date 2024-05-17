package com.im.common.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 充值修改支付凭证
 *
 * @author Barry
 * @date 2020-05-26
 */
@Data
@NoArgsConstructor
@ApiModel
public class RechargeUpdatePayImgParam {
    @NotNull
    @ApiModelProperty(value = "订单ID，在充值请求成功后会返回", required = true, position = 1)
    private Long id;

    @NotBlank
    @ApiModelProperty(value = "支付凭证地址", position = 2)
    private String payImg;
}
