package com.im.common.service;

import com.im.common.entity.tim.TimGlobalShutUp;
import com.im.common.param.TimGlobalShutUpSetParam;
import com.im.common.response.RestResponse;

import java.util.List;

/**
 * 用户全局禁言时间
 */
public interface TimGlobalShutUpService {

    /**
     * 设置禁言时间
     */
    RestResponse set(TimGlobalShutUpSetParam param);

    /**
     * 查询用户禁言时间
     */
    TimGlobalShutUp getByUserId(Long userId);

    /**
     * 查询用户集合禁言时间
     */
    List<TimGlobalShutUp> getByUserIds(List<Long> userIds);

}
