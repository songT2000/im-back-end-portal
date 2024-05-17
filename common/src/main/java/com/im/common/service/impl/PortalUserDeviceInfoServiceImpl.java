package com.im.common.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.im.common.entity.PortalUserDeviceInfo;
import com.im.common.entity.enums.DeviceTypeEnum;
import com.im.common.mapper.PortalUserDeviceInfoMapper;
import com.im.common.param.PortalUserDeviceInfoParam;
import com.im.common.response.RestResponse;
import com.im.common.service.PortalUserDeviceInfoService;
import com.im.common.util.mybatis.service.MyBatisPlusServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Barry
 * @date 2022-02-10
 */
@Service
public class PortalUserDeviceInfoServiceImpl extends MyBatisPlusServiceImpl<PortalUserDeviceInfoMapper, PortalUserDeviceInfo>
        implements PortalUserDeviceInfoService {

    @Override
    public RestResponse bind(Long userId, DeviceTypeEnum deviceType, PortalUserDeviceInfoParam param) {
        PortalUserDeviceInfo latest = queryLatest(userId);
        if (ObjectUtil.isNotEmpty(latest)) {
            //对比数据是否有变化,如果有变化，新增一条记录，没有变化，更新时间
            if (latest.getDeviceBrand().equals(param.getDeviceBrand())
                    && latest.getSystemModel().equals(param.getSystemModel())
                    && latest.getSystemVersion().equals(param.getSystemVersion())
                    && latest.getAppVersion().equals(param.getAppVersion())
                    && deviceType.equals(latest.getDeviceType())) {
                updateById(latest);
            } else {
                create(userId, deviceType, param);
            }
        } else {
            create(userId, deviceType, param);
        }

        return RestResponse.OK;
    }

    @Override
    public PortalUserDeviceInfo queryLatest(Long userId) {
        List<PortalUserDeviceInfo> list = lambdaQuery().eq(PortalUserDeviceInfo::getUserId, userId).orderByDesc(PortalUserDeviceInfo::getUpdateTime).list();
        return CollUtil.isNotEmpty(list) ? list.get(0) : null;
    }

    private void create(Long userId, DeviceTypeEnum deviceType, PortalUserDeviceInfoParam param) {
        PortalUserDeviceInfo deviceInfo = new PortalUserDeviceInfo();
        deviceInfo.setUserId(userId);
        deviceInfo.setDeviceBrand(param.getDeviceBrand());
        deviceInfo.setAppVersion(param.getAppVersion());
        deviceInfo.setSystemModel(param.getSystemModel());
        deviceInfo.setSystemVersion(param.getSystemVersion());
        deviceInfo.setDeviceType(deviceType);
        deviceInfo.setDeviceUnique(param.getDeviceUnique());
        save(deviceInfo);
    }


}
