package com.im.common.service.impl;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.im.common.cache.base.CacheProxy;
import com.im.common.cache.impl.PortalUserCache;
import com.im.common.cache.impl.SmsTemplateConfigCache;
import com.im.common.cache.impl.SysConfigCache;
import com.im.common.cache.impl.UserGroupCache;
import com.im.common.cache.sysconfig.bo.ImConfigBO;
import com.im.common.cache.sysconfig.bo.PortalConfigBO;
import com.im.common.cache.sysconfig.bo.RegisterConfigBO;
import com.im.common.cache.sysconfig.bo.ReportConfigBO;
import com.im.common.constant.CommonConstant;
import com.im.common.constant.RedisKeyEnum;
import com.im.common.constant.RegexConstant;
import com.im.common.entity.*;
import com.im.common.entity.enums.*;
import com.im.common.exception.ImException;
import com.im.common.mapper.PortalUserMapper;
import com.im.common.param.*;
import com.im.common.response.ResponseCode;
import com.im.common.response.RestResponse;
import com.im.common.service.*;
import com.im.common.util.*;
import com.im.common.util.api.im.tencent.entity.param.friend.TiFriendAddParam;
import com.im.common.util.api.im.tencent.entity.param.message.TIMTextElem;
import com.im.common.util.api.im.tencent.entity.param.message.TiMessage;
import com.im.common.util.api.im.tencent.entity.param.message.TiMsgBody;
import com.im.common.util.api.im.tencent.entity.param.portrait.TiAccountPortraitParam;
import com.im.common.util.api.im.tencent.service.rest.TiAccountPortraitService;
import com.im.common.util.api.im.tencent.service.rest.TiAccountService;
import com.im.common.util.api.im.tencent.service.rest.TiFriendService;
import com.im.common.util.api.im.tencent.service.rest.TiSingleChatService;
import com.im.common.util.api.im.tencent.util.TLSSigAPIv2;
import com.im.common.util.i18n.I18nTranslateUtil;
import com.im.common.util.jwt.JwtInfo;
import com.im.common.util.jwt.JwtUtil;
import com.im.common.util.mybatis.page.PageVO;
import com.im.common.util.mybatis.service.MyBatisPlusServiceImpl;
import com.im.common.util.redis.PortalRedisSessionManager;
import com.im.common.util.redis.RedisKeySerializer;
import com.im.common.util.user.UserUtil;
import com.im.common.vo.*;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * 用户表 服务实现类
 *
 * @author Barry
 * @date 2021-12-11
 */
