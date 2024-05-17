package com.im.common.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

/**
 * 添加后台用户
 *
 * @author Barry
 * @date 2019-11-15
 */
@Data
@NoArgsConstructor
@ApiModel
public class AdminUserAddParam {
    @NotBlank(message = "RSP_MSG.USERNAME_REQUIRED#I18N")
    @Length(min = 1, max = 128)
    @ApiModelProperty(value = "用户名", required = true)
    private String username;

    @NotBlank(message = "RSP_MSG.PASSWORD_REQUIRED#I18N")
    @Length(min = 1, max = 128)
    @ApiModelProperty(value = "登录密码，2次MD5", required = true)
    private String loginPwd;

    @ApiModelProperty(value = "备注")
    private String remark;

    @NotNull
    @Size(min = 1)
    @ApiModelProperty(value = "角色ID列表", required = true)
    private Set<Long> roleIds;
}
