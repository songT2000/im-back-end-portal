package com.im.common.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.im.common.cache.impl.SysConfigCache;
import com.im.common.cache.sysconfig.bo.GlobalConfigBO;
import com.im.common.entity.AdminUser;
import com.im.common.entity.enums.PortalTypeEnum;
import com.im.common.mapper.AdminUserMapper;
import com.im.common.response.ResponseCode;
import com.im.common.response.RestResponse;
import com.im.common.service.GoogleAuthService;
import com.im.common.util.google.auth.GoogleAuthUtil;
import com.im.common.util.google.auth.GoogleBindVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 谷歌身份验证器 服务类
 *
 * @author Barry
 * @date 2018/5/13
 */
@Service
public class GoogleAuthServiceImpl implements GoogleAuthService {
    private AdminUserMapper adminUserMapper;
    private SysConfigCache sysConfigCache;

    @Autowired
    public void setAdminUserMapper(AdminUserMapper adminUserMapper) {
        this.adminUserMapper = adminUserMapper;
    }

    @Autowired
    public void setSysConfigCache(SysConfigCache sysConfigCache) {
        this.sysConfigCache = sysConfigCache;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public GoogleBindVO createCredentials(String username, PortalTypeEnum portalType) {
        GlobalConfigBO globalConfig = sysConfigCache.getGlobalConfigFromRedis();

        GoogleBindVO googleBindVO = GoogleAuthUtil.createCredentials(username, globalConfig.getGoogleIssuer());

        if (googleBindVO != null) {
            updateGoogleKey(portalType, username, googleBindVO.getKey());
        }

        return googleBindVO;
    }

    @Override
    @Transactional(readOnly = true, rollbackFor = {Exception.class})
    public RestResponse authoriseGoogle(String username, PortalTypeEnum portalType, Integer googleCode) {
        if (portalType == PortalTypeEnum.PORTAL) {

            return RestResponse.failed(ResponseCode.SYS_METHOD_UNDER_CONSTRUCTION);

        } else if (portalType == PortalTypeEnum.ADMIN) {

            AdminUser user = adminUserMapper
                    .selectOne(new LambdaQueryWrapper<AdminUser>()
                            .eq(AdminUser::getUsername, username));
            return authoriseGoogle(user, googleCode);
        }

        return RestResponse.failed(ResponseCode.USER_NOT_FOUND);
    }

    @Override
    @Transactional(readOnly = true, rollbackFor = {Exception.class})
    public RestResponse authoriseGoogle(AdminUser user, Integer googleCode) {
        if (user == null) {
            return RestResponse.failed(ResponseCode.USER_NOT_FOUND);
        }
        GlobalConfigBO globalConfig = sysConfigCache.getGlobalConfigFromRedis();
        if (!Boolean.TRUE.equals(globalConfig.getGoogleEnabled())) {
            return RestResponse.OK;
        }
        if (!Boolean.TRUE.equals(user.getGoogleBound()) || StrUtil.isBlank(user.getGoogleKey())) {
            // 没有绑定谷歌，返回需要绑定谷歌的数据
            GoogleBindVO credentials = createCredentials(user.getUsername(), PortalTypeEnum.ADMIN);
            RestResponse rsp = RestResponse.failed(ResponseCode.GOOGLE_NOT_YET_BIND);
            rsp.setData(credentials);
            return rsp;
        }

        // 验证谷歌验证码
        return GoogleAuthUtil.authoriseGoogle(user.getUsername(), user.getGoogleKey(), googleCode);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public RestResponse bindGoogle(String username, PortalTypeEnum portalType, Integer googleCode) {
        if (portalType == PortalTypeEnum.PORTAL) {

            return RestResponse.failed(ResponseCode.SYS_METHOD_UNDER_CONSTRUCTION);

        } else if (portalType == PortalTypeEnum.ADMIN) {

            AdminUser user = adminUserMapper
                    .selectOne(new LambdaQueryWrapper<AdminUser>()
                            .eq(AdminUser::getUsername, username));
            return bindGoogle(user, googleCode);

        }

        return RestResponse.failed(ResponseCode.USER_NOT_FOUND);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public RestResponse bindGoogle(AdminUser user, Integer googleCode) {
        if (user == null) {
            return RestResponse.failed(ResponseCode.GOOGLE_CODE_ERROR);
        }

        // 已经绑定不可再次绑定
        if (StrUtil.isNotBlank(user.getGoogleKey()) && Boolean.TRUE.equals(user.getGoogleBound())) {
            return RestResponse.failed(ResponseCode.GOOGLE_ALREADY_BIND);
        }

        // 验证谷歌验证码
        RestResponse authorised = GoogleAuthUtil.authoriseGoogle(user.getUsername(), user.getGoogleKey(), googleCode);
        if (!authorised.isOkRsp()) {
            return authorised;
        }

        // 修改绑定状态
        AdminUser updateUser = new AdminUser();
        updateUser.setUsername(user.getUsername());
        updateUser.setGoogleBound(Boolean.TRUE);
        adminUserMapper.update(updateUser, new LambdaQueryWrapper<AdminUser>().eq(AdminUser::getUsername, updateUser.getUsername()));

        return RestResponse.OK;
    }

    private boolean updateGoogleKey(PortalTypeEnum portalType, String username, String googleKey) {
        if (portalType == PortalTypeEnum.PORTAL) {
            return false;

        } else if (portalType == PortalTypeEnum.ADMIN) {

            AdminUser updateUser = new AdminUser();
            updateUser.setGoogleKey(googleKey);
            int update = adminUserMapper.update(updateUser, new LambdaQueryWrapper<AdminUser>()
                    .eq(AdminUser::getUsername, username));
            return update > 0;

        }

        return false;
    }
}
