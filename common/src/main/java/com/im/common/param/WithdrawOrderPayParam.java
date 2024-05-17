package com.im.common.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * 提现订单打款参数
 *
 * @author Barry
 * @date 2020-05-26
 */
@Data
@NoArgsConstructor
@ApiModel
public class WithdrawOrderPayParam {
    @NotNull
    @ApiModelProperty(value = "数据ID", required = true, position = 1)
    private Long id;

    @NotNull
    @ApiModelProperty(value = "true=通过，false=拒绝", required = true, position = 2)
    private Boolean approve;

    @ApiModelProperty(value = "approve=true时，可以选择一个API代付配置进行自动打款，approve=false时该字段无效", required = true, position = 3)
    private Long apiWithdrawConfigId;

    @ApiModelProperty(value = "备注", position = 4)
    private String remark;
}
