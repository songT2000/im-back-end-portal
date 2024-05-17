package com.im.common.entity;

import com.im.common.entity.base.BaseEnableEntity;
import com.im.common.entity.base.BaseEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 用户表
 *
 * @author Barry
 * @date 2021-12-11
 */
@Data
@NoArgsConstructor
public class PortalUser extends BaseEnableEntity implements Serializable {
    private static final long serialVersionUID = -8787993130973445606L;

    /**
     * 用户名
     **/
    private String username;

    /**
     * 手机号国际区号
     **/
    private String mobilePrefix;

    /**
     * 手机
     **/
    private String mobile;

    /**
     * 昵称
     **/
    private String nickname;

    /**
     * 头像，为空就是系统默认头像
     **/
    private String avatar;

    /**
     * 提现姓名，一个账号只能绑定一个姓名
     */
    private String withdrawName;

    /**
     * 登录密码，MD5加密
     **/
    private String loginPwd;

    /**
     * 资金密码，MD5加密
     **/
    private String fundPwd;

    /**
     * 余额
     **/
    private BigDecimal balance;

    /**
     * 登录权限
     **/
    private Boolean loginEnabled;

    /**
     * 充值权限
     **/
    private Boolean rechargeEnabled;

    /**
     * 提现权限
     **/
    private Boolean withdrawEnabled;

    /**
     * 添加好友权限
     **/
    private Boolean addFriendEnabled;
}
