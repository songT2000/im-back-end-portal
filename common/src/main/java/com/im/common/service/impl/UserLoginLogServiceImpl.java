package com.im.common.service.impl;

import com.im.common.entity.UserLoginLog;
import com.im.common.entity.enums.PortalTypeEnum;
import com.im.common.entity.enums.UserLoginTypeEnum;
import com.im.common.mapper.UserLoginLogMapper;
import com.im.common.param.BaseLoginParam;
import com.im.common.param.ManualLoginParam;
import com.im.common.param.TokenLoginParam;
import com.im.common.service.UserLoginLogService;
import com.im.common.util.ip.IpAddressUtil;
import com.im.common.util.mybatis.service.MyBatisPlusServiceImpl;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户登录日志 服务实现类
 *
 * @author Barry
 * @date 2020-05-25
 */
@Service
public class UserLoginLogServiceImpl
        extends MyBatisPlusServiceImpl<UserLoginLogMapper, UserLoginLog>
        implements UserLoginLogService {
    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserLoginLog addLoginLog(Long userId, BaseLoginParam param, PortalTypeEnum portalType) {
        UserLoginLog log = new UserLoginLog();

        String area = IpAddressUtil.findLocation(param.getIp());

        log.setUserId(userId);
        log.setPortalType(portalType);
        log.setIp(param.getIp());
        log.setArea(area);
        log.setDeviceId(param.getDeviceId());
        log.setDeviceType(param.getDeviceType());
        log.setUrl(param.getUrl());

        UserLoginTypeEnum loginType = null;
        if (param instanceof ManualLoginParam) {
            loginType = UserLoginTypeEnum.MANUAL;
        } else if (param instanceof TokenLoginParam) {
            loginType = UserLoginTypeEnum.TOKEN;
        } else {
            loginType = UserLoginTypeEnum.MANUAL;
        }
        log.setLoginType(loginType);

        save(log);

        return log;
    }
}