package com.im.common.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author Barry
 * @date 2020-05-26
 */
@Data
@NoArgsConstructor
@ApiModel
public class UserAddBalanceAdminParam {
    @NotBlank
    @ApiModelProperty(value = "用户名", required = true, position = 1)
    private String username;

    @NotNull
    @ApiModelProperty(value = "金额，负数就减，正数就加", required = true, position = 2)
    private BigDecimal amount;

    @ApiModelProperty(value = "对应注单号，[列提示：充值填商户注单号，下发填系统注单号，没有就不用填]", position = 3)
    private String orderNum;

    @NotBlank
    @ApiModelProperty(value = "备注", required = true, position = 4)
    private String remark;

    /**
     * 如果用户绑定了谷歌，并且系统开启了谷歌，则需要输入验证码
     */
    @ApiModelProperty(value = "谷歌验证码，6位数字，通过/api/{portal/agent/admin}/user/has-bound-google来获取用户是否绑定了谷歌", position = 5)
    private Integer googleCode;
}
