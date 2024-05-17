package com.im.common.vo;

import com.im.common.entity.enums.DeviceTypeEnum;
import com.im.common.entity.enums.RegisterTypeEnum;
import com.im.common.entity.enums.SexEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 用户VO
 *
 * @author Barry
 * @date 2021-12-11
 */
@Data
@NoArgsConstructor
@ApiModel
public class PortalUserAdminVO {
    @ApiModelProperty(value = "ID", position = 1)
    private Long id;

    @ApiModelProperty(value = "用户名", position = 2)
    private String username;

    @ApiModelProperty(value = "手机国际区号", position = 3)
    private String mobilePrefix;

    @ApiModelProperty(value = "手机", position = 4)
    private String mobile;

    @ApiModelProperty(value = "昵称", position = 5)
    private String nickname;

    @ApiModelProperty(value = "头像", position = 6)
    private String avatar;

    @ApiModelProperty(value = "加入的用户组", position = 8)
    private List<UserGroupSimpleAdminVO> userGroups;

    @ApiModelProperty(value = "提现姓名", position = 9)
    private String withdrawName;

    @ApiModelProperty(value = "余额", position = 10)
    private BigDecimal balance;

    @ApiModelProperty(value = "登录权限", position = 11)
    private Boolean loginEnabled;

    @ApiModelProperty(value = "充值权限", position = 12)
    private Boolean rechargeEnabled;

    @ApiModelProperty(value = "提现权限", position = 13)
    private Boolean withdrawEnabled;

    @ApiModelProperty(value = "添加好友权限", position = 14)
    private Boolean addFriendEnabled;

    @ApiModelProperty(value = "资金密码", position = 15)
    private String fundPwd;

    @ApiModelProperty(value = "资金密码", position = 15)
    private Boolean fundPwdBound;

    @ApiModelProperty(value = "我的邀请码", position = 16)
    private String myInviteCode;

    @ApiModelProperty(value = "性别", position = 17)
    private SexEnum sex;

    @ApiModelProperty(value = "生日，yyyy-MM-dd", position = 18)
    private LocalDate birthday;

    @ApiModelProperty(value = "个性签名", position = 19)
    private String selfSignature;

    @ApiModelProperty(value = "上次登录时间", position = 20)
    private LocalDateTime lastLoginTime;

    @ApiModelProperty(value = "上次登录IP", position = 21)
    private String lastLoginIp;

    @ApiModelProperty(value = "上次登录区域", position = 22)
    private String lastLoginArea;

    @ApiModelProperty(value = "注册时间", position = 23)
    private LocalDateTime createTime;

    @ApiModelProperty(value = "注册设备", position = 24)
    private DeviceTypeEnum registerDevice;

    @ApiModelProperty(value = "注册方式", position = 25)
    private RegisterTypeEnum registerType;

    @ApiModelProperty(value = "注册时使用的邀请码", position = 26)
    private String registerInviteCode;

    @ApiModelProperty(value = "首充时间", position = 27)
    private LocalDateTime firstRechargeTime;

    @ApiModelProperty(value = "首充金额", position = 28)
    private BigDecimal firstRechargeAmount;

    @ApiModelProperty(value = "累充金额", position = 29)
    private BigDecimal totalRechargeAmount;

    @ApiModelProperty(value = "首提时间", position = 30)
    private LocalDateTime firstWithdrawTime;

    @ApiModelProperty(value = "首提金额", position = 31)
    private BigDecimal firstWithdrawAmount;

    @ApiModelProperty(value = "累提金额", position = 32)
    private BigDecimal totalWithdrawAmount;

    @ApiModelProperty(value = "管理员备注", position = 33)
    private String adminRemark;

    @ApiModelProperty(value = "启/禁状态", position = 34)
    private Boolean enabled;

    @ApiModelProperty(value = "内部用户", position = 35)
    private Boolean internalUser;

    @ApiModelProperty(value = "是否在线", position = 36)
    private Boolean online;

    public void patchEmpty() {
        this.userGroups = Optional.ofNullable(this.userGroups).orElse(new ArrayList<>());
    }
}
