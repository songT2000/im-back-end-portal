package com.im.common.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 绑定提现姓名
 *
 * @author Barry
 * @date 2021-02-24
 */
@Data
@NotNull
@ApiModel
public class UserBindWithdrawNameParam {
    @NotBlank
    @ApiModelProperty(value = "资金密码，2次MD5", required = true, position = 1)
    private String fundPwd;

    @NotBlank
    @Length(min = 1, max = 128)
    @ApiModelProperty(value = "提现姓名，不要验证是否为中文什么的，接口会验证", required = true, position = 2)
    private String withdrawName;

    /**
     * 如果用户绑定了谷歌，并且系统开启了谷歌，则需要输入验证码
     */
    @ApiModelProperty(value = "谷歌验证码，6位数字，通过/api/{portal/agent/admin}/user/has-bound-google来获取用户是否绑定了谷歌", position = 3)
    private Integer googleCode;
}
