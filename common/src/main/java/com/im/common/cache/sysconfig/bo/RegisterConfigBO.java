package com.im.common.cache.sysconfig.bo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 账户配置
 *
 * @author Max
 * @date 2021-01-18
 */
@Data
@NoArgsConstructor
public class RegisterConfigBO extends BaseSysConfigBO {
    /**
     * 注册是否启用，关闭后所有前台注册入口关闭
     */
    private Boolean enabled;

    /**
     * 首页注册是否启用，开启后在登录页面会显示注册按钮
     */
    private Boolean homeEnabled;

    /**
     * 是否必填邀请码，关闭后不会显示邀请码输入框
     */
    private Boolean inviteCodeRequired;

    /**
     * 是否必填手机，关闭后不会显示手机输入框
     */
    private Boolean mobileRequired;

    /**
     * 是否必填手机验证码，关闭后不会显示手机验证码输入框，请同时配置手机短信配置
     */
    private Boolean mobileVerificationCodeRequired;

    /**
     * 是否必填资金密码，关闭后不会显示资金密码输入框
     */
    private Boolean fundPwdRequired;

    /**
     * 每IP每天最大允许注册人数
     */
    private Integer dailyLimitCountByIp;

    /**
     * 前台自动生成邀请码长度，创建前台用户时，系统为其自动创建的我的邀请码的长度，小于等于0表示不自动生成邀请码
     */
    private Integer autoGenerateInviteCodeLength;

    /**
     * 是否允许加好友，注册成功后，是否允许用户加好友
     */
    private Boolean addFriendEnabled;

    /**
     * 是否必选头像，关闭也可以选填
     */
    private Boolean avatarRequired;
}
