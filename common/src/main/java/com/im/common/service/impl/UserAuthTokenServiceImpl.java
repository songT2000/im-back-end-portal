package com.im.common.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.im.common.cache.impl.SysConfigCache;
import com.im.common.cache.sysconfig.bo.AdminConfigBO;
import com.im.common.cache.sysconfig.bo.GlobalConfigBO;
import com.im.common.cache.sysconfig.bo.PortalConfigBO;
import com.im.common.entity.UserAuthToken;
import com.im.common.entity.enums.DeviceTypeEnum;
import com.im.common.entity.enums.PortalTypeEnum;
import com.im.common.mapper.UserAuthTokenMapper;
import com.im.common.param.BaseLoginParam;
import com.im.common.param.TokenLoginParam;
import com.im.common.response.ResponseCode;
import com.im.common.response.RestResponse;
import com.im.common.service.AdminUserService;
import com.im.common.service.PortalUserService;
import com.im.common.service.UserAuthTokenService;
import com.im.common.util.*;
import com.im.common.util.jwt.JwtInfo;
import com.im.common.util.jwt.JwtUtil;
import com.im.common.util.mybatis.service.MyBatisPlusServiceImpl;
import com.im.common.util.redis.RedisSessionManager;
import com.im.common.util.redis.RedisSessionManagerFactory;
import com.im.common.util.redis.RedisSessionUser;
import com.im.common.util.user.UserUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * 登录token表 服务实现类
 *
 * @author Barry
 * @date 2018/5/18
 */
