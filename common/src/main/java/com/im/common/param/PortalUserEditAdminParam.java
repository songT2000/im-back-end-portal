package com.im.common.param;

import com.im.common.entity.enums.SexEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Set;

/**
 * 编辑用户
 *
 * @author Barry
 * @date 2020-05-26
 */
@Data
@NoArgsConstructor
@ApiModel
public class PortalUserEditAdminParam {
    @NotBlank
    @ApiModelProperty(value = "用户名", required = true, position = 1)
    private String username;

    @ApiModelProperty(value = "昵称", position = 2)
    private String nickname;

    @ApiModelProperty(value = "头像", position = 3)
    private String avatar;

    @ApiModelProperty(value = "用户组ID列表", position = 4)
    private Set<Long> userGroupIds;

    @ApiModelProperty(value = "登录密码，2次MD5，不为空就修改", position = 5)
    private String loginPwd;

    @NotNull
    @ApiModelProperty(value = "登录权限", required = true, position = 6)
    private Boolean loginEnabled;

    @NotNull
    @ApiModelProperty(value = "充值权限", required = true, position = 7)
    private Boolean rechargeEnabled;

    @NotNull
    @ApiModelProperty(value = "提现权限", required = true, position = 8)
    private Boolean withdrawEnabled;

    @NotNull
    @ApiModelProperty(value = "添加好友权限", required = true, position = 9)
    private Boolean addFriendEnabled;

    @NotNull
    @ApiModelProperty(value = "可用权限", required = true, position = 10)
    private Boolean enabled;

    @ApiModelProperty(value = "性别", position = 11)
    private SexEnum sex;

    @ApiModelProperty(value = "生日，yyyy-MM-dd", position = 12)
    private LocalDate birthday;

    @ApiModelProperty(value = "个性签名", position = 13)
    private String selfSignature;

    @ApiModelProperty(value = "备注", position = 14)
    private String adminRemark;
}
