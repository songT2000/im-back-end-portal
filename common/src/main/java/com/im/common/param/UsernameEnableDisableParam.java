package com.im.common.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 批量 启/禁
 *
 * @author Barry
 * @date 2020-05-26
 */
@Data
@NoArgsConstructor
@ApiModel
public class UsernameEnableDisableParam {
    @NotBlank(message = "RSP_MSG.USERNAME_REQUIRED#I18N")
    @Length(min = 1, max = 128)
    @ApiModelProperty(value = "用户名", required = true, position = 1)
    private String username;

    @NotNull
    @ApiModelProperty(value = "true=启用，false=禁用", required = true, position = 2)
    private Boolean enable;
}
