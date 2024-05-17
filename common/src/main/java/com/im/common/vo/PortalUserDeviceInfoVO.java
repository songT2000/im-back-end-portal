package com.im.common.vo;

import com.im.common.entity.PortalUserDeviceInfo;
import com.im.common.entity.enums.DeviceTypeEnum;
import com.im.common.entity.enums.PortalTypeEnum;
import com.im.common.util.user.UserUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.beans.BeanCopier;

import java.time.LocalDateTime;

/**
 * 用户设备信息
 */
@Data
@NoArgsConstructor
@ApiModel
public class PortalUserDeviceInfoVO {

    public static final BeanCopier BEAN_COPIER = BeanCopier.create(PortalUserDeviceInfo.class, PortalUserDeviceInfoVO.class, false);

    public PortalUserDeviceInfoVO(PortalUserDeviceInfo e) {
        BEAN_COPIER.copy(e, this, null);
        this.username = UserUtil.getUsernameByIdFromLocal(e.getUserId(), PortalTypeEnum.PORTAL);
    }

    @ApiModelProperty("账号")
    private String username;

    @ApiModelProperty("设备类型")
    private DeviceTypeEnum deviceType;

    @ApiModelProperty("手机厂商")
    private String deviceBrand;

    @ApiModelProperty("手机型号")
    private String systemModel;

    @ApiModelProperty("系统版本")
    private String systemVersion;

    @ApiModelProperty("app版本")
    private String appVersion;

    @ApiModelProperty("设备唯一标识符")
    private String deviceUnique;

    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;
}
