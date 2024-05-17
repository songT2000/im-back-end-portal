package com.im.common.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.im.common.entity.base.BaseEntity;
import com.im.common.entity.enums.DeviceTypeEnum;
import com.im.common.entity.enums.RegisterTypeEnum;
import com.im.common.entity.enums.SexEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 用户资料表
 *
 * @author Barry
 * @date 2021-12-11
 */
@Data
@NoArgsConstructor
public class PortalUserProfile extends BaseEntity implements Serializable {
    private static final long serialVersionUID = -469027850646876383L;
    /**
     * 是否内部账号
     **/
    @TableField("`is_internal_user`")
    protected Boolean internalUser;
    /**
     * 性别
     **/
    private SexEnum sex;
    /**
     * 生日，yyyy-MM-dd
     **/
    private LocalDate birthday;
    /**
     * 我的邀请码
     **/
    private String myInviteCode;
    /**
     * 个性签名
     **/
    private String selfSignature;
    /**
     * 上次登录时间
     **/
    private LocalDateTime lastLoginTime;
    /**
     * 上次登录IP
     **/
    private String lastLoginIp;
    /**
     * 上次登录区域
     **/
    private String lastLoginArea;
    /**
     * 注册设备
     **/
    private DeviceTypeEnum registerDevice;
    /**
     * 注册方式
     **/
    private RegisterTypeEnum registerType;
    /**
     * 注册时使用的邀请码
     **/
    private String registerInviteCode;
    /**
     * 首充时间
     **/
    private LocalDateTime firstRechargeTime;
    /**
     * 首充金额
     **/
    private BigDecimal firstRechargeAmount;
    /**
     * 累充金额
     **/
    @TableField(update = "%s+#{et.totalRechargeAmount}", updateStrategy = FieldStrategy.NOT_NULL)
    private BigDecimal totalRechargeAmount;
    /**
     * 首提时间
     **/
    private LocalDateTime firstWithdrawTime;
    /**
     * 首提金额
     **/
    private BigDecimal firstWithdrawAmount;
    /**
     * 累提金额
     **/
    @TableField(update = "%s+#{et.totalWithdrawAmount}", updateStrategy = FieldStrategy.NOT_NULL)
    private BigDecimal totalWithdrawAmount;
    /**
     * 管理员备注
     **/
    private String adminRemark;

    /**
     * 设备唯一标识符
     */
    private String deviceUnique;
}
