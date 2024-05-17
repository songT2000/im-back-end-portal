package com.im.common.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * 手动登录参数（用户输用户名和密码）
 *
 * @author Barry
 * @date 2019-10-12
 */
@Data
@NoArgsConstructor
@ApiModel
public class ManualLoginParam extends BaseLoginParam {
    @NotBlank
    @Length(min = 1, max = 128)
    @ApiModelProperty(value = "用户名", required = true, position = 1)
    private String username;

    @NotBlank
    @Length(min = 1, max = 128)
    @ApiModelProperty(value = "密码，两次MD5", required = true, position = 2)
    private String password;

    @ApiModelProperty(value = "谷歌验证码，6位数字，用户输完用户名后，通过/api/{portal/agent/admin}/user/has-bound-google来获取用户是否绑定了谷歌", position = 3, hidden = true)
    private Integer googleCode;
}