@Service
public class PortalUserServiceImpl
        extends MyBatisPlusServiceImpl<PortalUserMapper, PortalUser>
        implements PortalUserService {
    private static final Log LOG = LogFactory.get();

    private PortalUserMapper portalUserMapper;
    private PortalRedisSessionManager portalRedisSessionManager;
    private SysConfigCache sysConfigCache;
    private CacheProxy cacheProxy;
    private UserAuthTokenService userAuthTokenService;
    private UserLoginLogService userLoginLogService;
    private PortalUserProfileService portalUserProfileService;
    private GoogleAuthService googleAuthService;
    private UserBillService userBillService;
    private TiAccountService tiAccountService;
    private TiAccountPortraitService tiAccountPortraitService;
    private ValueOperations<String, String> redisValue;
    private RedisKeySerializer redisKeySerializer;
    private SmsTemplateConfigCache smsTemplateConfigCache;
    private SmsService smsService;
    private UserGroupCache userGroupCache;
    private UserGroupUserService userGroupUserService;
    private TiFriendService tiFriendService;
    private PortalUserCache portalUserCache;
    private TiSingleChatService tiSingleChatService;
    private TimMessageC2cService timMessageC2cService;
    private TimMessageGroupService timMessageGroupService;
    private PortalUserDeviceInfoService portalUserDeviceInfoService;

    @Autowired
    public void setPortalUserMapper(PortalUserMapper portalUserMapper) {
        this.portalUserMapper = portalUserMapper;
    }

    @Autowired
    public void setPortalRedisSessionManager(PortalRedisSessionManager portalRedisSessionManager) {
        this.portalRedisSessionManager = portalRedisSessionManager;
    }

    @Autowired
    public void setSysConfigCache(SysConfigCache sysConfigCache) {
        this.sysConfigCache = sysConfigCache;
    }

    @Autowired
    public void setCacheProxy(CacheProxy cacheProxy) {
        this.cacheProxy = cacheProxy;
    }

    @Autowired
    public void setUserAuthTokenService(UserAuthTokenService userAuthTokenService) {
        this.userAuthTokenService = userAuthTokenService;
    }

    @Autowired
    public void setUserLoginLogService(UserLoginLogService userLoginLogService) {
        this.userLoginLogService = userLoginLogService;
    }

    @Autowired
    public void setPortalUserProfileService(PortalUserProfileService portalUserProfileService) {
        this.portalUserProfileService = portalUserProfileService;
    }

    @Autowired
    public void setGoogleAuthService(GoogleAuthService googleAuthService) {
        this.googleAuthService = googleAuthService;
    }

    @Autowired
    public void setUserBillService(UserBillService userBillService) {
        this.userBillService = userBillService;
    }

    @Autowired
    public void setTiAccountService(TiAccountService tiAccountService) {
        this.tiAccountService = tiAccountService;
    }

    @Autowired
    public void setTiAccountPortraitService(TiAccountPortraitService tiAccountPortraitService) {
        this.tiAccountPortraitService = tiAccountPortraitService;
    }

    @Autowired
    public void setRedisValue(ValueOperations<String, String> redisValue) {
        this.redisValue = redisValue;
    }

    @Autowired
    public void setRedisKeySerializer(RedisKeySerializer redisKeySerializer) {
        this.redisKeySerializer = redisKeySerializer;
    }

    @Autowired
    public void setSmsTemplateConfigCache(SmsTemplateConfigCache smsTemplateConfigCache) {
        this.smsTemplateConfigCache = smsTemplateConfigCache;
    }

    @Autowired
    public void setSmsService(SmsService smsService) {
        this.smsService = smsService;
    }

    @Autowired
    public void setUserGroupCache(UserGroupCache userGroupCache) {
        this.userGroupCache = userGroupCache;
    }

    @Autowired
    public void setUserGroupUserService(UserGroupUserService userGroupUserService) {
        this.userGroupUserService = userGroupUserService;
    }

    @Autowired
    public void setTiFriendService(TiFriendService tiFriendService) {
        this.tiFriendService = tiFriendService;
    }

    @Autowired
    public void setPortalUserCache(PortalUserCache portalUserCache) {
        this.portalUserCache = portalUserCache;
    }

    @Autowired
    public void setTiSingleChatService(TiSingleChatService tiSingleChatService) {
        this.tiSingleChatService = tiSingleChatService;
    }

    @Autowired
    public void setTimMessageC2cService(TimMessageC2cService timMessageC2cService) {
        this.timMessageC2cService = timMessageC2cService;
    }

    @Autowired
    public void setTimMessageGroupService(TimMessageGroupService timMessageGroupService) {
        this.timMessageGroupService = timMessageGroupService;
    }

    @Autowired
    public void setPortalUserDeviceInfoService(PortalUserDeviceInfoService portalUserDeviceInfoService) {
        this.portalUserDeviceInfoService = portalUserDeviceInfoService;
    }

    @Override
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public PortalUser getByUsername(String username) {
        return lambdaQuery()
                .eq(PortalUser::getUsername, username).one();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResponse<PortalUserLoginVO> manualLogin(@NonNull HttpServletRequest request, @NonNull ManualLoginParam param) {
        // todo 要支持手机登录，还没做，搭完架子再做

        PortalUser user = getByUsername(param.getUsername());
        if (user == null) {
            return RestResponse.failed(ResponseCode.USER_LOGIN_USERNAME_OR_PASSWORD_ERROR);
        }
        //对比账号，防止大小写不区分导致腾讯IM登录失败，必须保证大小写一致
        if (!user.getUsername().equals(param.getUsername())) {
            return RestResponse.failed(ResponseCode.USER_LOGIN_USERNAME_OR_PASSWORD_ERROR);
        }
        if (!Boolean.TRUE.equals(user.getEnabled())) {
            return RestResponse.failed(ResponseCode.YOUR_ACCOUNT_LOGIN_HAS_BEEN_DISABLED);
        }
        if (!Boolean.TRUE.equals(user.getLoginEnabled())) {
            return RestResponse.failed(ResponseCode.YOUR_ACCOUNT_LOGIN_HAS_BEEN_DISABLED);
        }

        // 验证密码
        boolean pwdValid = PasswordUtil.validatePwd(user.getLoginPwd(), param.getPassword());
        if (!pwdValid) {
            return RestResponse.failed(ResponseCode.USER_LOGIN_USERNAME_OR_PASSWORD_ERROR);
        }

        // 处理登录通用逻辑
        RestResponse<PortalSessionUser> restResponse = processLogin(user, param);
        if (restResponse.isOkRsp()) {
            PortalSessionUser sessionUser = restResponse.getData();

            PortalUserProfile profile = portalUserProfileService.getById(user.getId());

            return RestResponse.ok(new PortalUserLoginVO(sessionUser, user, profile));
        }
        return RestResponse.build(restResponse);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResponse<PortalSessionUser> tokenLogin(@NonNull HttpServletRequest request, @NonNull TokenLoginParam param) {
        // 解析token
        JwtInfo jwtInfo = JwtUtil.getJwtInfo(param.getToken());
        if (jwtInfo == null) {
            return RestResponse.failed(ResponseCode.USER_SESSION_TOKEN_INVALID);
        }

        DeviceTypeEnum deviceType = EnumUtil.valueOfIEnum(DeviceTypeEnum.class, jwtInfo.getDeviceType());
        if (deviceType == null) {
            return RestResponse.failed(ResponseCode.USER_SESSION_TOKEN_INVALID);
        }

        // 如user_auth_token无数据，返回您的登录失效，请重新登录（forceLogout）
        UserAuthToken authToken = userAuthTokenService.getByToken(param.getToken(), PortalTypeEnum.PORTAL);
        if (authToken == null) {
            return RestResponse.logout(ResponseCode.USER_SESSION_INVALIDATE);
        }
        if (authToken.getExpireTime() != null && LocalDateTime.now().isAfter(authToken.getExpireTime())) {
            userAuthTokenService.deleteTokenAndSession(param.getToken(), PortalTypeEnum.PORTAL);
            return RestResponse.logout(ResponseCode.USER_SESSION_EXPIRED);
        }

        // 获取用户
        PortalUser user = getByUsername(jwtInfo.getUsername());
        if (user == null) {
            return RestResponse.logout(ResponseCode.USER_SESSION_INVALIDATE);
        }
        if (!Boolean.TRUE.equals(user.getLoginEnabled())) {
            return RestResponse.failed(ResponseCode.YOUR_ACCOUNT_LOGIN_HAS_BEEN_DISABLED);
        }

        // 处理登录逻辑
        return processLogin(user, param);
    }

    /**
     * 处理登录通用逻辑
     *
     * @param user  用户
     * @param param 登录参数
     * @return PortalRestResponse
     */
    private RestResponse<PortalSessionUser> processLogin(@NonNull PortalUser user, @NonNull BaseLoginParam param) {
        // 判断账号状态
        if (!Boolean.TRUE.equals(user.getLoginEnabled())) {
            return RestResponse.failed(ResponseCode.YOUR_ACCOUNT_LOGIN_HAS_BEEN_DISABLED);
        }

        // 组装会话用户
        PortalSessionUser sessionUser = new PortalSessionUser(user, null, null);

        // 登录token
        String token;
        if (param instanceof TokenLoginParam) {
            token = ((TokenLoginParam) param).getToken();
        } else {
            // user_auth_token表存入
            token = userAuthTokenService.saveAuthTokenAfterLogin(sessionUser, param);
        }
        sessionUser.setToken(token);

        // IM密钥的过期时间跟系统配置一样
        ImConfigBO imConfig = sysConfigCache.getImConfigFromLocal();
        TLSSigAPIv2 tlsSigAPIv2 = new TLSSigAPIv2(imConfig.getTecentImSdkAppid(), imConfig.getTecentImSdkKey());
        PortalConfigBO portalConfig = sysConfigCache.getPortalConfigFromLocal();
        String userSig = tlsSigAPIv2.genUserSig(sessionUser.getUsername(), Duration.ofMinutes(portalConfig.getLoginExpireMinutes()).getSeconds());
        sessionUser.setUserSig(userSig);
        sessionUser.setTecentImSdkAppid(imConfig.getTecentImSdkAppid());

        // 踢出之前已经授权过的记录
        kickOutOtherByOtherLogin(user, token);

        // 登录成功，新增登录日志
        UserLoginLog loginLog =
                userLoginLogService.addLoginLog(user.getId(), param, PortalTypeEnum.PORTAL);

        sessionUser.setLoginIp(loginLog.getIp());
        sessionUser.setLoginArea(loginLog.getArea());
        sessionUser.setLoginDeviceId(loginLog.getDeviceId());
        sessionUser.setLoginDeviceType(loginLog.getDeviceType());
        sessionUser.setLoginTime(loginLog.getCreateTime());

        // 修改用户信息
//        lambdaUpdate()
//                .eq(PortalUser::getId, user.getId())
//                .update();
        portalUserProfileService.updateLastLogin(loginLog);

        // todo 同IP登录日志

        // todo 报表，登录人数
        // ReportConfigBO reportConfig = sysConfigCache.getReportConfigFromRedis();
        // String reportDate = DateTimeUtil.getOffsetDateStrByDateTime(loginLog.getCreateTime(), reportConfig.getOffsetTime());
        // reportService.addLoginCount(user.getId(), user.getRealType(), reportDate);

        // 保存session
        portalRedisSessionManager.setSessionUser(token, sessionUser);

        return RestResponse.ok(sessionUser);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResponse logout(@NonNull HttpServletRequest request) {
        String token = JwtUtil.getTokenFromHeader(request);
        if (StrUtil.isBlank(token)) {
            return RestResponse.OK;
        }

        // 删除该token，删除该redis会话
        userAuthTokenService.deleteTokenAndSession(token, PortalTypeEnum.PORTAL);

        // try {
        //     // 如果没有有效session了，把用户状态设置为离线
        //     JwtInfo jwtInfo = JwtUtil.getJwtInfo(token);
        //     if (jwtInfo != null) {
        //         Long userId = UserUtil.getUserIdByUsernameFromLocal(jwtInfo.getUsername(), PortalTypeEnum.PORTAL);
        //
        //         if (userId != null) {
        //             Set<String> tokenSet = portalRedisSessionManager.listAllTokenByUsername(jwtInfo.getUsername());
        //             // 是否还有有效session
        //             boolean hasValidSession = false;
        //
        //             if (CollectionUtil.isNotEmpty(tokenSet)) {
        //                 for (String otherToken : tokenSet) {
        //                     PortalSessionUser sessionUser = portalRedisSessionManager.getSessionUser(otherToken);
        //                     if (sessionUser != null) {
        //                         String logoutCode = portalRedisSessionManager.getLogoutCode(otherToken);
        //                         if (StrUtil.isBlank(logoutCode)) {
        //                             hasValidSession = true;
        //                             break;
        //                         }
        //                     }
        //                 }
        //             }
        //             if (!hasValidSession) {
        //                 // 把用户状态直接改为离线
        //                 updateUserOffline(userId, false);
        //             }
        //         }
        //     }
        // } catch (Exception e) {
        //     LOG.error(e, "登出出错");
        // }

        return RestResponse.OK;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void kickOutOtherLoginClient(PortalUser user, String excludeToken, ResponseCode logoutCode) {
        // 没有排除token，那么全部踢掉
        if (StrUtil.isBlank(excludeToken)) {
            kickOutAllLoginClient(user.getId(), logoutCode);
            return;
        }

        JwtInfo tokenInfo = JwtUtil.getJwtInfo(excludeToken);
        DeviceTypeEnum tokenDevice = EnumUtil.valueOfIEnum(DeviceTypeEnum.class, tokenInfo.getDeviceType());

        // 当logoutCode参数为空时设置默认值
        ResponseCode realLogoutCode = Optional.ofNullable(logoutCode).orElse(ResponseCode.USER_SESSION_INVALIDATE);

        PortalConfigBO portalConfig = sysConfigCache.getPortalConfigFromLocal();

        int webKeepLoginClient = portalConfig.getWebLoginMaxClient();
        if (StrUtil.isNotEmpty(excludeToken) && DeviceTypeEnum.isWeb(tokenDevice)) {
            webKeepLoginClient--;
        }

        int mobileKeepLoginClient = portalConfig.getMobileLoginMaxClient();
        if (StrUtil.isNotEmpty(excludeToken) && DeviceTypeEnum.isMobile(tokenDevice)) {
            mobileKeepLoginClient--;
        }

        // 处理redis
        Set<String> redisTokenSet = portalRedisSessionManager.listAllTokenByUsername(user.getUsername());
        if (CollectionUtil.isNotEmpty(redisTokenSet)) {
            List<PortalSessionUser> sessionUserList = new ArrayList<>();

            for (String redisToken : redisTokenSet) {
                if (redisToken.equalsIgnoreCase(excludeToken)) {
                    // 当前token不处理
                    continue;
                }

                String sessionUserLogoutCode = portalRedisSessionManager.getLogoutCode(redisToken);
                if (StrUtil.isBlank(sessionUserLogoutCode)) {
                    PortalSessionUser sessionUser = portalRedisSessionManager.getSessionUser(redisToken);

                    if (sessionUser == null) {
                        portalRedisSessionManager.removeSession(redisToken);
                    } else {
                        sessionUserList.add(sessionUser);
                    }
                }
            }

            // 最迟登录的在最前面
            sessionUserList = CollectionUtil.sort(sessionUserList, Comparator.comparing(PortalSessionUser::getLoginTime).reversed());
            for (int i = 0; i < sessionUserList.size(); i++) {
                PortalSessionUser sessionUser = sessionUserList.get(i);
                if (sessionUser.getToken().equals(excludeToken)) {
                    continue;
                }

                JwtInfo sessionTokenInfo = JwtUtil.getJwtInfo(sessionUser.getToken());
                DeviceTypeEnum sessionTokenDevice = EnumUtil.valueOfIEnum(DeviceTypeEnum.class, sessionTokenInfo.getDeviceType());

                if (DeviceTypeEnum.isSameTypeDevice(tokenDevice, sessionTokenDevice)) {

                    // 相同的设备类型，看每个类型允许登录数
                    {
                        if (DeviceTypeEnum.isMobile(tokenDevice) && DeviceTypeEnum.isMobile(sessionTokenDevice)) {

                            if (NumberUtil.isGreatThenZero(portalConfig.getMobileLoginMaxClient())) {

                                if (mobileKeepLoginClient <= 0) {
                                    userAuthTokenService.deleteTokenOnly(sessionUser.getToken(), PortalTypeEnum.PORTAL);
                                    portalRedisSessionManager.setLogoutCode(sessionUser.getToken(), realLogoutCode);
                                } else {
                                    mobileKeepLoginClient--;
                                }
                            }
                        }
                        if (DeviceTypeEnum.isWeb(tokenDevice) && DeviceTypeEnum.isWeb(sessionTokenDevice)) {

                            if (NumberUtil.isGreatThenZero(portalConfig.getWebLoginMaxClient())) {

                                if (webKeepLoginClient <= 0) {
                                    userAuthTokenService.deleteTokenOnly(sessionUser.getToken(), PortalTypeEnum.PORTAL);
                                    portalRedisSessionManager.setLogoutCode(sessionUser.getToken(), realLogoutCode);
                                } else {
                                    webKeepLoginClient--;
                                }
                            }
                        }
                    }
                } else {
                    // 不同的设备类型，是否允许同时登录
                    if (!Boolean.TRUE.equals(portalConfig.getAllowWebMobileSameLogin())) {
                        userAuthTokenService.deleteTokenOnly(sessionUser.getToken(), PortalTypeEnum.PORTAL);
                        portalRedisSessionManager.setLogoutCode(sessionUser.getToken(), realLogoutCode);
                    }
                }
            }
        }

        // 处理数据库
        List<UserAuthToken> authTokenList = userAuthTokenService.listByUserId(user.getId(), PortalTypeEnum.PORTAL, excludeToken);
        if (CollectionUtil.isNotEmpty(authTokenList)) {

            // 最迟登录的在最前面
            Comparator<UserAuthToken> reversed = Comparator.comparing(UserAuthToken::getCreateTime).reversed();
            authTokenList = CollectionUtil.sort(authTokenList, reversed);

            for (UserAuthToken dbToken : authTokenList) {
                if (dbToken.getToken().equals(excludeToken)) {
                    continue;
                }
                if (redisTokenSet.contains(dbToken.getToken())) {
                    continue;
                }

                JwtInfo dbTokenInfo = JwtUtil.getJwtInfo(dbToken.getToken());
                DeviceTypeEnum dbTokenDevice = EnumUtil.valueOfIEnum(DeviceTypeEnum.class, dbTokenInfo.getDeviceType());

                if (DeviceTypeEnum.isSameTypeDevice(tokenDevice, dbTokenDevice)) {

                    // 相同的设备类型，看每个类型允许登录数
                    {
                        if (DeviceTypeEnum.isMobile(tokenDevice) && DeviceTypeEnum.isMobile(dbTokenDevice)) {
                            if (NumberUtil.isGreatThenZero(portalConfig.getMobileLoginMaxClient())) {

                                if (mobileKeepLoginClient <= 0) {
                                    userAuthTokenService.deleteTokenOnly(dbToken.getToken(), PortalTypeEnum.PORTAL);
                                    portalRedisSessionManager.setLogoutCode(dbToken.getToken(), realLogoutCode);
                                } else {
                                    mobileKeepLoginClient--;
                                }
                            }
                        }
                        if (DeviceTypeEnum.isWeb(tokenDevice) && DeviceTypeEnum.isWeb(dbTokenDevice)) {

                            if (NumberUtil.isGreatThenZero(portalConfig.getWebLoginMaxClient())) {

                                if (webKeepLoginClient <= 0) {
                                    userAuthTokenService.deleteTokenOnly(dbToken.getToken(), PortalTypeEnum.PORTAL);
                                    portalRedisSessionManager.setLogoutCode(dbToken.getToken(), realLogoutCode);
                                } else {
                                    webKeepLoginClient--;
                                }
                            }
                        }
                    }
                } else {
                    // 不同的设备类型，是否允许同时登录
                    if (!Boolean.TRUE.equals(portalConfig.getAllowWebMobileSameLogin())) {
                        userAuthTokenService.deleteTokenOnly(dbToken.getToken(), PortalTypeEnum.PORTAL);
                        portalRedisSessionManager.setLogoutCode(dbToken.getToken(), realLogoutCode);
                    }
                }
            }
        }


        // // 当logoutCode参数为空时设置默认值
        // ResponseCode realLogoutCode = Optional.ofNullable(logoutCode).orElse(ResponseCode.USER_SESSION_INVALIDATE);
        //
        // // 把所有redis会话拿出来，仅保留该token
        // Set<String> tokenSet = portalRedisSessionManager.listAllTokenByUsername(user.getUsername());
        // if (CollectionUtil.isNotEmpty(tokenSet)) {
        //     for (String token : tokenSet) {
        //         if (token.equalsIgnoreCase(excludeToken)) {
        //             continue;
        //         }
        //         userAuthTokenService.deleteTokenOnly(token, PortalTypeEnum.PORTAL);
        //         portalRedisSessionManager.setLogoutCode(token, realLogoutCode);
        //     }
        // }
        //
        // // 获取这个用户的所有token，仅保留该token
        // List<UserAuthToken> authTokenList = userAuthTokenService.listByUserId(user.getId(), PortalTypeEnum.PORTAL, excludeToken);
        // if (CollectionUtil.isNotEmpty(authTokenList)) {
        //     for (UserAuthToken authToken : authTokenList) {
        //         // 删除token
        //         userAuthTokenService.removeById(authToken.getId());
        //
        //         // 会话中设置登出消息
        //         portalRedisSessionManager.setLogoutCode(authToken.getToken(), realLogoutCode);
        //     }
        // }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void kickOutAllLoginClient(long userId, ResponseCode logoutCode) {
        String username = UserUtil.getUsernameByIdFromLocal(userId, PortalTypeEnum.PORTAL);
        // 当logoutCode参数为空时设置默认值
        ResponseCode realLogoutCode = Optional.ofNullable(logoutCode).orElse(ResponseCode.USER_SESSION_INVALIDATE);

        // 把所有redis会话设置为登出
        Set<String> tokenSet = portalRedisSessionManager.listAllTokenByUsername(username);
        if (CollectionUtil.isNotEmpty(tokenSet)) {
            for (String token : tokenSet) {
                portalRedisSessionManager.setLogoutCode(token, realLogoutCode);
            }
        }

        // 把所有数据库会话设置为登出
        List<UserAuthToken> authTokenList = userAuthTokenService.listByUserId(userId, PortalTypeEnum.PORTAL);
        if (CollectionUtil.isNotEmpty(authTokenList)) {
            for (UserAuthToken authToken : authTokenList) {
                portalRedisSessionManager.setLogoutCode(authToken.getToken(), realLogoutCode);
            }
        }

        // 删除所有token
        userAuthTokenService.deleteUserTokenOnly(userId, PortalTypeEnum.PORTAL);

        // IM下线
        tiAccountService.kick(username);
    }

    // private void updateUserOffline(long userId, boolean kickOutIm) {
    //     // lambdaUpdate().eq(PortalUser::getId, userId).set(PortalUser::getOnline, false).update();
    //
    //     // 向IM踢出用户
    //     if (kickOutIm) {
    //         String username = UserUtil.getUsernameByIdFromLocal(userId, PortalTypeEnum.PORTAL);
    //         tiAccountService.kick(username);
    //     }
    // }

    @Override
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public boolean isExists(String username) {
        Integer count = lambdaQuery()
                .eq(PortalUser::getUsername, StrUtil.trim(username)).count();
        return NumberUtil.isGreatThenZero(count);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResponse editLoginPwdForPortal(@NonNull PortalSessionUser sessionUser, @NonNull UserEditPwdParam param) {
        // 检测旧密码是否匹配
        PortalUser user = getById(sessionUser.getId());
        RestResponse rsp = PasswordUtil.validEditLoginPwd(user.getLoginPwd(), param.getOldPwd(), param.getNewPwd());
        if (!rsp.isOkRsp()) {
            return rsp;
        }

        boolean updated = lambdaUpdate()
                .eq(PortalUser::getId, user.getId())
                .set(PortalUser::getLoginPwd, PasswordUtil.generateDbPwdFromMd5String(param.getNewPwd()))
                .update();
        if (!updated) {
            return RestResponse.SYS_DATA_STATUS_ERROR;
        }

        // 要求重新登录
        kickOutAllLoginClient(user.getId(), ResponseCode.USER_SESSION_INFO_CHANGED);

        return RestResponse.OK;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResponse bindFundPwdForPortal(@NonNull PortalSessionUser sessionUser, @NonNull UserBindPwdParam param) {
        // 检测旧密码是否已经绑定
        PortalUser user = getById(sessionUser.getId());
        RestResponse rsp = PasswordUtil.validBindFundPwd(user.getFundPwd(), user.getLoginPwd(), param.getPassword());
        if (!rsp.isOkRsp()) {
            return rsp;
        }

        boolean updated = lambdaUpdate()
                .eq(PortalUser::getId, user.getId())
                .set(PortalUser::getFundPwd, PasswordUtil.generateDbPwdFromMd5String(param.getPassword()))
                .update();
        if (!updated) {
            return RestResponse.SYS_DATA_STATUS_ERROR;
        }

        return RestResponse.OK;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResponse editFundPwdForPortal(@NonNull PortalSessionUser sessionUser, @NonNull UserEditPwdParam param) {
        // 检测旧密码是否匹配
        PortalUser user = getById(sessionUser.getId());
        RestResponse rsp = PasswordUtil.validEditFundPwd(user.getFundPwd(), user.getLoginPwd(),
                param.getOldPwd(), param.getNewPwd());
        if (!rsp.isOkRsp()) {
            return rsp;
        }

        boolean updated = lambdaUpdate()
                .eq(PortalUser::getId, user.getId())
                .set(PortalUser::getFundPwd, PasswordUtil.generateDbPwdFromMd5String(param.getNewPwd()))
                .update();
        if (!updated) {
            return RestResponse.SYS_DATA_STATUS_ERROR;
        }

        // 要求重新登录
        kickOutAllLoginClient(user.getId(), ResponseCode.USER_SESSION_INFO_CHANGED);

        return RestResponse.OK;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResponse bindWithdrawNameForPortal(PortalSessionUser sessionUser, UserBindWithdrawNameParam param) {
        PortalUser user = getById(sessionUser.getId());
        if (StrUtil.isNotBlank(user.getWithdrawName())) {
            return RestResponse.failed(ResponseCode.USER_WITHDRAW_NAME_ALREADY_BOUND);
        }

        // 必须绑定资金密码
        if (StrUtil.isBlank(user.getFundPwd())) {
            return RestResponse.failed(ResponseCode.USER_FUND_PASSWORD_NOT_YET_BIND);
        }

        // 验证资金密码
        if (!PasswordUtil.validatePwd(user.getFundPwd(), param.getFundPwd())) {
            return RestResponse.failed(ResponseCode.USER_FUND_PASSWORD_INCORRECT);
        }

        // 姓名必须合法
        if (!ReUtil.isMatch(RegexConstant.CHINESE_NAME_REGEX, param.getWithdrawName())) {
            return RestResponse.failed(ResponseCode.USER_WITHDRAW_NAME_MUST_BE_CHINESE);
        }

        LambdaUpdateChainWrapper<PortalUser> wrapper = lambdaUpdate()
                .eq(PortalUser::getId, user.getId())
                .set(PortalUser::getWithdrawName, param.getWithdrawName());

        boolean updated = wrapper.update();
        if (!updated) {
            return RestResponse.SYS_DATA_STATUS_ERROR;
        }

        cacheProxy.signalRefreshCache(SysCacheRefreshTypeEnum.PORTAL_USER);

        return RestResponse.OK;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PortalUserAdminVO getVOForAdmin(UsernameParam param) {
        PortalUserPageAdminParam pageParam = new PortalUserPageAdminParam();
        pageParam.setUsername(param.getUsername());
        List<PortalUserAdminVO> list = portalUserMapper.pageVOForAdmin(pageParam.toPage(), pageParam);

        // 组装额外数据
        combineUserExtraDataForAdmin(list);

        return CollectionUtil.isEmpty(list) ? null : list.get(0);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PageVO<PortalUserAdminVO> pageVOForAdmin(PortalUserPageAdminParam param) {
        // 查询
        Page<PortalUser> page = param.toPage();
        List<PortalUserAdminVO> list = portalUserMapper.pageVOForAdmin(page, param);

        if (CollectionUtil.isEmpty(list)) {
            return new PageVO<>(param);
        }

        // 组装额外数据
        combineUserExtraDataForAdmin(list);


        // 手动封装分页
        PageVO<PortalUserAdminVO> pageVO = new PageVO<>(page, list);
        return pageVO;
    }

    private void combineUserExtraDataForAdmin(List<PortalUserAdminVO> userVOs) {
        Set<Long> userIds = CollectionUtil.toSet(userVOs, PortalUserAdminVO::getId);
        if (CollectionUtil.isEmpty(userIds)) {
            return;
        }

        // 查询所有人的用户组数据
        List<UserGroupUser> allUserGroups = userGroupUserService
                .lambdaQuery()
                .in(UserGroupUser::getUserId, userIds)
                .list();
        Map<Long, List<UserGroupUser>> userGroupMap = CollectionUtil.toMapList(allUserGroups, UserGroupUser::getUserId);

        // 组装数据、
        userVOs.forEach(userVo -> {
            userVo.setFundPwdBound(StrUtil.isNotBlank(userVo.getFundPwd()));
            userVo.setFundPwd(null);

            Long userId = userVo.getId();

            // 用户组
            List<UserGroupUser> userGroups = userGroupMap.get(userId);
            if (CollectionUtil.isNotEmpty(userGroups)) {
                List<UserGroupSimpleAdminVO> vos = CollectionUtil.toList(userGroups, e -> {
                    String groupName = userGroupCache.getNameByIdFromLocal(e.getGroupId());
                    return new UserGroupSimpleAdminVO(e.getGroupId(), groupName);
                });
                userVo.setUserGroups(vos);
            }

            // 空值
            userVo.patchEmpty();
        });

        //查询在线状态
        Set<String> usernames = CollectionUtil.toSet(userVOs, PortalUserAdminVO::getUsername);
        RestResponse<Map<String, Boolean>> response = tiAccountService.queryOnlineStatus(new ArrayList<>(usernames));
        if(response.isOkRsp()){
            Map<String, Boolean> data = response.getData();
            for (PortalUserAdminVO userVO : userVOs) {
                userVO.setOnline(Boolean.TRUE.equals(data.get(userVO.getUsername())));
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResponse addForAdmin(PortalUserAddAdminParam param) {
        // 检查用户名是否存在
        String realUsername = StrUtil.cleanBlank(param.getUsername());
        {
            boolean exists = isExists(realUsername);
            if (exists) {
                return RestResponse.failed(ResponseCode.USER_USERNAME_EXISTED);
            }
        }

        // 检查默认好友是否存在
        Set<String> defaultFriendSet = new HashSet<>();
        {
            if (StrUtil.isNotBlank(param.getDefaultFriends())) {
                Set<String> defaultFriends = CollectionUtil.splitStrToSet(param.getDefaultFriends(), CommonConstant.DOT_EN);
                for (String defaultFriend : defaultFriends) {
                    Long friendId = UserUtil.getUserIdByUsernameFromLocal(defaultFriend, PortalTypeEnum.PORTAL);
                    if (friendId == null) {
                        return RestResponse.failed(ResponseCode.USER_USERNAME_NOT_EXISTED, defaultFriend);
                    }
                    defaultFriendSet.add(UserUtil.getUsernameByIdFromLocal(friendId, PortalTypeEnum.PORTAL));
                }
            }
        }

        String nickname = StrUtil.blankToDefault(StrUtil.trim(param.getNickname()), realUsername);

        PortalUser user = new PortalUser();
        user.setUsername(realUsername);
        user.setNickname(nickname);
        user.setAvatar(StrUtil.isBlank(param.getAvatar()) ? null : param.getAvatar());
        user.setLoginPwd(PasswordUtil.generateDbPwdFromMd5String(param.getLoginPwd()));
        user.setLoginEnabled(param.getLoginEnabled());
        user.setRechargeEnabled(param.getRechargeEnabled());
        user.setWithdrawEnabled(param.getWithdrawEnabled());
        user.setAddFriendEnabled(param.getAddFriendEnabled());
        user.setEnabled(param.getEnabled());
        // user.setOnline(false);

        // 保存用户
        boolean save = save(user);
        if (!save) {
            return RestResponse.failed(ResponseCode.SYS_DATA_STATUS_ERROR);
        }

        // 创建资料
        PortalUserProfile profile = new PortalUserProfile();
        profile.setId(user.getId());
        profile.setSex(param.getSex());
        profile.setBirthday(param.getBirthday());
        profile.setSelfSignature(StrUtil.trim(param.getSelfSignature()));
        profile.setRegisterDevice(DeviceTypeEnum.PC);
        profile.setRegisterType(RegisterTypeEnum.ADMIN_CREATE);
        profile.setAdminRemark(StrUtil.trim(param.getAdminRemark()));
        portalUserProfileService.createUserProfile(profile);

        // 用户组
        userGroupUserService.adjustUserGroup(user.getId(), param.getUserGroupIds());

        // todo 报表，注册人数
        // ReportConfigBO reportConfig = sysConfigCache.getReportConfigFromRedis();
        // String reportDate = DateTimeUtil.getOffsetDateStrByDateTime(user.getCreateTime(), reportConfig.getOffsetTime());
        // reportService.addRegisterCount(user.getId(), user.getRealType(), reportDate);

        // 向腾讯IM注册
        {
            RestResponse rsp = tiAccountService.accountImport(user.getUsername());
            if (!rsp.isOkRsp()) {
                // 创建不成功，回滚数据
                throw new ImException(rsp);
            }
        }

        // 向腾讯IM设置资料
        {
            TiAccountPortraitParam portrait = new TiAccountPortraitParam();
            portrait.setUsername(user.getUsername());
            portrait.setNickname(user.getNickname());
            portrait.setAvatar(user.getAvatar());
            portrait.setSex(profile.getSex());
            portrait.setBirthday(profile.getBirthday());
            portrait.setSelfSignature(profile.getSelfSignature());
            portrait.setAddFriendEnabled(user.getAddFriendEnabled());
            portrait.setEnabled(user.getEnabled());
            tiAccountPortraitService.setPortrait(portrait);
        }

        // 加默认好友列表
        {
            if (CollectionUtil.isNotEmpty(defaultFriendSet)) {
                List<TiFriendAddParam.AddFriendItemDTO> friendList = CollectionUtil.toList(defaultFriendSet, friend -> new TiFriendAddParam.AddFriendItemDTO(friend, AddSourceTypeEnum.RESTAPI));
                TiFriendAddParam tiFriendAddParam = new TiFriendAddParam(user.getUsername(), friendList);
                tiFriendService.addFriend(tiFriendAddParam);
            }
        }

        cacheProxy.signalRefreshCache(SysCacheRefreshTypeEnum.PORTAL_USER);

        return RestResponse.OK;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResponse editForAdmin(PortalUserEditAdminParam param) {
        PortalUser user;
        {
            // 检查基本参数
            user = lambdaQuery().eq(PortalUser::getUsername, param.getUsername()).one();
            if (user == null) {
                return RestResponse.failed(ResponseCode.USER_NOT_FOUND);
            }
        }

        String nickname = StrUtil.blankToDefault(StrUtil.trim(param.getNickname()), user.getUsername());

        LambdaUpdateChainWrapper<PortalUser> wrapper = lambdaUpdate()
                .eq(PortalUser::getId, user.getId())
                .set(PortalUser::getNickname, nickname)
                .set(PortalUser::getAvatar, StrUtil.isBlank(param.getAvatar()) ? null : param.getAvatar())
                .set(PortalUser::getLoginEnabled, param.getLoginEnabled())
                .set(PortalUser::getRechargeEnabled, param.getRechargeEnabled())
                .set(PortalUser::getWithdrawEnabled, param.getWithdrawEnabled())
                .set(PortalUser::getAddFriendEnabled, param.getAddFriendEnabled())
                .set(PortalUser::getEnabled, param.getEnabled());

        if (StrUtil.isNotBlank(param.getLoginPwd())) {
            wrapper.set(PortalUser::getLoginPwd, PasswordUtil.generateDbPwdFromMd5String(param.getLoginPwd()));
        }

        boolean updated = wrapper.update();

        // 保存用户
        if (!updated) {
            return RestResponse.SYS_DATA_STATUS_ERROR;
        }

        portalUserProfileService
                .lambdaUpdate()
                .eq(PortalUserProfile::getId, user.getId())
                .set(PortalUserProfile::getSex, param.getSex())
                .set(PortalUserProfile::getBirthday, param.getBirthday())
                .set(PortalUserProfile::getSelfSignature, StrUtil.trim(param.getSelfSignature()))
                .set(PortalUserProfile::getAdminRemark, StrUtil.trim(param.getAdminRemark()))
                .update();

        // 用户组
        userGroupUserService.adjustUserGroup(user.getId(), param.getUserGroupIds());

        // 修改腾讯IM资料
        {
            TiAccountPortraitParam portrait = new TiAccountPortraitParam();
            portrait.setUsername(user.getUsername());
            portrait.setNickname(nickname);
            portrait.setAvatar(StrUtil.isBlank(param.getAvatar()) ? null : param.getAvatar());
            portrait.setSex(param.getSex());
            portrait.setBirthday(param.getBirthday());
            portrait.setSelfSignature(StrUtil.trim(param.getSelfSignature()));
            portrait.setAddFriendEnabled(param.getAddFriendEnabled());
            portrait.setEnabled(param.getEnabled());
            tiAccountPortraitService.setPortrait(portrait);
        }

        // 踢下线
        kickOutAllLoginClient(user.getId(), ResponseCode.USER_SESSION_INFO_CHANGED);

        cacheProxy.signalRefreshCache(SysCacheRefreshTypeEnum.PORTAL_USER);

        return RestResponse.OK;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResponse enableDisableLoginForAdmin(UsernameEnableDisableParam param) {
        boolean updated = lambdaUpdate()
                .eq(PortalUser::getUsername, param.getUsername())
                .set(PortalUser::getLoginEnabled, param.getEnable())
                .update();
        if (updated) {
            kickOutAllLoginClient(param.getUsername(), ResponseCode.USER_SESSION_INFO_CHANGED);

            return RestResponse.OK;
        } else {
            return RestResponse.SYS_DATA_STATUS_ERROR;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResponse enableDisableAddFriendForAdmin(UsernameEnableDisableParam param) {
        boolean updated = lambdaUpdate()
                .eq(PortalUser::getUsername, param.getUsername())
                .set(PortalUser::getAddFriendEnabled, param.getEnable())
                .update();
        if (updated) {
            // 向腾讯IM设置资料
            {
                tiAccountPortraitService.enableDisableAddFriend(param.getUsername(), param.getEnable());
            }

            // 踢下线
            kickOutAllLoginClient(param.getUsername(), ResponseCode.USER_SESSION_INFO_CHANGED);

            return RestResponse.OK;
        } else {
            return RestResponse.SYS_DATA_STATUS_ERROR;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResponse enableDisableRechargeForAdmin(UsernameEnableDisableParam param) {
        boolean updated = lambdaUpdate()
                .eq(PortalUser::getUsername, param.getUsername())
                .set(PortalUser::getRechargeEnabled, param.getEnable())
                .update();
        if (updated) {
            return RestResponse.OK;
        } else {
            return RestResponse.SYS_DATA_STATUS_ERROR;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResponse enableDisableWithdrawForAdmin(UsernameEnableDisableParam param) {
        boolean updated = lambdaUpdate()
                .eq(PortalUser::getUsername, param.getUsername())
                .set(PortalUser::getWithdrawEnabled, param.getEnable())
                .update();
        if (updated) {
            return RestResponse.OK;
        } else {
            return RestResponse.SYS_DATA_STATUS_ERROR;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResponse enableDisableForAdmin(UsernameEnableDisableParam param) {
        boolean updated = lambdaUpdate()
                .eq(PortalUser::getUsername, param.getUsername())
                .set(PortalUser::getEnabled, param.getEnable())
                .update();
        if (updated) {
            // 成功后踢出用户
            kickOutAllLoginClient(param.getUsername(), ResponseCode.USER_SESSION_INFO_CHANGED);
            // 向腾讯IM设置资料角色为禁用角色
            {
                tiAccountPortraitService.enableDisable(param.getUsername(), param.getEnable());
            }
            Long userId = portalUserCache.getIdByUsernameFromLocal(param.getUsername());
            //删除单聊记录（包含本地和腾讯IM）
            timMessageC2cService.deleteUserHistoryMessage(userId);

            //删除群聊记录（包含本地和腾讯IM）
            timMessageGroupService.deleteUserHistoryMessage(userId);

            return RestResponse.OK;
        } else {
            return RestResponse.SYS_DATA_STATUS_ERROR;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResponse resetFundPwdForAdmin(AdminSessionUser sessionUser, UsernameGoogleCodeParam param) {
        // 检查谷歌
        {
            RestResponse googleRsp = googleAuthService.authoriseGoogle(sessionUser.getUsername(), PortalTypeEnum.ADMIN, param.getGoogleCode());
            if (!googleRsp.isOkRsp()) {
                return googleRsp;
            }
        }

        boolean updated = lambdaUpdate()
                .eq(PortalUser::getUsername, param.getUsername())
                .set(PortalUser::getFundPwd, null)
                .update();
        if (updated) {
            // 成功后踢出用户
            kickOutAllLoginClient(param.getUsername(), ResponseCode.USER_SESSION_INFO_CHANGED);
            return RestResponse.OK;
        } else {
            return RestResponse.SYS_DATA_STATUS_ERROR;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResponse editWithdrawNameForAdmin(AdminSessionUser sessionUser, PortalUserEditWithdrawNameAdminParam param) {
        // 检查谷歌
        {
            RestResponse googleRsp = googleAuthService.authoriseGoogle(sessionUser.getUsername(), PortalTypeEnum.ADMIN, param.getGoogleCode());
            if (!googleRsp.isOkRsp()) {
                return googleRsp;
            }
        }

        boolean updated = lambdaUpdate()
                .eq(PortalUser::getUsername, param.getUsername())
                .set(PortalUser::getWithdrawName, StrUtil.trim(param.getWithdrawName()))
                .update();
        if (updated) {
            // 成功后踢出用户
            kickOutAllLoginClient(param.getUsername(), ResponseCode.USER_SESSION_INFO_CHANGED);
            return RestResponse.OK;
        } else {
            return RestResponse.SYS_DATA_STATUS_ERROR;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResponse editMyInviteCodeForAdmin(AdminSessionUser sessionUser, PortalUserEditMyInviteCodeAdminParam param) {
        Long userId = UserUtil.getUserIdByUsernameFromLocal(param.getUsername(), PortalTypeEnum.PORTAL);
        if (userId == null) {
            return RestResponse.failed(ResponseCode.USER_NOT_FOUND, param.getUsername());
        }

        // 检查重复
        Integer count = portalUserProfileService
                .lambdaQuery()
                .eq(PortalUserProfile::getMyInviteCode, param.getMyInviteCode())
                .ne(PortalUserProfile::getId, userId)
                .count();
        if (NumberUtil.isGreatThenZero(count)) {
            return RestResponse.failed(ResponseCode.SYS_REQUEST_REPEAT);
        }

        portalUserProfileService
                .lambdaUpdate()
                .eq(PortalUserProfile::getId, userId)
                .set(PortalUserProfile::getMyInviteCode, param.getMyInviteCode())
                .update();
        return RestResponse.OK;
    }

    @Override
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public BigDecimal getBalance(long userId) {
        PortalUser one = lambdaQuery()
                .select(PortalUser::getBalance)
                .eq(PortalUser::getId, userId)
                .one();
        return one == null ? BigDecimal.ZERO : one.getBalance();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResponse adminAddBalance(AdminSessionUser sessionUser, UserAddBalanceAdminParam param) {
        // 检查谷歌
        {
            RestResponse googleRsp = googleAuthService.authoriseGoogle(sessionUser.getUsername(), PortalTypeEnum.ADMIN, param.getGoogleCode());
            if (!googleRsp.isOkRsp()) {
                return googleRsp;
            }
        }

        Long userId = UserUtil.getUserIdByUsernameFromLocal(param.getUsername(), PortalTypeEnum.PORTAL);
        if (userId == null) {
            return RestResponse.failed(ResponseCode.USER_NOT_FOUND);
        }

        // 增减余额
        LocalDateTime now = LocalDateTime.now();
        boolean updated = portalUserMapper.addBalance(userId, param.getAmount(), true);
        if (!updated) {
            return RestResponse.failed(ResponseCode.USER_INSUFFICIENT_BALANCE);
        }
        BigDecimal balance = getBalance(userId);

        // 新增账变
        ReportConfigBO reportConfig = sysConfigCache.getReportConfigFromRedis();
        String reportDate = DateTimeUtil.getOffsetDateStrByDateTime(now, reportConfig.getOffsetTime());

        userBillService.addBalanceBill(userId, param.getAmount(), balance,
                param.getOrderNum(), UserBillTypeEnum.ADMIN_ADD_BALANCE, now, reportDate, param.getRemark());

        return RestResponse.OK;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResponse addBalanceWithReportDate(long userId, BigDecimal amount, String orderNum, UserBillTypeEnum billType, String billRemark, String billReportDate, boolean allowToNegate) {
        if (NumberUtil.isEquals(amount, BigDecimal.ZERO)) {
            return RestResponse.failed(ResponseCode.SYS_REQUEST_PARAM_ERROR);
        }

        String username = UserUtil.getUsernameByIdFromLocal(userId, PortalTypeEnum.PORTAL);
        if (StrUtil.isBlank(username)) {
            return RestResponse.failed(ResponseCode.USER_NOT_FOUND);
        }

        LocalDateTime now = LocalDateTime.now();
        boolean updated = portalUserMapper.addBalance(userId, amount, allowToNegate);
        if (!updated) {
            return RestResponse.failed(ResponseCode.USER_INSUFFICIENT_BALANCE);
        }

        // 增加账单
        if (billType != null) {
            BigDecimal balance = getBalance(userId);
            userBillService.addBalanceBill(userId, amount, balance, orderNum, billType, now, billReportDate, billRemark);
        }

        return RestResponse.OK;
    }

    @Override
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public List<PortalUser> listBalanceSnapshot() {
        return lambdaQuery().select(PortalUser::getId, PortalUser::getBalance).list();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResponse registerForPortal(PortalUserRegisterParam param) {
        // 注册开关必须打开
        RegisterConfigBO registerConfig = sysConfigCache.getRegisterConfigFromRedis();
        if (!Boolean.TRUE.equals(registerConfig.getEnabled())) {
            return RestResponse.failed(ResponseCode.USER_REGISTER_DISABLED);
        }

        // 每IP每天最大允许注册人数
        String today = DateTimeUtil.getNowDateStr();
        RestResponse dailyRsp = checkDailyLimitCount(registerConfig, param.getIp(), today);
        if (!dailyRsp.isOkRsp()) {
            return dailyRsp;
        }

        // 检查参数，并返回代理ID和返点
        RestResponse<Object[]> paramRsp = checkRegisterParam(registerConfig, param);
        if (!paramRsp.isOkRsp()) {
            return paramRsp;
        }

        String username = StrUtil.cleanBlank(param.getUsername());
        String nickname = StrUtil.blankToDefault(StrUtil.trim(param.getNickname()), username);

        // 存数据
        PortalUser user = new PortalUser();
        user.setUsername(username);
        SmsTemplateConfig smsTemplate = smsTemplateConfigCache.getByCodeFromLocal(param.getMobileCountryCode());
        user.setMobilePrefix(smsTemplate != null && StrUtil.isNotBlank(param.getMobile()) ? smsTemplate.getPrefix() : null);
        user.setMobile(StrUtil.isBlank(param.getMobile()) ? null : StrUtil.cleanBlank(param.getMobile()));
        user.setNickname(nickname);
        user.setAvatar(StrUtil.isBlank(param.getAvatar()) ? null : StrUtil.cleanBlank(param.getAvatar()));
        user.setLoginPwd(PasswordUtil.generateDbPwdFromMd5String(param.getLoginPwd()));
        if (StrUtil.isNotBlank(param.getFundPwd())) {
            user.setFundPwd(PasswordUtil.generateDbPwdFromMd5String(param.getFundPwd()));
        }
        user.setBalance(BigDecimal.ZERO);
        // user.setOnline(false);
        user.setLoginEnabled(true);
        user.setRechargeEnabled(true);
        user.setWithdrawEnabled(true);
        user.setAddFriendEnabled(registerConfig.getAddFriendEnabled());

        // 保存用户
        boolean save = save(user);
        if (!save) {
            return RestResponse.failed(ResponseCode.SYS_DATA_STATUS_ERROR);
        }

        LocalDate birthday = null;
        if (DateTimeUtil.isDate(param.getBirthday())) {
            birthday = DateTimeUtil.fromDateStrToLocalDate(param.getBirthday());
        }

        // 创建资料
        PortalUserProfile profile = new PortalUserProfile();
        profile.setId(user.getId());
        profile.setRegisterDevice(param.getRegisterDevice());
        profile.setRegisterType(Boolean.TRUE.equals(registerConfig.getInviteCodeRequired()) ? RegisterTypeEnum.INVITE_CODE_REGISTER : RegisterTypeEnum.HOME_REGISTER);
        profile.setRegisterInviteCode(Boolean.TRUE.equals(registerConfig.getInviteCodeRequired()) ? param.getInviteCode() : null);
        profile.setSex(param.getSex());
        profile.setBirthday(birthday);
        profile.setDeviceUnique(param.getDeviceUnique());
        portalUserProfileService.createUserProfile(profile);

        // todo 报表，注册人数
        // ReportConfigBO reportConfig = sysConfigCache.getReportConfigFromRedis();
        // String reportDate = DateTimeUtil.getOffsetDateStrByDateTime(user.getCreateTime(), reportConfig.getOffsetTime());
        // reportService.addRegisterCount(user.getId(), user.getRealType(), reportDate);

        // 向腾讯IM注册
        {
            RestResponse rsp = tiAccountService.accountImport(user.getUsername());
            if (!rsp.isOkRsp()) {
                // 创建不成功，回滚数据
                throw new ImException(rsp);
            }
        }

        // 向腾讯IM设置资料
        {
            TiAccountPortraitParam portrait = new TiAccountPortraitParam();
            portrait.setUsername(user.getUsername());
            portrait.setNickname(user.getNickname());
            portrait.setAvatar(user.getAvatar());
            portrait.setSex(profile.getSex());
            portrait.setBirthday(profile.getBirthday());
            portrait.setSelfSignature(profile.getSelfSignature());
            portrait.setAddFriendEnabled(user.getAddFriendEnabled());
            portrait.setEnabled(user.getEnabled());
            tiAccountPortraitService.setPortrait(portrait);
        }

        // 绑定两个用户为好友
        {
            // 邀请码是否启用而且用户填写了邀请码，前面已经验证了是否有效
            if (Boolean.TRUE.equals(registerConfig.getInviteCodeRequired()) && StrUtil.isNotBlank(param.getInviteCode())) {
                PortalUserProfile inviteCodeUserProfile = portalUserProfileService.lambdaQuery().eq(PortalUserProfile::getMyInviteCode, param.getInviteCode()).one();
                String toAccount = portalUserCache.getUsernameByIdFromLocal(inviteCodeUserProfile.getId());
                TiFriendAddParam tiFriendAddParam = new TiFriendAddParam(username,
                        ListUtil.of(new TiFriendAddParam.AddFriendItemDTO(toAccount, AddSourceTypeEnum.INVITE_CODE)));
                tiFriendService.addFriend(tiFriendAddParam);
                // 向对方发一条消息
                TiMsgBody tiMsgBody = new TiMsgBody();
                tiMsgBody.setMsgType(TiMsgTypeEnum.TIMTextElem);
                String message = I18nTranslateUtil.translate("RSP_MSG.ADD_FRIEND_SUCCESS_MESSAGE#I18N");
                tiMsgBody.setMsgContent(new TIMTextElem(message));
                TiMessage tiMessage = new TiMessage(username, toAccount, RandomUtil.randomInt(100000), ListUtil.of(tiMsgBody));
                tiSingleChatService.sendMessage(tiMessage);
            }
        }

        // 增加IP今日注册数
        incrementDailyLimitCount(param.getIp(), today);

        // 刷新缓存
        cacheProxy.signalRefreshCache(SysCacheRefreshTypeEnum.PORTAL_USER);
        return RestResponse.OK;
    }

    /**
     * 检查注册参数
     *
     * @param registerConfig 注册配置
     * @param param          不需要输入的数据会被清空
     * @return
     */
    private RestResponse checkRegisterParam(RegisterConfigBO registerConfig, PortalUserRegisterParam param) {
        // 用户名是否合法
        String realUsername = StrUtil.cleanBlank(param.getUsername());
        if (StrUtil.isBlank(realUsername)) {
            return RestResponse.failed(ResponseCode.SYS_REQUEST_PARAM_ERROR_WITH_MESSAGE, RegexConstant.USERNAME_REGEX_REMARK);
        }
        if (!ReUtil.isMatch(RegexConstant.USERNAME_REGEX, realUsername)) {
            return RestResponse.failed(ResponseCode.SYS_REQUEST_PARAM_ERROR_WITH_MESSAGE, RegexConstant.USERNAME_REGEX_REMARK);
        }
        param.setUsername(realUsername);

        // 用户名是否存在
        Integer count = lambdaQuery().eq(PortalUser::getUsername, realUsername).count();
        if (NumberUtil.isGreatThenZero(count)) {
            return RestResponse.failed(ResponseCode.USER_USERNAME_EXISTED);
        }

        // 头像
        if (Boolean.TRUE.equals(registerConfig.getAvatarRequired()) && StrUtil.isBlank(param.getAvatar())) {
            return RestResponse.failed(ResponseCode.USER_REGISTER_AVATAR_REQUIRED);
        }

        // 检查邀请码
        if (Boolean.TRUE.equals(registerConfig.getInviteCodeRequired()) && StrUtil.isBlank(param.getInviteCode())) {
            return RestResponse.failed(ResponseCode.USER_REGISTER_INVITE_CODE_REQUIRED);
        }
        if (Boolean.TRUE.equals(registerConfig.getInviteCodeRequired()) && StrUtil.isNotBlank(param.getInviteCode())) {
            // 邀请码是否启用
            PortalUserProfile profile = portalUserProfileService.lambdaQuery().eq(PortalUserProfile::getMyInviteCode, param.getInviteCode()).one();
            if (profile == null) {
                return RestResponse.failed(ResponseCode.USER_REGISTER_INVITE_CODE_NOT_FOUND);
            }
            PortalUser user = lambdaQuery().eq(PortalUser::getId, profile.getId()).one();
            if (!Boolean.TRUE.equals(user.getLoginEnabled())) {
                return RestResponse.failed(ResponseCode.USER_REGISTER_INVITE_CODE_DISABLED);
            }
        }

        // 检查手机
        if (Boolean.TRUE.equals(registerConfig.getMobileRequired())) {
            // if (StrUtil.isBlank(param.getMobileCountryCode()) || StrUtil.isBlank(param.getMobile())) {
            //     return RestResponse.failed(ResponseCode.USER_REGISTER_MOBILE_REQUIRED);
            // }
            //
            // SmsTemplateConfig smsTemplate = smsTemplateConfigCache.getByCodeFromLocal(param.getMobileCountryCode());
            // if (smsTemplate == null) {
            //     return RestResponse.failed(ResponseCode.SMS_COUNTRY_NOT_SUPPORT);
            // }
            //
            // // 不能重复
            // Integer mobileCount = lambdaQuery().eq(PortalUser::getMobile, StrUtil.cleanBlank(param.getMobile())).count();
            // if (NumberUtil.isGreatThenZero(mobileCount)) {
            //     return RestResponse.failed(ResponseCode.USER_REGISTER_MOBILE_REPEAT);
            // }
        }
        // dontknowwhy
        if (StrUtil.isNotBlank(param.getMobile())) {
            // 不能重复
            Integer mobileCount = lambdaQuery().eq(PortalUser::getMobile, StrUtil.cleanBlank(param.getMobile())).count();
            if (NumberUtil.isGreatThenZero(mobileCount)) {
                return RestResponse.failed(ResponseCode.USER_REGISTER_MOBILE_REPEAT);
            }
        }

        // 检查手机验证码
        if (Boolean.TRUE.equals(registerConfig.getMobileRequired()) && Boolean.TRUE.equals(registerConfig.getMobileVerificationCodeRequired())) {
            // // 必填
            // if (StrUtil.isBlank(param.getMobileVerificationCode())) {
            //     return RestResponse.failed(ResponseCode.USER_REGISTER_MOBILE_VERIFICATION_CODE_REQUIRED);
            // }
            // // 校验
            // RestResponse rsp = smsService.verifyVerificationCode(param.getMobileCountryCode(), param.getMobile(), param.getMobileVerificationCode());
            // if (!rsp.isOkRsp()) {
            //     return rsp;
            // }
        }

        // 资金密码
        if (Boolean.TRUE.equals(registerConfig.getFundPwdRequired())) {
            // 不能为空
            if (StrUtil.isBlank(param.getFundPwd())) {
                return RestResponse.failed(ResponseCode.USER_REGISTER_FUND_PWD_REQUIRED);
            }
        } else {
            // 不必填，填了也清空
            param.setFundPwd(null);
        }

        return RestResponse.OK;
    }

    private RestResponse checkDailyLimitCount(RegisterConfigBO registerConfig, String ip, String date) {
        if (!NumberUtil.isGreatThenZero(registerConfig.getDailyLimitCountByIp())) {
            return RestResponse.OK;
        }

        String redisKey = StrUtil.format(RedisKeyEnum.REGISTER_DAILY_LIMIT_COUNT_BY_IP.getVal(), date, ip);
        Object val = redisValue.get(redisKey);
        int alreadyCount = Convert.toInt(val, 0);

        if (alreadyCount >= registerConfig.getDailyLimitCountByIp()) {
            return RestResponse.failed(ResponseCode.USER_REGISTER_LIMIT_COUNT_BY_IP_EXCEEDED, ip);
        }
        return RestResponse.OK;
    }

    private void incrementDailyLimitCount(String ip, String date) {
        String redisKey = StrUtil.format(RedisKeyEnum.REGISTER_DAILY_LIMIT_COUNT_BY_IP.getVal(), date, ip);
        byte[] redisKeyByte = redisKeySerializer.serialize(redisKey);
        redisValue.getOperations().executePipelined((RedisCallback<Object>) connection -> {
            connection.incrBy(redisKeyByte, 1);

            connection.expire(redisKeyByte, Duration.ofDays(1L).plusMinutes(10).getSeconds());

            return null;
        });
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateFirstAndTotalRecharge(long userId, BigDecimal amount, LocalDateTime time) {
        PortalUserProfile oldProfile = portalUserProfileService.getById(userId);

        if (oldProfile == null) {
            return;
        }

        PortalUserProfile updateProfile = new PortalUserProfile();
        updateProfile.setId(userId);
        if (oldProfile.getFirstRechargeTime() == null) {
            updateProfile.setFirstRechargeTime(time);
        }
        if (oldProfile.getFirstRechargeAmount() == null) {
            updateProfile.setFirstRechargeAmount(amount);
        }
        updateProfile.setTotalRechargeAmount(amount);
        portalUserProfileService.updateById(updateProfile);

        // todo 报表
        // if (user.getFirstRechargeTime() == null) {
        //     ReportConfigBO reportConfig = sysConfigCache.getReportConfigFromRedis();
        //     String reportDate = DateTimeUtil.getOffsetDateStrByDateTime(time, reportConfig.getOffsetTime());
        //     reportService.addFirstRecharge(user.getId(), reportDate, amount);
        // }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateFirstAndTotalWithdraw(long userId, BigDecimal amount, LocalDateTime time) {
        PortalUserProfile oldProfile = portalUserProfileService.getById(userId);

        if (oldProfile == null) {
            return;
        }

        PortalUserProfile updateProfile = new PortalUserProfile();
        updateProfile.setId(userId);
        if (oldProfile.getFirstWithdrawTime() == null) {
            updateProfile.setFirstWithdrawTime(time);
        }
        if (oldProfile.getFirstWithdrawAmount() == null) {
            updateProfile.setFirstWithdrawAmount(amount);
        }
        updateProfile.setTotalWithdrawAmount(amount);
        portalUserProfileService.updateById(updateProfile);
    }

    @Override
    public RestResponse editInternalUserForAdmin(UsernameEnableDisableParam param) {
        Long userId = UserUtil.getUserIdByUsernameFromLocal(param.getUsername(), PortalTypeEnum.PORTAL);
        if (userId == null) {
            return RestResponse.failed(ResponseCode.USER_NOT_FOUND, param.getUsername());
        }
        portalUserProfileService.lambdaUpdate()
                .eq(PortalUserProfile::getId, userId)
                .set(PortalUserProfile::getInternalUser, param.getEnable())
                .update();
        return RestResponse.OK;
    }

}