@Service
public class UserAuthTokenServiceImpl
        extends MyBatisPlusServiceImpl<UserAuthTokenMapper, UserAuthToken>
        implements UserAuthTokenService {

    private static final Log LOG = LogFactory.get();

    private RedisSessionManagerFactory redisSessionManagerFactory;
    private SysConfigCache sysConfigCache;
    private PortalUserService portalUserService;
    private AdminUserService adminUserService;
    private UserAuthTokenMapper userAuthTokenMapper;

    @Autowired
    public void setRedisSessionManagerFactory(RedisSessionManagerFactory redisSessionManagerFactory) {
        this.redisSessionManagerFactory = redisSessionManagerFactory;
    }

    @Autowired
    public void setSysConfigCache(SysConfigCache sysConfigCache) {
        this.sysConfigCache = sysConfigCache;
    }

    @Autowired
    public void setPortalUserService(PortalUserService portalUserService) {
        this.portalUserService = portalUserService;
    }

    @Autowired
    public void setAdminUserService(AdminUserService adminUserService) {
        this.adminUserService = adminUserService;
    }

    @Autowired
    public void setUserAuthTokenMapper(UserAuthTokenMapper userAuthTokenMapper) {
        this.userAuthTokenMapper = userAuthTokenMapper;
    }

    @Override
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public UserAuthToken getByToken(String token, PortalTypeEnum portalType) {
        return lambdaQuery()
                .eq(UserAuthToken::getToken, token)
                .eq(UserAuthToken::getPortalType, portalType)
                .one();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String saveAuthTokenAfterLogin(RedisSessionUser sessionUser, BaseLoginParam param) {
        Object[] tokenArr = newJwtToken(sessionUser.getUsername(), param.getDeviceId(), param.getDeviceType(), sessionUser.getPortalType());
        String token = String.valueOf(tokenArr[1]);

        Long expireSeconds = (Long) tokenArr[0];

        UserAuthToken authToken = new UserAuthToken();
        authToken.setUserId(sessionUser.getId());
        authToken.setPortalType(sessionUser.getPortalType());
        authToken.setToken(token);
        authToken.setDeviceId(param.getDeviceId());
        authToken.setDeviceType(param.getDeviceType());

        LocalDateTime expireTime = LocalDateTime.now().plusSeconds(expireSeconds);

        authToken.setExpireTime(expireTime);

        // 保存token
        save(authToken);

        return token;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteTokenAndSession(String token, PortalTypeEnum portalType) {
        lambdaUpdate()
                .eq(UserAuthToken::getToken, token)
                .eq(UserAuthToken::getPortalType, portalType)
                .remove();

        redisSessionManagerFactory.getSessionManager(portalType).removeSession(token);

        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteUserTokenAndSession(long userId, PortalTypeEnum portalType) {
        RedisSessionManager sessionManager = redisSessionManagerFactory.getSessionManager(portalType);
        String username = UserUtil.getUsernameByIdFromLocal(userId, portalType);

        // 删除所有redis会话
        Set<String> tokenSet = sessionManager.listAllTokenByUsername(username);
        if (CollectionUtil.isNotEmpty(tokenSet)) {
            for (String token : tokenSet) {
                sessionManager.removeSession(token);
            }
        }

        // 删除所有token会话
        List<UserAuthToken> authTokenList = listByUserId(userId, portalType);
        if (CollectionUtil.isNotEmpty(authTokenList)) {
            for (UserAuthToken authToken : authTokenList) {
                deleteTokenAndSession(authToken.getToken(), portalType);
            }
        }

        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteTokenOnly(String token, PortalTypeEnum portalType) {
        return lambdaUpdate()
                .eq(UserAuthToken::getToken, token)
                .eq(UserAuthToken::getPortalType, portalType)
                .remove();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteUserTokenOnly(long userId, PortalTypeEnum portalType) {
        return lambdaUpdate()
                .eq(UserAuthToken::getUserId, userId)
                .eq(UserAuthToken::getPortalType, portalType)
                .remove();
    }

    @Override
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public List<UserAuthToken> listByUserId(long userId, PortalTypeEnum portalType, String excludeToken) {
        LambdaQueryChainWrapper<UserAuthToken> wrapper = lambdaQuery()
                .eq(UserAuthToken::getUserId, userId)
                .eq(UserAuthToken::getPortalType, portalType);

        if (StrUtil.isNotBlank(excludeToken)) {
            wrapper = wrapper.ne(UserAuthToken::getToken, excludeToken);
        }

        return wrapper.orderByAsc(UserAuthToken::getCreateTime).list();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public <T extends RedisSessionUser> T checkToken(HttpServletRequest request, HttpServletResponse response, PortalTypeEnum portalType) {
        // 解析token
        String token = JwtUtil.getTokenFromHeader(request);
        if (StrUtil.isBlank(token)) {
            ResponseUtil.printJson(response, RestResponse.failed(ResponseCode.USER_SESSION_PLEASE_LOGIN_FIRST));
            return null;
        }

        JwtInfo jwtInfo = JwtUtil.getJwtInfo(token);
        if (jwtInfo == null) {
            ResponseUtil.printJson(response, RestResponse.failed(ResponseCode.USER_SESSION_TOKEN_INVALID));
            return null;
        }

        // 会话期间，不验证token是否过期，只检查是否被登出
        T sessionUser = redisSessionManagerFactory.getSessionManager(portalType).getSessionUser(request);
        if (sessionUser != null) {
            // 是否被登出
            if (hasForceLogout(token, portalType, response)) {
                return null;
            }
            return sessionUser;
        }

        // 该token没有在登录状态，自动登录
        TokenLoginParam loginParam = new TokenLoginParam();
        loginParam.setToken(token);
        loginParam.setUrl(RequestUtil.getRequestWebsite(request));
        loginParam.setIp(RequestUtil.getRequestIp(request));
        loginParam.setDeviceId(jwtInfo.getDeviceId());
        loginParam.setDeviceType(EnumUtil.valueOfIEnum(DeviceTypeEnum.class, jwtInfo.getDeviceType()));

        // token登录
        RestResponse rsp = RestResponse.failed(ResponseCode.USER_NOT_FOUND);
        if (portalType == PortalTypeEnum.PORTAL) {
            // LOG.error("token登录,loginParam:{}",loginParam);
            rsp = portalUserService.tokenLogin(request, loginParam);
        } else if (portalType == PortalTypeEnum.ADMIN) {
            rsp = adminUserService.tokenLogin(request, loginParam);
        }

        if (!rsp.isOkRsp()) {
            ResponseUtil.printJson(response, rsp);
            return null;
        }

        // // token中的用户名和会话的用户名不一致
        // if (!jwtInfo.getUsername().equals(sessionUser.getUsername())) {
        //     LOG.error("token中的用户名和会话的用户名不一致");
        //     LOG.error("jwtInfoUsername:{}", jwtInfo.getUsername());
        //     LOG.error("sessionUsername:{}", sessionUser.getUsername());
        //     LOG.error("jwtInfo:{}", jwtInfo);
        //     LOG.error("sessionUser:{}", jwtInfo);
        //     deleteTokenAndSession(token, portalType);
        //     ResponseUtil.printJson(response, RestResponse.logout(ResponseCode.USER_SESSION_INVALIDATE));
        //     return null;
        // }

        // renew token
        long renewExpireSeconds = getExpireSeconds(portalType);
        if (NumberUtil.isGreatThenZero(renewExpireSeconds)) {
            lambdaUpdate()
                    .eq(UserAuthToken::getToken, token)
                    .eq(UserAuthToken::getPortalType, portalType)
                    .set(UserAuthToken::getExpireTime, LocalDateTime.now().plusSeconds(renewExpireSeconds))
                    .update();
        }

        sessionUser = (T) rsp.getData();

        return sessionUser;
    }

    private boolean hasForceLogout(String token, PortalTypeEnum portalType, HttpServletResponse response) {
        // 强制登出
        String logoutCode = redisSessionManagerFactory.getSessionManager(portalType).getLogoutCode(token);
        if (StrUtil.isNotBlank(logoutCode)) {

            deleteTokenAndSession(token, portalType);

            ResponseUtil.printJson(response, RestResponse.logout(ResponseCode.getEnum(logoutCode)));
            return true;
        }

        return false;
    }

    @Override
    public Object[] newJwtToken(String username, String deviceId,
                                DeviceTypeEnum deviceType, PortalTypeEnum portalType) {
        JwtInfo jwtInfo = new JwtInfo(username, deviceId, null, deviceType.getVal(), portalType.getVal());

        // 过期时间
        long expireSeconds = getExpireSeconds(portalType);

        GlobalConfigBO globalConfig = sysConfigCache.getGlobalConfigFromLocal();
        String token = JwtUtil.sign(jwtInfo, globalConfig.getJwtSecret(), expireSeconds);

        return new Object[]{expireSeconds, token};
    }

    private long getExpireSeconds(PortalTypeEnum portalType) {
        long expireSeconds = 0;
        if (portalType == PortalTypeEnum.PORTAL) {
            // 前台过期时间
            PortalConfigBO portalConfig = sysConfigCache.getPortalConfigFromLocal();
            expireSeconds = Duration.ofMinutes(portalConfig.getLoginExpireMinutes()).getSeconds();

            // 这个地方就是说会导致WEB+H5会跟原生一样，过几天后打开浏览器会自动登录
            // if (deviceType != UserDeviceTypeEnum.IOS && deviceType != UserDeviceTypeEnum.ANDROID) {
            //     // 非原生30分钟过期
            //     expireSeconds = Duration.ofMinutes(SessionManager.SESSION_EXPIRE_MINUTES).getSeconds();
            // } else {
            //     // 原生过期时间取配置
            //     expireSeconds = Duration.ofMinutes(portalConfig.getTokenExpireMinutes()).getSeconds();
            // }
        } else if (portalType == PortalTypeEnum.ADMIN) {
            // 后台过期时间
            AdminConfigBO adminConfig = sysConfigCache.getAdminConfigFromLocal();
            expireSeconds = Duration.ofMinutes(adminConfig.getLoginExpireMinutes()).getSeconds();
        }
        return expireSeconds;
    }

    @Override
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public List<UserAuthToken> listExpiredUserAuthToken() {
        return lambdaQuery()
                .lt(UserAuthToken::getExpireTime, LocalDateTime.now())
                .list();
    }
}
