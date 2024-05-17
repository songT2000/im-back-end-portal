package com.im.common.service;

import com.im.common.entity.AdminUser;
import com.im.common.entity.enums.PortalTypeEnum;
import com.im.common.response.RestResponse;
import com.im.common.util.google.auth.GoogleBindVO;

/**
 * 谷歌身份验证器 服务类
 *
 * @author Barry
 * @date 2018/5/13
 */
public interface GoogleAuthService {
    /**
     * 生成Google密钥，并将密钥保存到用户表中，有可能返回空
     *
     * @param username   用户名,不能为空
     * @param portalType 门户类型
     * @return 返回对象中含有二维码图片和手动绑定验证码, 其中qrCode已Base64编码, 可直接显示在image的src中
     */
    GoogleBindVO createCredentials(String username, PortalTypeEnum portalType);

    /**
     * 验证用户google验证码，验证成功后60分钟相同验证码不允许再次使用
     *
     * @param username   用户名,不能为空
     * @param portalType 门户类型
     * @param googleCode 谷歌验证码
     * @return true表示成功，false表示失败
     */
    RestResponse authoriseGoogle(String username, PortalTypeEnum portalType, Integer googleCode);

    /**
     * 验证用户google验证码，验证成功后60分钟内相同验证码不允许再次使用
     *
     * @param user       用户
     * @param googleCode 谷歌验证码
     * @return true表示成功，false表示失败
     */
    RestResponse authoriseGoogle(AdminUser user, Integer googleCode);

    /**
     * 绑定谷歌身份验证器,确保先调用{@link #createCredentials}方法生成密钥
     *
     * @param username   用户名,为能为空
     * @param portalType 门户类型
     * @param googleCode 验证码
     * @return 返回处理状态, 对象中success=true表示成功; false则带有相应错误消息
     */
    RestResponse bindGoogle(String username, PortalTypeEnum portalType, Integer googleCode);

    /**
     * 绑定谷歌身份验证器,确保先调用{@link #createCredentials)}方法生成密钥
     *
     * @param user       用户
     * @param googleCode 验证码
     * @return 返回处理状态, 对象中success=true表示成功; false则带有相应错误消息
     */
    RestResponse bindGoogle(AdminUser user, Integer googleCode);
}
