package com.im.common.vo;

import com.im.common.entity.PortalUserProfile;
import com.im.common.entity.tim.TimUserDeviceState;
import com.im.common.util.StrUtil;
import com.im.common.util.ip.IpAddressUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * <p>前台用户当前登录信息</p>
 *
 * @author Barry
 * @date 2019-10-12
 */
@Data
@NoArgsConstructor
@ApiModel
public class PortalUserLastLoginIpVO {
    public PortalUserLastLoginIpVO(TimUserDeviceState lastLoginDevice, PortalUserProfile profile, String username, Boolean online) {
        this.username = username;

        if (lastLoginDevice != null && StrUtil.isNotBlank(lastLoginDevice.getClientIp())) {
            this.lastLoginTime = lastLoginDevice.getCreateTime();
            this.lastLoginIp = lastLoginDevice.getClientIp();
            this.lastLoginArea = IpAddressUtil.findLocation(this.lastLoginIp);
        } else {
            if (profile != null) {
                this.lastLoginTime = profile.getLastLoginTime();
                this.lastLoginIp = profile.getLastLoginIp();
                this.lastLoginArea = profile.getLastLoginArea();
            }
        }

        this.online = Boolean.TRUE.equals(online);
    }

    // public PortalUserLastLoginIpVO(PortalUserProfile profile, String username, Boolean online) {
    //     this.username = username;
    //     if (profile != null) {
    //         this.lastLoginTime = profile.getLastLoginTime();
    //         this.lastLoginIp = profile.getLastLoginIp();
    //         this.lastLoginArea = profile.getLastLoginArea();
    //     }
    //     this.online = online;
    // }

    @ApiModelProperty(value = "用户名", position = 1)
    private String username;

    @ApiModelProperty(value = "上次登录时间", position = 2)
    private LocalDateTime lastLoginTime;

    @ApiModelProperty(value = "上次登录IP", position = 3)
    private String lastLoginIp;

    @ApiModelProperty(value = "上次登录区域", position = 4)
    private String lastLoginArea;

    @ApiModelProperty(value = "是否在线", position = 5)
    private Boolean online;
}
