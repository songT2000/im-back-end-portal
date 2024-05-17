package com.im.common.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 添加银行卡
 *
 * @author Max
 * @date 2020-06-08
 */
@Data
@NoArgsConstructor
@ApiModel
public class UserBankCardAddAdminParam {
    @NotBlank
    @ApiModelProperty(value = "用户名", required = true, position = 1)
    private String username;

    @NotNull
    @ApiModelProperty(value = "银行ID，注意是拿[所有提现银行列表]接口数据", required = true, position = 2)
    private Long bankId;

    @NotBlank
    @Length(min = 10, max = 30, message = "银行卡位数为10～30位")
    @ApiModelProperty(value = "卡号", required = true, position = 3)
    private String cardNum;

    @ApiModelProperty(value = "支行名称", position = 4)
    private String branch;

    @ApiModelProperty(value = "是否启用", required = true, position = 5)
    private Boolean enabled;
}
