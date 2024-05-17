package com.im.common.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 用户修改密码
 *
 * @author Barry
 * @date 2019-12-18
 */
@Data
@NotNull
@ApiModel
public class UserEditPwdParam {
    @NotBlank(message = "RSP_MSG.PASSWORD_REQUIRED#I18N")
    @Length(min = 1, max = 128)
    @ApiModelProperty(value = "旧密码，2次MD5", required = true)
    private String oldPwd;

    @NotBlank(message = "RSP_MSG.PASSWORD_REQUIRED#I18N")
    @Length(min = 1, max = 128)
    @ApiModelProperty(value = "新密码，2次MD5", required = true)
    private String newPwd;
}
