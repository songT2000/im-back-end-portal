package com.im.common.param;

import com.im.common.util.ip.RequestIp;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 绑定谷歌
 *
 * @author Barry
 * @date 2019-11-11
 */
@Data
@NoArgsConstructor
@ApiModel
public class BindGoogleParam {
    /**
     * 用户名
     **/
    @NotBlank(message = "RSP_MSG.USERNAME_REQUIRED#I18N")
    @Length(min = 1, max = 128)
    @ApiModelProperty(value = "用户名", required = true, position = 1)
    private String username;

    /**
     * 密码
     **/
    @NotBlank(message = "RSP_MSG.PASSWORD_REQUIRED#I18N")
    @Length(min = 1, max = 128)
    @ApiModelProperty(value = "密码，两次MD5", required = true, position = 2)
    private String password;

    /**
     * 登录IP，后台注入，不需要传入
     **/
    @RequestIp
    @ApiModelProperty(hidden = true)
    private String ip;

    /**
     * 谷歌验证码
     */
    @NotNull(message = "RSP_MSG.GOOGLE_CODE_REQUIRED#I18N")
    @ApiModelProperty(value = "谷歌验证码，6位数字", position = 3)
    private Integer googleCode;
}
