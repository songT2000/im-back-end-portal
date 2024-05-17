package com.im.common.service;

import com.im.common.entity.PortalUserDeviceInfo;
import com.im.common.entity.enums.DeviceTypeEnum;
import com.im.common.param.PortalUserDeviceInfoParam;
import com.im.common.response.RestResponse;
import com.im.common.util.mybatis.service.MyBatisPlusService;

public interface PortalUserDeviceInfoService extends MyBatisPlusService<PortalUserDeviceInfo> {

    /**
     * 保存用户设备信息
     */
    RestResponse bind(Long userId, DeviceTypeEnum deviceType, PortalUserDeviceInfoParam param);

    PortalUserDeviceInfo queryLatest(Long userId);
}
