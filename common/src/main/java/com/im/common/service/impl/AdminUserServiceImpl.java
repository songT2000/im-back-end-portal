package com.im.common.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.im.common.cache.base.CacheProxy;
import com.im.common.cache.impl.AdminIpBlackWhiteCache;
import com.im.common.cache.impl.SysConfigCache;
import com.im.common.cache.sysconfig.bo.AdminConfigBO;
import com.im.common.cache.sysconfig.bo.GlobalConfigBO;
import com.im.common.constant.CommonConstant;
import com.im.common.entity.*;
import com.im.common.entity.enums.DeviceTypeEnum;
import com.im.common.entity.enums.PortalTypeEnum;
import com.im.common.entity.enums.SysCacheRefreshTypeEnum;
import com.im.common.mapper.AdminUserMapper;
import com.im.common.param.*;
import com.im.common.response.ResponseCode;
import com.im.common.response.RestResponse;
import com.im.common.service.*;
import com.im.common.util.CollectionUtil;
import com.im.common.util.EnumUtil;
import com.im.common.util.NumberUtil;
import com.im.common.util.PasswordUtil;
import com.im.common.util.google.auth.GoogleBindVO;
import com.im.common.util.jwt.JwtInfo;
import com.im.common.util.jwt.JwtUtil;
import com.im.common.util.mybatis.page.PageVO;
import com.im.common.util.mybatis.service.MyBatisPlusServiceImpl;
import com.im.common.util.redis.AdminRedisSessionManager;
import com.im.common.util.user.UserUtil;
import com.im.common.vo.*;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

/**
 * 后台管理用户表 服务实现类
 *
 * @author Barry
 * @date 2019-11-06
 */
