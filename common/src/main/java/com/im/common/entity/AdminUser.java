package com.im.common.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.im.common.entity.base.BaseDeletableEnableEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 后台管理用户表
 *
 * @author Barry
 * @date 2019-11-06
 */
@Data
@NoArgsConstructor
public class AdminUser extends BaseDeletableEnableEntity implements Serializable {
    private static final long serialVersionUID = -5091605699079527324L;

    /**
     * 用户名
     **/
    private String username;

    /**
     * 登录密码，MD5加密
     **/
    private String loginPwd;

    /**
     * 上次登录时间
     **/
    private LocalDateTime lastLoginTime;

    /**
     * 上次登录IP
     */
    private String lastLoginIp;

    /**
     * 上次登录地址
     */
    private String lastLoginArea;

    /**
     * 登录密码输入错误次数
     */
    @TableField(update = "%s+1", updateStrategy = FieldStrategy.NOT_NULL)
    private Integer pwdErrorTimes;

    /**
     * 谷歌密钥
     */
    private String googleKey;

    /**
     * 是否绑定谷歌
     **/
    @TableField("is_google_bound")
    private Boolean googleBound;

    /**
     * 是否在线
     **/
    @TableField("is_online")
    private Boolean online;

    /**
     * 备注
     */
    private String remark;
}
