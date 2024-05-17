package com.im.common.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.im.common.cache.base.CacheProxy;
import com.im.common.cache.impl.BankCache;
import com.im.common.entity.BankCardRechargeConfig;
import com.im.common.entity.BankCardRechargeConfigCard;
import com.im.common.entity.enums.PortalTypeEnum;
import com.im.common.entity.enums.SysCacheRefreshTypeEnum;
import com.im.common.mapper.BankCardRechargeConfigMapper;
import com.im.common.param.*;
import com.im.common.response.ResponseCode;
import com.im.common.response.RestResponse;
import com.im.common.service.BankCardRechargeConfigCardService;
import com.im.common.service.BankCardRechargeConfigService;
import com.im.common.service.GoogleAuthService;
import com.im.common.util.CollectionUtil;
import com.im.common.util.DateTimeUtil;
import com.im.common.util.NumberUtil;
import com.im.common.util.StrUtil;
import com.im.common.util.mybatis.page.PageVO;
import com.im.common.util.mybatis.service.MyBatisPlusServiceImpl;
import com.im.common.vo.AdminSessionUser;
import com.im.common.vo.BankCardRechargeConfigAdminVO;
import com.im.common.vo.BankCardRechargeConfigCardAdminVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

/**
 * 银行卡充值配置 服务实现类
 *
 * @author Barry
 * @date 2021-09-29
 */
@Service
public class BankCardRechargeConfigServiceImpl
        extends MyBatisPlusServiceImpl<BankCardRechargeConfigMapper, BankCardRechargeConfig>
        implements BankCardRechargeConfigService {

    private GoogleAuthService googleAuthService;
    private CacheProxy cacheProxy;
    private BankCardRechargeConfigCardService cardService;
    private BankCache bankCache;

    @Autowired
    public void setGoogleAuthService(GoogleAuthService googleAuthService) {
        this.googleAuthService = googleAuthService;
    }

    @Autowired
    public void setCacheProxy(CacheProxy cacheProxy) {
        this.cacheProxy = cacheProxy;
    }

    @Autowired
    public void setCardService(BankCardRechargeConfigCardService cardService) {
        this.cardService = cardService;
    }

    @Autowired
    public void setBankCache(BankCache bankCache) {
        this.bankCache = bankCache;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResponse addForAdmin(AdminSessionUser sessionUser, BankCardRechargeConfigAddParam param) {
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

        BankCardRechargeConfig config = new BankCardRechargeConfig();
        Long id = IdWorker.getId();
        config.setId(id);
        config.setName(param.getName());
        config.setAmountRange(param.getAmountRange());
        config.setAmountMaxPrecision(param.getAmountMaxPrecision());
        config.setServiceChargePercent(param.getServiceChargePercent());
        config.setEnabled(param.getEnabled());
        config.setEnableTime(param.getEnableTime());
        config.setNeedInputUserCardName(param.getNeedInputUserCardName());
        config.setSort(param.getSort());
        config.setDeleted(false);
        boolean saved = save(config);
        if (!saved) {
            return RestResponse.SYS_DATA_STATUS_ERROR;
        }

        // 保存卡配置
        cardService.adjustCards(id, param.getCardList());

        // 刷新缓存
        cacheProxy.signalRefreshCache(SysCacheRefreshTypeEnum.BANK_CARD_RECHARGE_CONFIG);

        return RestResponse.OK;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResponse editForAdmin(AdminSessionUser sessionUser, BankCardRechargeConfigEditParam param) {
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

        boolean updated = lambdaUpdate().eq(BankCardRechargeConfig::getId, param.getId())
                .set(BankCardRechargeConfig::getName, param.getName())
                .set(BankCardRechargeConfig::getAmountRange, param.getAmountRange())
                .set(BankCardRechargeConfig::getAmountMaxPrecision, param.getAmountMaxPrecision())
                .set(BankCardRechargeConfig::getServiceChargePercent, param.getServiceChargePercent())
                .set(BankCardRechargeConfig::getEnabled, param.getEnabled())
                .set(BankCardRechargeConfig::getEnableTime, param.getEnableTime())
                .set(BankCardRechargeConfig::getNeedInputUserCardName, param.getNeedInputUserCardName())
                .set(BankCardRechargeConfig::getSort, param.getSort())
                .update();
        if (!updated) {
            return RestResponse.SYS_DATA_STATUS_ERROR;
        }

        // 保存卡配置
        cardService.adjustCards(param.getId(), param.getCardList());

        // 刷新缓存
        cacheProxy.signalRefreshCache(SysCacheRefreshTypeEnum.BANK_CARD_RECHARGE_CONFIG);

        return RestResponse.OK;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, readOnly = true)
    public PageVO<BankCardRechargeConfigAdminVO> pageVOForAdmin(BankCardRechargeConfigPageParam param) {
        PageVO<BankCardRechargeConfigAdminVO> pageVO = pageVO(param, e -> new BankCardRechargeConfigAdminVO(e));

        if (CollectionUtil.isEmpty(pageVO.getRecords())) {
            return pageVO;
        }

        // 加载规则配置
        Set<Long> configIds = CollectionUtil.toSet(pageVO.getRecords(), e -> e.getId());
        List<BankCardRechargeConfigCard> cards = cardService.listByConfigIds(configIds);

        List<BankCardRechargeConfigAdminVO> list = pageVO.getRecords();
        for (BankCardRechargeConfigAdminVO config : list) {
            List<BankCardRechargeConfigCard> configCards = CollectionUtil.filterList(cards, e -> e.getConfigId().equals(config.getId()));

            List<BankCardRechargeConfigCardAdminVO> cardVOList = CollectionUtil.toList(configCards, e -> new BankCardRechargeConfigCardAdminVO(e, bankCache));

            config.setCardList(cardVOList);
        }

        return pageVO;
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
                .eq(BankCardRechargeConfig::getId, param.getId())
                .set(BankCardRechargeConfig::getDeleted, Boolean.TRUE)
                .update();
        if (!updated) {
            return RestResponse.SYS_DATA_STATUS_ERROR;
        }

        // 删除卡

        // 刷新缓存
        cacheProxy.signalRefreshCache(SysCacheRefreshTypeEnum.BANK_CARD_RECHARGE_CONFIG);

        return RestResponse.OK;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResponse enableDisableForAdmin(IdEnableDisableParam param) {
        boolean saved = lambdaUpdate()
                .eq(BankCardRechargeConfig::getId, param.getId())
                .set(BankCardRechargeConfig::getEnabled, param.getEnable())
                .update();

        if (!saved) {
            return RestResponse.SYS_DATA_STATUS_ERROR;
        }

        // 刷新缓存
        cacheProxy.signalRefreshCache(SysCacheRefreshTypeEnum.BANK_CARD_RECHARGE_CONFIG);

        return RestResponse.OK;
    }
}
