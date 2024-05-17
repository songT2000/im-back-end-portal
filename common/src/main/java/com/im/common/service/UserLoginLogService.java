package com.im.common.service;

import com.im.common.entity.UserLoginLog;
import com.im.common.entity.enums.PortalTypeEnum;
import com.im.common.param.BaseLoginParam;
import com.im.common.util.mybatis.service.MyBatisPlusService;
import lombok.NonNull;

/**
 * 用户登录日志 服务类
 *
 * @author Barry
 * @date 2020-05-25
 */
public interface UserLoginLogService extends MyBatisPlusService<UserLoginLog> {
    /**
     * 新增登录日志，不会保留试玩的登录日志
     *
     * @param userId   用户ID
     * @param param    登录参数
     * @param portalType 门户类型
     * @return UserLoginLog
     */
    UserLoginLog addLoginLog(Long userId, BaseLoginParam param, PortalTypeEnum portalType);
}
