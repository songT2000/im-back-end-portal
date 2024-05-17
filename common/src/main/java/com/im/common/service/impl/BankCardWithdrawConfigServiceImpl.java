package com.im.common.service.impl;

import com.im.common.cache.base.CacheProxy;
import com.im.common.entity.BankCardWithdrawConfig;
import com.im.common.entity.enums.PortalTypeEnum;
import com.im.common.entity.enums.SysCacheRefreshTypeEnum;
import com.im.common.mapper.BankCardWithdrawConfigMapper;
import com.im.common.param.BankCardWithdrawConfigAddParam;
import com.im.common.param.BankCardWithdrawConfigEditParam;
import com.im.common.param.IdEnableDisableParam;
import com.im.common.param.IdGoogleParam;
import com.im.common.response.ResponseCode;
import com.im.common.response.RestResponse;
import com.im.common.service.BankCardWithdrawConfigService;
import com.im.common.service.GoogleAuthService;
import com.im.common.util.NumberUtil;
import com.im.common.util.mybatis.service.MyBatisPlusServiceImpl;
import com.im.common.vo.AdminSessionUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 银行卡提现配置 服务实现类
 *
 * @author Barry
 * @date 2021-09-29
 */
@Service
public class BankCardWithdrawConfigServiceImpl
        extends MyBatisPlusServiceImpl<BankCardWithdrawConfigMapper, BankCardWithdrawConfig>
        implements BankCardWithdrawConfigService {
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
    public RestResponse addForAdmin(AdminSessionUser sessionUser, BankCardWithdrawConfigAddParam param) {
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

        BankCardWithdrawConfig config = new BankCardWithdrawConfig();
        config.setName(param.getName());
        config.setAmountRange(param.getAmountRange());
        config.setAmountMaxPrecision(param.getAmountMaxPrecision());
        config.setServiceChargePercent(param.getServiceChargePercent());
        config.setEnabled(param.getEnabled());
        config.setSort(param.getSort());
        config.setDeleted(Boolean.FALSE);

        boolean saved = save(config);
        if (!saved) {
            return RestResponse.SYS_DATA_STATUS_ERROR;
        }

        // 刷新缓存
        cacheProxy.signalRefreshCache(SysCacheRefreshTypeEnum.BANK_CARD_WITHDRAW_CONFIG);

        return RestResponse.OK;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResponse editForAdmin(AdminSessionUser sessionUser, BankCardWithdrawConfigEditParam param) {
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

        boolean updated = lambdaUpdate().eq(BankCardWithdrawConfig::getId, param.getId())
                .set(BankCardWithdrawConfig::getName, param.getName())
                .set(BankCardWithdrawConfig::getAmountRange, param.getAmountRange())
                .set(BankCardWithdrawConfig::getAmountMaxPrecision, param.getAmountMaxPrecision())
                .set(BankCardWithdrawConfig::getServiceChargePercent, param.getServiceChargePercent())
                .set(BankCardWithdrawConfig::getSort, param.getSort())
                .set(BankCardWithdrawConfig::getEnabled, param.getEnabled())
                .update();

        if (!updated) {
            return RestResponse.SYS_DATA_STATUS_ERROR;
        }

        // 刷新缓存
        cacheProxy.signalRefreshCache(SysCacheRefreshTypeEnum.BANK_CARD_WITHDRAW_CONFIG);

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
                .eq(BankCardWithdrawConfig::getId, param.getId())
                .set(BankCardWithdrawConfig::getDeleted, Boolean.TRUE)
                .update();
        if (!updated) {
            return RestResponse.SYS_DATA_STATUS_ERROR;
        }

        // 刷新缓存
        cacheProxy.signalRefreshCache(SysCacheRefreshTypeEnum.BANK_CARD_WITHDRAW_CONFIG);

        return RestResponse.OK;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResponse enableDisableForAdmin(IdEnableDisableParam param) {
        boolean saved = lambdaUpdate()
                .eq(BankCardWithdrawConfig::getId, param.getId())
                .set(BankCardWithdrawConfig::getEnabled, param.getEnable())
                .update();

        if (!saved) {
            return RestResponse.SYS_DATA_STATUS_ERROR;
        }

        // 刷新缓存
        cacheProxy.signalRefreshCache(SysCacheRefreshTypeEnum.BANK_CARD_WITHDRAW_CONFIG);

        return RestResponse.OK;
    }
}
