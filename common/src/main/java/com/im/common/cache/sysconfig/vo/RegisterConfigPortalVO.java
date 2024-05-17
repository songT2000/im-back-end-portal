package com.im.common.cache.sysconfig.vo;

import com.im.common.cache.sysconfig.bo.BaseSysConfigBO;
import com.im.common.cache.sysconfig.bo.RegisterConfigBO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.beans.BeanCopier;

/**
 * 注册配置
 *
 * @author Barry
 * @date 2021-01-18
 */
@Data
@NoArgsConstructor
@ApiModel
public class RegisterConfigPortalVO extends BaseSysConfigBO {
    private static final BeanCopier BEAN_COPIER = BeanCopier.create(RegisterConfigBO.class, RegisterConfigPortalVO.class, false);

    public RegisterConfigPortalVO(RegisterConfigBO config) {
        BEAN_COPIER.copy(config, this, null);
    }

    @ApiModelProperty(value = "注册是否启用，关闭后所有前台注册入口关闭", position = 1)
    private Boolean enabled;

    @ApiModelProperty(value = "首页注册是否启用，开启后在登录页面会显示注册按钮", position = 2)
    private Boolean homeEnabled;

    @ApiModelProperty(value = "是否必填邀请码，关闭后不会显示邀请码输入框", position = 3)
    private Boolean inviteCodeRequired;

    @ApiModelProperty(value = "是否必填手机，关闭后不会显示手机输入框", position = 4)
    private Boolean mobileRequired;

    @ApiModelProperty(value = "是否必填手机验证码，关闭后不会显示手机验证码输入框", position = 5)
    private Boolean mobileVerificationCodeRequired;

    @ApiModelProperty(value = "是否必填资金密码，关闭后不会显示资金密码输入框", position = 6)
    private Boolean fundPwdRequired;

    @ApiModelProperty(value = "是否必选头像，关闭也可以选填", position = 7)
    private Boolean avatarRequired;
}
