package com.im.common.service.impl;

import com.im.common.cache.base.CacheProxy;
import com.im.common.entity.ApiRechargeConfig;
import com.im.common.entity.enums.PortalTypeEnum;
import com.im.common.entity.enums.SysCacheRefreshTypeEnum;
import com.im.common.mapper.ApiRechargeConfigMapper;
import com.im.common.param.ApiRechargeConfigAddParam;
import com.im.common.param.ApiRechargeConfigEditParam;
import com.im.common.param.IdEnableDisableParam;
import com.im.common.param.IdGoogleParam;
import com.im.common.response.ResponseCode;
import com.im.common.response.RestResponse;
import com.im.common.service.ApiRechargeConfigService;
import com.im.common.service.GoogleAuthService;
import com.im.common.util.DateTimeUtil;
import com.im.common.util.NumberUtil;
import com.im.common.util.StrUtil;
import com.im.common.util.api.pay.base.recharge.ApiRechargeHandler;
import com.im.common.util.api.pay.base.recharge.ApiRechargeHandlerFactory;
import com.im.common.util.mybatis.service.MyBatisPlusServiceImpl;
import com.im.common.vo.AdminSessionUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 三方充值配置 服务实现类
 *
 * @author Barry
 * @date 2021-09-29
 */
@Service
public class ApiRechargeConfigServiceImpl
        extends MyBatisPlusServiceImpl<ApiRechargeConfigMapper, ApiRechargeConfig>
        implements ApiRechargeConfigService {

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
    public RestResponse addForAdmin(AdminSessionUser sessionUser, ApiRechargeConfigAddParam param) {
        // 检查谷歌
        {
            RestResponse googleRsp = googleAuthService.authoriseGoogle(sessionUser.getUsername(), PortalTypeEnum.ADMIN, param.getGoogleCode());
            if (!googleRsp.isOkRsp()) {
                return googleRsp;
            }
        }

        // 验证金额范围
        if (!NumberUtil.isMultipleAmountRangeStr(param.getAmountRange())) {
            return RestResponse.failed(ResponseCode.SYS_AMOUNT_RANGE_FORMATTING_INCORRECT);
        }

        // 启用时间段
        if (StrUtil.isNotBlank(param.getEnableTime()) && !DateTimeUtil.isServiceTimeFormat(param.getEnableTime())) {
            return RestResponse.failed(ResponseCode.SYS_TIME_RANGE_FORMATTING_INCORRECT);
        }

        // 验证三方配置是否正确
        ApiRechargeHandler rechargeHandler = ApiRechargeHandlerFactory.getRechargeHandler(param.getCode());
        if (rechargeHandler == null) {
            return RestResponse.failed(ResponseCode.API_RECHARGE_HANDLER_NOT_FOUND);
        }
        if (!rechargeHandler.isValidRechargeThirdConfig(param.getThirdConfig())) {
            return RestResponse.failed(ResponseCode.API_RECHARGE_THIRD_CONFIG_FORMATTING_INCORRECT);
        }

        ApiRechargeConfig config = new ApiRechargeConfig();
        config.setGroup(param.getGroup());
        config.setAdminName(param.getAdminName());
        config.setPortalName(param.getPortalName());
        config.setCode(param.getCode());
        config.setAmountRange(param.getAmountRange());
        config.setAmountMaxPrecision(param.getAmountMaxPrecision());
        config.setServiceChargePercent(param.getServiceChargePercent());
        config.setEnabled(param.getEnabled());
        config.setEnableTime(param.getEnableTime());
        config.setNeedInputUserCardName(param.getNeedInputUserCardName());
        config.setThirdConfig(param.getThirdConfig());
        config.setThirdCallbackWhitelistIp(param.getThirdCallbackWhitelistIp());
        config.setSort(param.getSort());
        config.setDeleted(false);
        boolean saved = save(config);
        if (!saved) {
            return RestResponse.SYS_DATA_STATUS_ERROR;
        }

        // 刷新缓存
        cacheProxy.signalRefreshCache(SysCacheRefreshTypeEnum.API_RECHARGE_CONFIG);

        return RestResponse.OK;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResponse editForAdmin(AdminSessionUser sessionUser, ApiRechargeConfigEditParam param) {
        // 检查谷歌
        {
            RestResponse googleRsp = googleAuthService.authoriseGoogle(sessionUser.getUsername(), PortalTypeEnum.ADMIN, param.getGoogleCode());
            if (!googleRsp.isOkRsp()) {
                return googleRsp;
            }
        }

        // 验证金额范围
        if (!NumberUtil.isMultipleAmountRangeStr(param.getAmountRange())) {
            return RestResponse.failed(ResponseCode.SYS_AMOUNT_RANGE_FORMATTING_INCORRECT);
        }

        // 启用时间段
        if (StrUtil.isNotBlank(param.getEnableTime()) && !DateTimeUtil.isServiceTimeFormat(param.getEnableTime())) {
            return RestResponse.failed(ResponseCode.SYS_TIME_RANGE_FORMATTING_INCORRECT);
        }

        // 验证三方配置是否正确
        ApiRechargeHandler rechargeHandler = ApiRechargeHandlerFactory.getRechargeHandler(param.getCode());
        if (rechargeHandler == null) {
            return RestResponse.failed(ResponseCode.API_RECHARGE_HANDLER_NOT_FOUND);
        }
        if (!rechargeHandler.isValidRechargeThirdConfig(param.getThirdConfig())) {
            return RestResponse.failed(ResponseCode.API_RECHARGE_THIRD_CONFIG_FORMATTING_INCORRECT);
        }

        boolean updated = lambdaUpdate().eq(ApiRechargeConfig::getId, param.getId())
                .set(ApiRechargeConfig::getGroup, param.getGroup())
                .set(ApiRechargeConfig::getAdminName, param.getAdminName())
                .set(ApiRechargeConfig::getPortalName, param.getPortalName())
                .set(ApiRechargeConfig::getCode, param.getCode())
                .set(ApiRechargeConfig::getServiceChargePercent, param.getServiceChargePercent())
                .set(ApiRechargeConfig::getAmountRange, param.getAmountRange())
                .set(ApiRechargeConfig::getAmountMaxPrecision, param.getAmountMaxPrecision())
                .set(ApiRechargeConfig::getEnabled, param.getEnabled())
                .set(ApiRechargeConfig::getEnableTime, param.getEnableTime())
                .set(ApiRechargeConfig::getNeedInputUserCardName, param.getNeedInputUserCardName())
                .set(ApiRechargeConfig::getThirdConfig, param.getThirdConfig())
                .set(ApiRechargeConfig::getThirdCallbackWhitelistIp, param.getThirdCallbackWhitelistIp())
                .set(ApiRechargeConfig::getSort, param.getSort())
                .update();

        if (!updated) {
            return RestResponse.SYS_DATA_STATUS_ERROR;
        }

        // 刷新缓存
        cacheProxy.signalRefreshCache(SysCacheRefreshTypeEnum.API_RECHARGE_CONFIG);

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
                .eq(ApiRechargeConfig::getId, param.getId())
                .set(ApiRechargeConfig::getDeleted, Boolean.TRUE)
                .update();
        if (!updated) {
            return RestResponse.SYS_DATA_STATUS_ERROR;
        }

        // 刷新缓存
        cacheProxy.signalRefreshCache(SysCacheRefreshTypeEnum.API_RECHARGE_CONFIG);

        return RestResponse.OK;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResponse enableDisableForAdmin(IdEnableDisableParam param) {
        boolean saved = lambdaUpdate()
                .eq(ApiRechargeConfig::getId, param.getId())
                .set(ApiRechargeConfig::getEnabled, param.getEnable())
                .update();

        if (!saved) {
            return RestResponse.SYS_DATA_STATUS_ERROR;
        }

        // 刷新缓存
        cacheProxy.signalRefreshCache(SysCacheRefreshTypeEnum.API_RECHARGE_CONFIG);

        return RestResponse.OK;
    }
}
