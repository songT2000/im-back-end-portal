package com.im.common.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * 获取用户是否已经绑定谷歌
 *
 * @author Barry
 * @date 2019-11-11
 */
@Data
@NoArgsConstructor
@ApiModel
public class HasBoundGoogleParam {
    /**
     * 用户名
     **/
    @NotBlank(message = "RSP_MSG.USERNAME_REQUIRED#I18N")
    @Length(min = 1, max = 128)
    @ApiModelProperty(value = "用户名", required = true)
    private String username;
}
