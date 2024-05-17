package com.im.common.service.impl;

import com.im.common.cache.base.CacheProxy;
import com.im.common.entity.ApiWithdrawConfig;
import com.im.common.entity.enums.PortalTypeEnum;
import com.im.common.entity.enums.SysCacheRefreshTypeEnum;
import com.im.common.mapper.ApiWithdrawConfigMapper;
import com.im.common.param.ApiWithdrawConfigAddParam;
import com.im.common.param.ApiWithdrawConfigEditParam;
import com.im.common.param.IdEnableDisableParam;
import com.im.common.param.IdGoogleParam;
import com.im.common.response.ResponseCode;
import com.im.common.response.RestResponse;
import com.im.common.service.ApiWithdrawConfigService;
import com.im.common.service.GoogleAuthService;
import com.im.common.util.api.pay.base.withdraw.ApiWithdrawHandler;
import com.im.common.util.api.pay.base.withdraw.ApiWithdrawHandlerFactory;
import com.im.common.util.mybatis.service.MyBatisPlusServiceImpl;
import com.im.common.vo.AdminSessionUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * API代付配置 服务实现类
 *
 * @author Barry
 * @date 2021-09-29
 */
@Service
public class ApiWithdrawConfigServiceImpl
        extends MyBatisPlusServiceImpl<ApiWithdrawConfigMapper, ApiWithdrawConfig>
        implements ApiWithdrawConfigService {

    private GoogleAuthService googleAuthService;
    private CacheProxy cacheProxy;

    @Autowired
    public void setGoogleAuthService(GoogleAuthService googleAuthService) {
        this.googleAuthService = googleAuthService;
    }

    @Autowired
    public void setCacheProxy(CacheProxy cacheProxy) {
        this.cacheProxy = cacheProxy;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResponse addForAdmin(AdminSessionUser sessionUser, ApiWithdrawConfigAddParam param) {
        // 检查谷歌
        {
            RestResponse googleRsp = googleAuthService.authoriseGoogle(sessionUser.getUsername(), PortalTypeEnum.ADMIN, param.getGoogleCode());
            if (!googleRsp.isOkRsp()) {
                return googleRsp;
            }
        }

        // 验证三方配置是否正确
        ApiWithdrawHandler withdrawHandler = ApiWithdrawHandlerFactory.getWithdrawHandler(param.getCode());
        if (withdrawHandler == null) {
            return RestResponse.failed(ResponseCode.API_WITHDRAW_HANDLER_NOT_FOUND);
        }
        if (!withdrawHandler.isValidWithdrawThirdConfig(param.getThirdConfig())) {
            return RestResponse.failed(ResponseCode.API_WITHDRAW_THIRD_CONFIG_FORMATTING_INCORRECT);
        }

        ApiWithdrawConfig config = new ApiWithdrawConfig();
        config.setName(param.getName());
        config.setCode(param.getCode());
        config.setWithdrawConfigSource(param.getWithdrawConfigSource());
        config.setEnabled(param.getEnabled());
        config.setThirdConfig(param.getThirdConfig());
        config.setThirdCallbackWhitelistIp(param.getThirdCallbackWhitelistIp());
        config.setSort(param.getSort());
        config.setDeleted(Boolean.FALSE);

        boolean saved = save(config);
        if (!saved) {
            return RestResponse.SYS_DATA_STATUS_ERROR;
        }

        // 刷新缓存
        cacheProxy.signalRefreshCache(SysCacheRefreshTypeEnum.API_WITHDRAW_CONFIG);

        return RestResponse.OK;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResponse editForAdmin(AdminSessionUser sessionUser, ApiWithdrawConfigEditParam param) {
        // 检查谷歌
        {
            RestResponse googleRsp = googleAuthService.authoriseGoogle(sessionUser.getUsername(), PortalTypeEnum.ADMIN, param.getGoogleCode());
            if (!googleRsp.isOkRsp()) {
                return googleRsp;
            }
        }

        // 验证三方配置是否正确
        ApiWithdrawHandler withdrawHandler = ApiWithdrawHandlerFactory.getWithdrawHandler(param.getCode());
        if (withdrawHandler == null) {
            return RestResponse.failed(ResponseCode.API_WITHDRAW_HANDLER_NOT_FOUND);
        }
        if (!withdrawHandler.isValidWithdrawThirdConfig(param.getThirdConfig())) {
            return RestResponse.failed(ResponseCode.API_WITHDRAW_THIRD_CONFIG_FORMATTING_INCORRECT);
        }

        boolean updated = lambdaUpdate().eq(ApiWithdrawConfig::getId, param.getId())
                .set(ApiWithdrawConfig::getName, param.getName())
                .set(ApiWithdrawConfig::getCode, param.getCode())
                .set(ApiWithdrawConfig::getWithdrawConfigSource, param.getWithdrawConfigSource())
                .set(ApiWithdrawConfig::getEnabled, param.getEnabled())
                .set(ApiWithdrawConfig::getThirdConfig, param.getThirdConfig())
                .set(ApiWithdrawConfig::getThirdCallbackWhitelistIp, param.getThirdCallbackWhitelistIp())
                .set(ApiWithdrawConfig::getSort, param.getSort())
                .update();

        if (!updated) {
            return RestResponse.SYS_DATA_STATUS_ERROR;
        }

        // 刷新缓存
        cacheProxy.signalRefreshCache(SysCacheRefreshTypeEnum.API_WITHDRAW_CONFIG);

        return RestResponse.OK;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResponse deleteForAdmin(AdminSessionUser sessionUser, IdGoogleParam param) {
        // 检查谷歌
        {
            RestResponse googleRsp = googleAuthService.authoriseGoogle(sessionUser.getUsername(), PortalTypeEnum.ADMIN, param.getGoogleCode());
            if (!googleRsp.isOkRsp()) {
                return googleRsp;
            }
        }

        boolean updated = lambdaUpdate()
                .eq(ApiWithdrawConfig::getId, param.getId())
                .set(ApiWithdrawConfig::getDeleted, Boolean.TRUE)
                .update();
        if (!updated) {
            return RestResponse.SYS_DATA_STATUS_ERROR;
        }

        // 刷新缓存
        cacheProxy.signalRefreshCache(SysCacheRefreshTypeEnum.API_WITHDRAW_CONFIG);

        return RestResponse.OK;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResponse enableDisableForAdmin(IdEnableDisableParam param) {
        boolean saved = lambdaUpdate()
                .eq(ApiWithdrawConfig::getId, param.getId())
                .set(ApiWithdrawConfig::getEnabled, param.getEnable())
                .update();

        if (!saved) {
            return RestResponse.SYS_DATA_STATUS_ERROR;
        }

        // 刷新缓存
        cacheProxy.signalRefreshCache(SysCacheRefreshTypeEnum.API_WITHDRAW_CONFIG);

        return RestResponse.OK;
    }
}
