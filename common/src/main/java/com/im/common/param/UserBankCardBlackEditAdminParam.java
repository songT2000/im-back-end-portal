package com.im.common.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 编辑银行卡黑名单
 *
 * @author Barry
 * @date 2020-06-08
 */
@Data
@NoArgsConstructor
@ApiModel
public class UserBankCardBlackEditAdminParam {
    @NotNull
    @ApiModelProperty(value = "数据ID", required = true, position = 1)
    private Long id;

    @ApiModelProperty(value = "卡姓名", position = 2)
    private String cardName;

    @NotBlank
    @ApiModelProperty(value = "卡号", required = true, position = 3)
    private String cardNum;

    @ApiModelProperty(value = "备注", position = 4)
    private String remark;
}
