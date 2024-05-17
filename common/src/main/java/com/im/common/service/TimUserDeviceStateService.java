package com.im.common.service;

import com.im.common.entity.tim.TimUserDeviceState;
import com.im.common.response.RestResponse;
import com.im.common.util.mybatis.service.MyBatisPlusService;

/**
 * 用户设备状态变更服务
 */
public interface TimUserDeviceStateService extends MyBatisPlusService<TimUserDeviceState> {
    /**
     * 获取最后登录设备
     *
     * @param userId
     * @return
     */
    TimUserDeviceState getLastLoginDevice(long userId);

    /**
     * 用户设备状态变更
     *
     * @param timUserDeviceState 变更信息
     */
    void change(TimUserDeviceState timUserDeviceState);

    /**
     * 从api中同步一下在线状态，防止出现错误数据
     */
    void syncStateFromSdk();

    /**
     * 踢下线
     *
     * @param userId 用户ID
     */
    RestResponse kickOut(Long userId);
}
