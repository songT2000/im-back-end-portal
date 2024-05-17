package com.im.common.param;

import com.alibaba.fastjson.annotation.JSONField;
import com.im.common.constant.RegexConstant;
import com.im.common.entity.enums.DeviceTypeEnum;
import com.im.common.entity.enums.SexEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

/**
 * 用户注册
 *
 * @author Barry
 * @date 2019-12-18
 */
@Data
@NotNull
@ApiModel
public class PortalUserRegisterParam {
    @NotBlank
    @Length(min = 1, max = 128)
    @Pattern(regexp = RegexConstant.USERNAME_REGEX, message = RegexConstant.USERNAME_REGEX_REMARK)
    @ApiModelProperty(value = "[步骤一]用户名，分2个步骤的情况下，可以先调用[前台用户相关接口-判断用户名是否存在]来验证用户名是否已经被使用", required = true, position = 1)
    private String username;

    @NotBlank
    @Length(min = 1, max = 128)
    @ApiModelProperty(value = "[步骤一]登录密码，2次MD5", required = true, position = 2)
    private String loginPwd;

    @ApiModelProperty(value = "[步骤一]邀请码，注册配置inviteCodeRequired等于true显示且必填，=false不显示", position = 3)
    private String inviteCode;

    @ApiModelProperty(value = "[步骤一]手机国家，注册配置mobileRequired等于true显示且必填，=false不显示", position = 4)
    private String mobileCountryCode;

    @ApiModelProperty(value = "[步骤一]手机，注册配置mobileRequired等于true显示且必填，=false不显示", position = 5)
    private String mobile;

    @ApiModelProperty(value = "[步骤一]手机验证码(6位数字)，注册配置mobileVerificationCodeRequired等于true显示且必填，=false不显示，如果要输入，则要给个按钮获取验证码", position = 6)
    private String mobileVerificationCode;

    @ApiModelProperty(value = "[步骤二]资金密码，2次MD5，注册配置fundPwdRequired等于true显示且必填，=false不显示", position = 7)
    private String fundPwd;

    @ApiModelProperty(value = "[步骤二]头像，非必填，先调用[文件上传相关接口-上传头像]接口，再把接口返回的路径传这里", position = 8)
    private String avatar;

    /**
     * 我这里昵称暂时不必填，兼容其它版本
     */
    @ApiModelProperty(value = "[步骤二]昵称，必填", required = true, position = 9)
    private String nickname;

    @ApiModelProperty(value = "[步骤二]性别，非必填", position = 10)
    private SexEnum sex;

    @ApiModelProperty(value = "[步骤二]生日，非必填，yyyy-MM-dd", position = 11)
    private String birthday;

    @NotNull
    @ApiModelProperty(value = "注册设备", required = true, position = 12)
    private DeviceTypeEnum registerDevice;

    @ApiModelProperty(value = "设备唯一标识符", position = 13)
    private String deviceUnique;

    @JSONField(deserialize = false)
    @ApiModelProperty(hidden = true)
    private String ip;

    @JSONField(deserialize = false)
    @ApiModelProperty(hidden = true)
    private String url;
}
