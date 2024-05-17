package com.im.common.param;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * token登录参数
 *
 * @author Barry
 * @date 2019-10-12
 */
@Data
@NoArgsConstructor
public class TokenLoginParam extends BaseLoginParam {
    /**
     * token
     **/
    @NotBlank(message = "RSP_MSG.TOKEN_REQUIRED#I18N")
    private String token;
}