@Service
public class AdminUserServiceImpl
        extends MyBatisPlusServiceImpl<AdminUserMapper, AdminUser>
        implements AdminUserService {
    private static final Log LOG = LogFactory.get();

    private AdminUserMapper adminUserMapper;
    private GoogleAuthService googleAuthService;
    private AdminRedisSessionManager adminRedisSessionManager;
    private SysConfigCache sysConfigCache;
    private AdminRoleService roleService;
    private AdminMenuService menuService;
    private AdminUserRoleService userRoleService;
    private CacheProxy cacheProxy;
    private UserAuthTokenService userAuthTokenService;
    private UserLoginLogService userLoginLogService;
    private AdminIpBlackWhiteCache adminIpBlackWhiteCache;

    @Autowired
    public void setAdminUserMapper(AdminUserMapper adminUserMapper) {
        this.adminUserMapper = adminUserMapper;
    }

    @Autowired
    public void setGoogleAuthService(GoogleAuthService googleAuthService) {
        this.googleAuthService = googleAuthService;
    }

    @Autowired
    public void setAdminRedisSessionManager(AdminRedisSessionManager adminRedisSessionManager) {
        this.adminRedisSessionManager = adminRedisSessionManager;
    }

    @Autowired
    public void setSysConfigCache(SysConfigCache sysConfigCache) {
        this.sysConfigCache = sysConfigCache;
    }

    @Autowired
    public void setRoleService(AdminRoleService roleService) {
        this.roleService = roleService;
    }

    @Autowired
    public void setMenuService(AdminMenuService menuService) {
        this.menuService = menuService;
    }

    @Autowired
    public void setUserRoleService(AdminUserRoleService userRoleService) {
        this.userRoleService = userRoleService;
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
    public void setAdminIpBlackWhiteCache(AdminIpBlackWhiteCache adminIpBlackWhiteCache) {
        this.adminIpBlackWhiteCache = adminIpBlackWhiteCache;
    }

    @Override
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public List<Long> listOnlineUserId() {
        List<AdminUser> list = lambdaQuery()
                .select(AdminUser::getId)
                .eq(AdminUser::getOnline, Boolean.TRUE)
                .list();
        return CollectionUtil.toList(list, AdminUser::getId);
    }

    @Override
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public AdminUser getByUsernameNotDeleted(String username) {
        return lambdaQuery()
                .eq(AdminUser::getUsername, username)
                .eq(AdminUser::getDeleted, Boolean.FALSE).one();
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateByUsername(AdminUser user) {
        return update(user, new LambdaQueryWrapper<AdminUser>().eq(AdminUser::getUsername, user.getUsername()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResponse<AdminUserLoginVO> manualLogin(HttpServletRequest request, ManualLoginParam param) {
        AdminUser user = getByUsernameNotDeleted(param.getUsername());
        if (user == null) {
            return RestResponse.failed(ResponseCode.USER_LOGIN_USERNAME_OR_PASSWORD_ERROR);
        }

        // 检查系统级允许登录IP
        AdminConfigBO adminConfig = sysConfigCache.getAdminConfigFromRedis();

        // 验证密码
        boolean pwdValid = PasswordUtil.validatePwd(user.getLoginPwd(), param.getPassword());
        if (!pwdValid) {
            if (NumberUtil.isGreatThenZero(adminConfig.getLoginPwdErrorTimes())) {
                int leftTimes = increasePwdErrorTimes(user, adminConfig);
                if (leftTimes <= 0) {
                    return RestResponse.failed(ResponseCode.USER_LOGIN_TOO_MANY_PWD_ERROR_TIMES);
                } else {
                    return RestResponse.failed(ResponseCode.USER_LOGIN_USERNAME_OR_PASSWORD_ERROR_LEFT_TIME, leftTimes);
                }
            } else {
                return RestResponse.failed(ResponseCode.USER_LOGIN_USERNAME_OR_PASSWORD_ERROR);
            }
        }

        // 验证谷歌，验证谷歌要放在最后一步，因为一组验证码短时间内只能使用一次
        GlobalConfigBO globalConfig = sysConfigCache.getGlobalConfigFromRedis();
        if (Boolean.TRUE.equals(globalConfig.getGoogleEnabled())) {
            if (!Boolean.TRUE.equals(user.getGoogleBound()) || StrUtil.isBlank(user.getGoogleKey())) {
                // 没有绑定谷歌，返回需要绑定谷歌的数据
                GoogleBindVO credentials = googleAuthService.createCredentials(user.getUsername(), PortalTypeEnum.ADMIN);
                RestResponse rsp = RestResponse.failed(ResponseCode.GOOGLE_NOT_YET_BIND);
                rsp.setData(credentials);
                return rsp;
            }
            if (param.getGoogleCode() == null) {
                return RestResponse.failed(ResponseCode.GOOGLE_CODE_REQUIRED);
            }

            RestResponse authorised = googleAuthService.authoriseGoogle(user, param.getGoogleCode());
            if (!authorised.isOkRsp()) {
                if (NumberUtil.isGreatThenZero(adminConfig.getLoginPwdErrorTimes())) {
                    if (!authorised.getCode().equals(ResponseCode.GOOGLE_NOT_YET_BIND.name())) {
                        increasePwdErrorTimes(user, adminConfig);
                    }
                }
                return authorised;
            }
        }

        // 处理登录通用逻辑
        RestResponse<AdminSessionUser> restResponse = processLogin(user, param);
        if (restResponse.isOkRsp()) {
            AdminSessionUser sessionUser = restResponse.getData();
            boolean hasAdminRole = roleService.hasAdminRole(sessionUser.getRoles());

            // 获取权限列表
            List<AdminMenu> adminMenus = menuService.listUserMenus(user.getId(), hasAdminRole);

            return RestResponse.ok(new AdminUserLoginVO(sessionUser, user, adminMenus));
        }
        return RestResponse.build(restResponse);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResponse tokenLogin(HttpServletRequest request, TokenLoginParam param) {
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
        UserAuthToken authToken = userAuthTokenService.getByToken(param.getToken(), PortalTypeEnum.ADMIN);
        if (authToken == null) {
            return RestResponse.logout(ResponseCode.USER_SESSION_INVALIDATE);
        }
        if (authToken.getExpireTime() != null && LocalDateTime.now().isAfter(authToken.getExpireTime())) {
            userAuthTokenService.deleteTokenAndSession(param.getToken(), PortalTypeEnum.ADMIN);
            return RestResponse.logout(ResponseCode.USER_SESSION_EXPIRED);
        }

        // 获取用户
        AdminUser user = getByUsernameNotDeleted(jwtInfo.getUsername());
        if (user == null) {
            return RestResponse.logout(ResponseCode.USER_SESSION_INVALIDATE);
        }
        if (Boolean.TRUE.equals(user.getDeleted())) {
            return RestResponse.failed(ResponseCode.USER_NOT_FOUND);
        }
        if (!Boolean.TRUE.equals(user.getEnabled())) {
            return RestResponse.failed(ResponseCode.YOUR_ACCOUNT_HAS_BEEN_DISABLED);
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
    private RestResponse<AdminSessionUser> processLogin(AdminUser user, BaseLoginParam param) {
        // 判断账户状态
        if (Boolean.TRUE.equals(user.getDeleted())) {
            return RestResponse.failed(ResponseCode.USER_NOT_FOUND);
        }
        if (!Boolean.TRUE.equals(user.getEnabled())) {
            return RestResponse.failed(ResponseCode.YOUR_ACCOUNT_HAS_BEEN_DISABLED);
        }

        // 检查允许登录IP
        RestResponse ipRsp = adminIpBlackWhiteCache.checkIpBlackWhite(param.getIp());
        if (!ipRsp.isOkRsp()) {
            return ipRsp;
        }

        // 检查密码出错次数
        AdminConfigBO adminConfig = sysConfigCache.getAdminConfigFromRedis();
        if (NumberUtil.isGreatThenZero(adminConfig.getLoginPwdErrorTimes())) {
            if (user.getPwdErrorTimes() >= adminConfig.getLoginPwdErrorTimes()) {
                return RestResponse.failed(ResponseCode.USER_LOGIN_TOO_MANY_PWD_ERROR_TIMES, param.getIp());
            }
        }

        // 获取角色列表
        List<AdminRole> adminRoles = roleService.listUserRoles(user.getId(), true);
        if (CollectionUtil.isEmpty(adminRoles)) {
            return RestResponse.failed(ResponseCode.USER_ROLE_DISABLED);
        }

        // 组装会话用户
        AdminSessionUser sessionUser = new AdminSessionUser(user, null, adminRoles);

        // 登录token
        String token;
        if (param instanceof TokenLoginParam) {
            token = ((TokenLoginParam) param).getToken();
        } else {
            // user_auth_token表存入
            token = userAuthTokenService.saveAuthTokenAfterLogin(sessionUser, param);
        }
        sessionUser.setToken(token);

        // 踢出之前已经授权过的记录
        kickOutOtherByOtherLogin(user, token);

        // 登录成功，新增登录日志
        UserLoginLog loginLog =
                userLoginLogService.addLoginLog(user.getId(), param, PortalTypeEnum.ADMIN);

        sessionUser.setLoginIp(loginLog.getIp());
        sessionUser.setLoginArea(loginLog.getArea());
        sessionUser.setLoginDeviceId(loginLog.getDeviceId());
        sessionUser.setLoginDeviceType(loginLog.getDeviceType());
        sessionUser.setLoginTime(loginLog.getCreateTime());

        // 修改用户信息
        lambdaUpdate()
                .eq(AdminUser::getId, user.getId())
                .set(AdminUser::getOnline, true)
                .set(AdminUser::getLastLoginTime, loginLog.getCreateTime())
                .set(AdminUser::getLastLoginIp, loginLog.getIp())
                .set(AdminUser::getLastLoginArea, loginLog.getArea())
                .set(AdminUser::getPwdErrorTimes, CommonConstant.INT_0)
                .update();

        // 保存session
        adminRedisSessionManager.setSessionUser(token, sessionUser);

        return RestResponse.ok(sessionUser);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResponse logout(HttpServletRequest request) {
        String token = JwtUtil.getTokenFromHeader(request);
        if (StrUtil.isBlank(token)) {
            return RestResponse.OK;
        }

        // 删除该token，删除该redis会话
        userAuthTokenService.deleteTokenAndSession(token, PortalTypeEnum.ADMIN);

        try {
            // 如果没有有效session了，把用户状态设置为离线
            JwtInfo jwtInfo = JwtUtil.getJwtInfo(token);
            if (jwtInfo != null) {
                Long userId = UserUtil.getUserIdByUsernameFromLocal(jwtInfo.getUsername(), PortalTypeEnum.ADMIN);

                if (userId != null) {
                    Set<String> tokenSet = adminRedisSessionManager.listAllTokenByUsername(jwtInfo.getUsername());
                    // 是否还有有效session
                    boolean hasValidSession = false;

                    if (CollectionUtil.isNotEmpty(tokenSet)) {
                        for (String otherToken : tokenSet) {
                            AdminSessionUser sessionUser = adminRedisSessionManager.getSessionUser(otherToken);
                            if (sessionUser != null) {
                                String logoutCode = adminRedisSessionManager.getLogoutCode(otherToken);
                                if (StrUtil.isBlank(logoutCode)) {
                                    hasValidSession = true;
                                    break;
                                }
                            }
                        }
                    }
                    if (!hasValidSession) {
                        // 把用户状态直接改为离线
                        updateUserOffline(userId);
                    }
                }
            }
        } catch (Exception e) {
            LOG.error(e, "登出出错");
        }

        return RestResponse.OK;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void kickOutOtherLoginClient(AdminUser user, String excludeToken, ResponseCode logoutCode) {
        // 当logoutCode参数为空时设置默认值
        ResponseCode realLogoutCode = Optional.ofNullable(logoutCode).orElse(ResponseCode.USER_SESSION_INVALIDATE);

        // 把所有redis会话拿出来，仅保留该token
        Set<String> tokenSet = adminRedisSessionManager.listAllTokenByUsername(user.getUsername());
        if (CollectionUtil.isNotEmpty(tokenSet)) {
            for (String token : tokenSet) {
                if (token.equalsIgnoreCase(excludeToken)) {
                    continue;
                }
                userAuthTokenService.deleteTokenOnly(token, PortalTypeEnum.ADMIN);
                adminRedisSessionManager.setLogoutCode(token, realLogoutCode);
            }
        }

        // 获取这个用户的所有token，仅保留该token
        List<UserAuthToken> authTokenList = userAuthTokenService.listByUserId(user.getId(), PortalTypeEnum.ADMIN, excludeToken);
        if (CollectionUtil.isNotEmpty(authTokenList)) {
            for (UserAuthToken authToken : authTokenList) {
                // 删除token
                userAuthTokenService.removeById(authToken.getId());

                // 会话中设置登出消息
                adminRedisSessionManager.setLogoutCode(authToken.getToken(), realLogoutCode);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void kickOutAllLoginClient(long userId, ResponseCode logoutCode) {
        String username = UserUtil.getUsernameByIdFromLocal(userId, PortalTypeEnum.ADMIN);
        // 当logoutCode参数为空时设置默认值
        ResponseCode realLogoutCode = Optional.ofNullable(logoutCode).orElse(ResponseCode.USER_SESSION_INVALIDATE);

        // 把所有redis会话设置为登出
        Set<String> tokenSet = adminRedisSessionManager.listAllTokenByUsername(username);
        if (CollectionUtil.isNotEmpty(tokenSet)) {
            for (String token : tokenSet) {
                adminRedisSessionManager.setLogoutCode(token, realLogoutCode);
            }
        }

        // 把所有数据库会话设置为登出
        List<UserAuthToken> authTokenList = userAuthTokenService.listByUserId(userId, PortalTypeEnum.ADMIN);
        if (CollectionUtil.isNotEmpty(authTokenList)) {
            for (UserAuthToken authToken : authTokenList) {
                adminRedisSessionManager.setLogoutCode(authToken.getToken(), realLogoutCode);
            }
        }

        // 删除所有token
        userAuthTokenService.deleteUserTokenOnly(userId, PortalTypeEnum.ADMIN);

        // 用户状态改为离线
        updateUserOffline(userId);
    }

    private void updateUserOffline(long userId) {
        lambdaUpdate().eq(AdminUser::getId, userId).set(AdminUser::getOnline, false).update();
    }

    @Override
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public boolean hasBoundGoogle(String username) {
        GlobalConfigBO globalConfig = sysConfigCache.getGlobalConfigFromRedis();
        if (!Boolean.TRUE.equals(globalConfig.getGoogleEnabled())) {
            return false;
        }

        AdminUser one = lambdaQuery()
                .select(AdminUser::getGoogleKey, AdminUser::getGoogleBound)
                .eq(AdminUser::getUsername, username)
                .eq(AdminUser::getDeleted, Boolean.FALSE).one();
        return one != null && StrUtil.isNotBlank(one.getGoogleKey()) && Boolean.TRUE.equals(one.getGoogleBound());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResponse bindGoogle(BindGoogleParam param) {
        // 检查允许登录IP
        RestResponse ipRsp = adminIpBlackWhiteCache.checkIpBlackWhite(param.getIp());
        if (!ipRsp.isOkRsp()) {
            return ipRsp;
        }

        AdminUser user = getByUsernameNotDeleted(param.getUsername());
        if (user == null) {
            return RestResponse.failed(ResponseCode.USER_LOGIN_USERNAME_OR_PASSWORD_ERROR);
        }

        // 检查密码出错次数
        AdminConfigBO adminConfig = sysConfigCache.getAdminConfigFromRedis();
        if (NumberUtil.isGreatThenZero(adminConfig.getLoginPwdErrorTimes())) {
            if (user.getPwdErrorTimes() >= adminConfig.getLoginPwdErrorTimes()) {
                return RestResponse.failed(ResponseCode.USER_LOGIN_TOO_MANY_PWD_ERROR_TIMES, param.getIp());
            }
        }

        // 判断账户状态
        if (Boolean.TRUE.equals(user.getDeleted())) {
            return RestResponse.failed(ResponseCode.USER_NOT_FOUND);
        }
        if (!Boolean.TRUE.equals(user.getEnabled())) {
            return RestResponse.failed(ResponseCode.YOUR_ACCOUNT_HAS_BEEN_DISABLED);
        }

        // 验证密码
        boolean pwdValid = PasswordUtil.validatePwd(user.getLoginPwd(), param.getPassword());
        if (!pwdValid) {
            if (NumberUtil.isGreatThenZero(adminConfig.getLoginPwdErrorTimes())) {
                int leftTimes = increasePwdErrorTimes(user, adminConfig);
                if (leftTimes <= 0) {
                    return RestResponse.failed(ResponseCode.USER_LOGIN_TOO_MANY_PWD_ERROR_TIMES);
                } else {
                    return RestResponse.failed(ResponseCode.USER_LOGIN_USERNAME_OR_PASSWORD_ERROR_LEFT_TIME, leftTimes);
                }
            } else {
                return RestResponse.failed(ResponseCode.USER_LOGIN_USERNAME_OR_PASSWORD_ERROR);
            }
        }

        // 绑定谷歌
        return googleAuthService.bindGoogle(user, param.getGoogleCode());
    }

    @Override
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public PageVO<AdminUserVO> pageLowerUsers(AdminUserPageParam param,
                                              AdminSessionUser sessionUser) {
        List<AdminRole> sessionRoles = roleService.listUserRoles(sessionUser.getId());

        if (CollectionUtil.isEmpty(sessionRoles)) {
            return new PageVO<>(param);
        }

        Page<AdminUser> pageParam = param.toPage();

        // 查询下级角色
        List<AdminRole> lowerRoles = roleService.listLowerRoles(CollectionUtil.toList(sessionRoles, AdminRole::getId));
        List<Long> lowerRoleIds = CollectionUtil.toList(lowerRoles, AdminRole::getId);

        // 先查出用户分页列表，然后再查出角色
        List<AdminUser> adminUsers = adminUserMapper.pageLowerUsers(pageParam, param, lowerRoleIds);
        if (CollectionUtil.isEmpty(adminUsers)) {
            return new PageVO<>(param);
        }

        List<Long> adminIds = CollectionUtil.toList(adminUsers, AdminUser::getId);

        // 查出所有人的角色列表
        Map<Long, List<AdminRole>> rolesMap = roleService.listUserRoles(adminIds);

        Function<AdminUser, AdminUserVO> mapper = (adminUser) -> {
            AdminUserVO adminUserVO = new AdminUserVO(adminUser);
            List<AdminRole> adminRoles = rolesMap.get(adminUserVO.getId());
            adminUserVO.setRoles(CollectionUtil.toList(adminRoles, AdminRoleVO::new));
            adminUserVO.setHasAdminRole(roleService.hasAdminRole(adminRoles));
            return adminUserVO;
        };

        // 手动封装分页
        PageVO<AdminUserVO> adminUserPageVO = new PageVO<>(pageParam, adminUsers, mapper);
        return adminUserPageVO;
    }

    @Override
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public boolean isExists(String username) {
        return lambdaQuery()
                .eq(AdminUser::getUsername, StrUtil.trim(username)).one() != null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResponse editLoginPwd(AdminSessionUser sessionUser, UserEditPwdParam param) {
        // 检测旧密码是否匹配
        AdminUser user = getById(sessionUser.getId());
        RestResponse rsp = PasswordUtil.validEditLoginPwd(user.getLoginPwd(), param.getOldPwd(), param.getNewPwd());
        if (!rsp.isOkRsp()) {
            return rsp;
        }

        boolean updated = lambdaUpdate()
                .eq(AdminUser::getId, user.getId())
                .set(AdminUser::getLoginPwd, PasswordUtil.generateDbPwdFromMd5String(param.getNewPwd()))
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
    public RestResponse add(AdminSessionUser sessionUser,
                            AdminUserAddParam param) {
        // 检查用户名是否已经存在
        if (isExists(param.getUsername())) {
            return RestResponse.failed(ResponseCode.USER_USERNAME_EXISTED);
        }

        // 是否是合法下级角色
        RestResponse isLowerRsp = roleService.isLowerRolesById(sessionUser.getId(), param.getRoleIds());
        if (!isLowerRsp.isOkRsp()) {
            return isLowerRsp;
        }

        // 新增用户
        AdminUser adminUser = new AdminUser();
        adminUser.setUsername(StrUtil.trim(param.getUsername()));
        adminUser.setLoginPwd(PasswordUtil.generateDbPwdFromMd5String(param.getLoginPwd()));
        adminUser.setPwdErrorTimes(0);
        // 默认不启用谷歌
        adminUser.setGoogleBound(Boolean.FALSE);
        adminUser.setEnabled(Boolean.TRUE);
        adminUser.setOnline(Boolean.FALSE);
        adminUser.setDeleted(Boolean.FALSE);
        adminUser.setRemark(param.getRemark());

        // 新增用户
        boolean inserted = save(adminUser);

        if (inserted) {
            // 新增用户角色
            userRoleService.adjustUserRoles(adminUser.getId(), param.getRoleIds());
            cacheProxy.signalRefreshCache(SysCacheRefreshTypeEnum.ADMIN_USER);

            return RestResponse.OK;
        } else {
            return RestResponse.SYS_DATA_STATUS_ERROR;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResponse edit(AdminSessionUser sessionUser,
                             AdminUserEditParam param) {
        AdminUser dbUser = getByUsernameNotDeleted(param.getUsername());

        // 检查是否可以编辑
        RestResponse rsp = checkEditableForLower(sessionUser, dbUser);
        if (!rsp.isOkRsp()) {
            return rsp;
        }

        // 是否是合法下级角色
        RestResponse isLowerRsp = roleService.isLowerRolesById(sessionUser.getId(), param.getRoleIds());
        if (!isLowerRsp.isOkRsp()) {
            return isLowerRsp;
        }

        // 修改用户
        AdminUser updateUser = new AdminUser();
        updateUser.setUsername(param.getUsername());
        if (StrUtil.isNotBlank(param.getLoginPwd())) {
            updateUser.setLoginPwd(PasswordUtil.generateDbPwdFromMd5String(param.getLoginPwd()));
            // 修改密码的同时，重置密码错误次数
            updateUser.setPwdErrorTimes(0);
        }
        updateUser.setRemark(param.getRemark());
        boolean updated = updateByUsername(updateUser);
        if (!updated) {
            return RestResponse.failed(ResponseCode.SYS_DATA_STATUS_ERROR);
        }

        // 调整角色
        userRoleService.adjustUserRoles(dbUser.getId(), param.getRoleIds());

        // 踢出在线用户
        kickOutAllLoginClient(dbUser.getId(), ResponseCode.USER_SESSION_INFO_CHANGED);
        cacheProxy.signalRefreshCache(SysCacheRefreshTypeEnum.ADMIN_USER);

        return RestResponse.OK;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResponse delete(AdminSessionUser sessionUser,
                               UsernameGoogleCodeParam param) {
        // 检查谷歌
        {
            RestResponse googleRsp = googleAuthService.authoriseGoogle(sessionUser.getUsername(), PortalTypeEnum.ADMIN, param.getGoogleCode());
            if (!googleRsp.isOkRsp()) {
                return googleRsp;
            }
        }

        AdminUser dbUser = getByUsernameNotDeleted(param.getUsername());

        // 检查是否可以编辑
        RestResponse rsp = checkEditableForLower(sessionUser, dbUser);
        if (!rsp.isOkRsp()) {
            return rsp;
        }

        // 修改用户
        boolean updated = lambdaUpdate()
                .eq(AdminUser::getUsername, param.getUsername())
                .eq(AdminUser::getDeleted, false)
                .set(AdminUser::getDeleted, true)
                .update();
        if (!updated) {
            return RestResponse.failed(ResponseCode.SYS_DATA_STATUS_ERROR);
        }

        // 踢出在线用户
        kickOutAllLoginClient(dbUser.getId(), ResponseCode.USER_SESSION_INFO_CHANGED);
        cacheProxy.signalRefreshCache(SysCacheRefreshTypeEnum.ADMIN_USER);

        return RestResponse.OK;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResponse enableDisable(AdminSessionUser sessionUser,
                                      String username, boolean enable) {
        AdminUser dbUser = getByUsernameNotDeleted(username);

        // 检查是否可以编辑
        RestResponse rsp = checkEditableForLower(sessionUser, dbUser);
        if (!rsp.isOkRsp()) {
            return rsp;
        }

        // 修改用户
        boolean updated = lambdaUpdate()
                .eq(AdminUser::getUsername, username)
                .eq(AdminUser::getEnabled, !enable)
                .set(AdminUser::getEnabled, enable)
                .update();
        if (!updated) {
            return RestResponse.failed(ResponseCode.SYS_DATA_STATUS_ERROR);
        }

        // 踢出在线用户
        kickOutAllLoginClient(dbUser.getId(), ResponseCode.USER_SESSION_INFO_CHANGED);
        cacheProxy.signalRefreshCache(SysCacheRefreshTypeEnum.ADMIN_USER);

        return RestResponse.OK;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResponse unbindGoogle(AdminSessionUser sessionUser,
                                     String username) {
        AdminUser dbUser = getByUsernameNotDeleted(username);

        // 检查是否可以编辑
        RestResponse rsp = checkEditableForLower(sessionUser, dbUser);
        if (!rsp.isOkRsp()) {
            return rsp;
        }

        // 修改用户
        boolean updated = lambdaUpdate()
                .eq(AdminUser::getUsername, username)
                .set(AdminUser::getGoogleBound, false)
                .set(AdminUser::getGoogleKey, null)
                .update();
        if (updated) {
            // 踢下线
            kickOutAllLoginClient(username, ResponseCode.USER_SESSION_INFO_CHANGED);
        }

        return updated ? RestResponse.OK : RestResponse.SYS_DATA_STATUS_ERROR;
    }

    private RestResponse checkEditableForLower(AdminSessionUser sessionUser,
                                               AdminUser targetUser) {
        if (targetUser == null) {
            return RestResponse.failed(ResponseCode.SYS_DATA_NOT_EXIST);
        }

        // 不能操作本人账号
        if (targetUser.getId().equals(sessionUser.getId())) {
            return RestResponse.failed(ResponseCode.USER_NOT_ALLOW_OPERATE_SELF);
        }

        // 不能操作已删除账号
        if (Boolean.TRUE.equals(targetUser.getDeleted())) {
            return RestResponse.failed(ResponseCode.SYS_DATA_DELETED);
        }

        // 只能操作原来是下级的账号
        RestResponse isLowerRsp = roleService.isLowerUser(sessionUser.getId(), targetUser.getId());
        if (!isLowerRsp.isOkRsp()) {
            return isLowerRsp;
        }
        return RestResponse.OK;
    }

    /**
     * 增加一次密码错误次数，并返回剩余次数
     *
     * @param user        用户
     * @param adminConfig AdminConfigBO
     * @return
     */
    private int increasePwdErrorTimes(AdminUser user, AdminConfigBO adminConfig) {
        if (!NumberUtil.isGreatThenZero(adminConfig.getLoginPwdErrorTimes())) {
            return CommonConstant.INT_0;
        }

        AdminUser updateUser = new AdminUser();
        updateUser.setPwdErrorTimes(1);

        saveOrUpdate(updateUser, new LambdaQueryWrapper<AdminUser>().eq(AdminUser::getId, user.getId()));

        return adminConfig.getLoginPwdErrorTimes() - user.getPwdErrorTimes() - 1;
    }

    /***
     * 重置密码错误次数
     * @param sessionUser
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResponse resetLoginPwdErrorNum(AdminSessionUser sessionUser,
                                              String username) {
        AdminUser dbUser = getByUsernameNotDeleted(username);
        // 检查是否可以编辑
        RestResponse rsp = checkEditableForLower(sessionUser, dbUser);
        if (!rsp.isOkRsp()) {
            return rsp;
        }

        // 修改用户
        boolean updated = lambdaUpdate()
                .eq(AdminUser::getUsername, username)
                .set(AdminUser::getPwdErrorTimes, 0)
                .update();
        if (!updated) {
            return RestResponse.SYS_DATA_STATUS_ERROR;
        }
        return RestResponse.OK;
    }

    @Override
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public List<AdminUserSimpleVO> listEnabledSimpleUser() {
        List<AdminUser> list = lambdaQuery()
                .eq(AdminUser::getEnabled, Boolean.TRUE)
                .eq(AdminUser::getDeleted, Boolean.FALSE)
                .list();
        return CollectionUtil.toList(list, AdminUserSimpleVO::new);
    }
}
