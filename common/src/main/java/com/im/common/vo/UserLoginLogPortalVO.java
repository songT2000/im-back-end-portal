package com.im.common.vo;

import com.im.common.entity.UserLoginLog;
import com.im.common.entity.enums.DeviceTypeEnum;
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
public class UserLoginLogPortalVO {
    public static final BeanCopier BEAN_COPIER = BeanCopier.create(UserLoginLog.class, UserLoginLogPortalVO.class, false);

    public UserLoginLogPortalVO(UserLoginLog log) {
        BEAN_COPIER.copy(log, this, null);
    }

    @ApiModelProperty("登录IP")
    private String ip;

    @ApiModelProperty("登录区域")
    private String area;

    @ApiModelProperty("登录设备标识")
    private String deviceId;

    @ApiModelProperty("登录设备类型")
    private DeviceTypeEnum deviceType;

    @ApiModelProperty("登录时间")
    private LocalDateTime createTime;
}
