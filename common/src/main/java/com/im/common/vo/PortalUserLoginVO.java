package com.im.common.vo;

import com.im.common.entity.PortalUser;
import com.im.common.entity.PortalUserProfile;
import com.im.common.util.MessageEncryptUtil;
import com.im.common.util.StrUtil;
import com.im.common.util.jwt.JwtUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.beans.BeanCopier;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * <p>前台用户当前登录信息</p>
 *
 * @author Barry
 * @date 2019-10-12
 */
@Data
@NoArgsConstructor
@ApiModel
public class PortalUserLoginVO {
    public static final BeanCopier BEAN_COPIER = BeanCopier.create(PortalUser.class, PortalUserLoginVO.class, false);

    public PortalUserLoginVO(PortalSessionUser sessionUser, PortalUser user, PortalUserProfile profile) {
        BEAN_COPIER.copy(user, this, null);
        this.token = sessionUser.getToken();
        this.userSig = sessionUser.getUserSig();
        this.tecentImSdkAppid = sessionUser.getTecentImSdkAppid();

        if (StrUtil.isNotBlank(this.mobile)) {
            this.mobile = StrUtil.hidePhone(this.mobile);
        }
        // if (StrUtil.isBlank(this.nickname)) {
        //     this.nickname = user.getUsername();
        // }
        if (StrUtil.isNotBlank(this.withdrawName)) {
            this.withdrawName = StrUtil.hidePhone(this.withdrawName);
        }
        this.fundPwdBound = StrUtil.isNotBlank(user.getFundPwd());

        if (profile != null) {
            // this.sex = profile.getSex();
            // this.birthday = profile.getBirthday();
            // if (this.birthday != null) {
            //     this.age = this.birthday.getYear() - LocalDate.now().getYear() + 1;
            // }
            this.myInviteCode = profile.getMyInviteCode();
            // this.selfSignature = profile.getselfSignature();
        }

        this.messageEncryptKey = StrUtil.rsaPublicEncryptApiData(MessageEncryptUtil.ENCRYPT_KEY);

        this.myInviteCode = Optional.ofNullable(this.myInviteCode).orElse(StrUtil.EMPTY);
    }

    @ApiModelProperty(value = "用户名", position = 1)
    private String username;

    @ApiModelProperty(value = "手机国际区号，有可能为空", position = 2)
    private String mobilePrefix;

    @ApiModelProperty(value = "手机，已模糊，有可能为空", position = 3)
    private String mobile;

    @ApiModelProperty(value = "余额", position = 4)
    private BigDecimal balance;
    //
    // @ApiModelProperty(value = "昵称，不会空", position = 3)
    // private String nickname;
    //
    // @ApiModelProperty(value = "头像，URL格式，如果为空，则用一个默认的本地文件头像", position = 4)
    // private String avatar;

    @ApiModelProperty(value = "提现姓名，已模糊，有可能为空", position = 5)
    private String withdrawName;

    @ApiModelProperty(value = "是否设置了资金密码", position = 6)
    private Boolean fundPwdBound;

    // @ApiModelProperty(value = "性别，有可能为空", position = 7)
    // private SexEnum sex;
    //
    // @ApiModelProperty(value = "生日，yyyy-MM-dd，有可能为空", position = 8)
    // private LocalDate birthday;
    //
    // @ApiModelProperty(value = "年龄，有可能为空", position = 9)
    // private Integer age;

    @ApiModelProperty(value = "我的邀请码，不会空", position = 7)
    private String myInviteCode;

    // @ApiModelProperty(value = "个性签名", position = 11)
    // private String selfSignature;

    @ApiModelProperty(value = "token，保存在本地，并且每个/auth请求都放到header的" + JwtUtil.AUTH_HEADER + "里", position = 8)
    private String token;

    @ApiModelProperty(value = "用户签名，与腾讯IM交互时使用", position = 9)
    private String userSig;

    @ApiModelProperty(value = "App 在即时通信 IM 分配的应用标识", position = 10)
    private Long tecentImSdkAppid;

    @ApiModelProperty(value = "消息AES KEY，请使用RSA私解密，RSA密解之前已经提供过了，如果没有RSA解密方法，请再找我", position = 11)
    private String messageEncryptKey;
}
