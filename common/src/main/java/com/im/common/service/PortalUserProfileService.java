package com.im.common.service;

import com.im.common.entity.PortalUserProfile;
import com.im.common.entity.UserLoginLog;
import com.im.common.util.mybatis.service.MyBatisPlusService;

/**
 * 用户资料表 服务类
 *
 * @author Barry
 * @date 2021-12-12
 */
public interface PortalUserProfileService extends MyBatisPlusService<PortalUserProfile> {
    /**
     * 创建资料，并生成默认的邀请码
     *
     * @param profile
     */
    void createUserProfile(PortalUserProfile profile);

    /**
     * 修改上次登录信息
     *
     * @param loginLog
     */
    void updateLastLogin(UserLoginLog loginLog);

    /**
     * 定时任务调用同步用户资料(同步当天活跃的用户)
     */
    void syncFromSdk();
}
