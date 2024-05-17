package com.im.common.param;

import com.im.common.entity.enums.WithdrawConfigSourceEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 提现请求
 *
 * @author Barry
 * @date 2020-05-26
 */
@Data
@NoArgsConstructor
@ApiModel
public class WithdrawRequestPortalParam {
    @NotNull
    @ApiModelProperty(value = "配置来源", required = true, position = 1)
    private WithdrawConfigSourceEnum configSource;

    @NotNull
    @ApiModelProperty(value = "配置ID", required = true, position = 2)
    private Long configId;

    @ApiModelProperty(value = "银行卡ID，根据configSource不同而不同，二选一，选银行卡必须实名认证", position = 3)
    private Long bankCardId;

    @NotNull
    @ApiModelProperty(value = "提现金额", required = true, position = 5)
    private BigDecimal amount;

    @NotBlank
    @ApiModelProperty(value = "资金密码，2次MD5", required = true, position = 6)
    private String fundPwd;

    /**
     * 如果用户绑定了谷歌，并且系统开启了谷歌，则需要输入验证码
     */
    @ApiModelProperty(value = "谷歌验证码，6位数字，通过/api/{seller/buyer/admin}/user/has-bound-google来获取用户是否绑定了谷歌", position = 7)
    private Integer googleCode;
}
