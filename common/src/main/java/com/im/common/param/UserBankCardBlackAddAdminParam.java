package com.im.common.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * 添加银行卡黑名单
 *
 * @author Barry
 * @date 2020-06-08
 */
@Data
@NoArgsConstructor
@ApiModel
public class UserBankCardBlackAddAdminParam {
    @ApiModelProperty(value = "卡姓名，[列提示：姓名仅作为标识，不作为程序判断依据]", position = 1)
    private String cardName;

    @NotBlank
    @ApiModelProperty(value = "卡号，[列提示：影响商户手动下发/商户API代付/代理提现", required = true, position = 2)
    private String cardNum;

    @ApiModelProperty(value = "备注", position = 3)
    private String remark;
}
