package com.im.common.vo;

import com.im.common.entity.UserLoginLog;
import com.im.common.entity.enums.DeviceTypeEnum;
import com.im.common.entity.enums.UserLoginTypeEnum;
import com.im.common.util.user.UserUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.beans.BeanCopier;

import java.time.LocalDateTime;

/**
 * 登录日志VO
 *
 * @author Barry
 * @date 2020-05-27
 */
@Data
@NoArgsConstructor
@ApiModel
public class UserLoginLogAdminVO {
    public static final BeanCopier BEAN_COPIER = BeanCopier.create(UserLoginLog.class, UserLoginLogAdminVO.class, false);

    public UserLoginLogAdminVO(UserLoginLog log) {
        BEAN_COPIER.copy(log, this, null);

        this.username = UserUtil.getUsernameByIdFromLocal(log.getUserId(), log.getPortalType());
    }

    @ApiModelProperty(value = "用户ID", position = 1)
    private Long userId;

    @ApiModelProperty(value = "用户名", position = 2)
    private String username;

    @ApiModelProperty(value = "登录域名", position = 3)
    private String url;

    @ApiModelProperty(value = "登录IP", position = 4)
    private String ip;

    @ApiModelProperty(value = "登录区域", position = 5)
    private String area;

    @ApiModelProperty(value = "登录设备标识", position = 6)
    private String deviceId;

    @ApiModelProperty(value = "登录设备类型", position = 7)
    private DeviceTypeEnum deviceType;

    @ApiModelProperty(value = "登录方式", position = 8)
    private UserLoginTypeEnum loginType;

    @ApiModelProperty(value = "登录时间", position = 9)
    private LocalDateTime createTime;
}
