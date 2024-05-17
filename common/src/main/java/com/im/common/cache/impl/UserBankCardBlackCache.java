package com.im.common.cache.impl;

import com.im.common.cache.base.BaseCacheHandler;
import com.im.common.cache.base.CacheProperty;
import com.im.common.entity.UserBankCardBlack;
import com.im.common.entity.enums.SysCacheRefreshTypeEnum;
import com.im.common.response.ResponseCode;
import com.im.common.response.RestResponse;
import com.im.common.service.UserBankCardBlackService;
import com.im.common.util.StrUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * <p>平台银行卡黑名单缓存</p>
 *
 * @author Barry
 * @date 2020-06-16
 */
@CacheProperty(value = SysCacheRefreshTypeEnum.USER_BANK_CARD_BLACK, redis = false, local = true)
@Component
public class UserBankCardBlackCache implements BaseCacheHandler {
    private UserBankCardBlackService userBankCardBlackService;

    private Set<String> LOCAL_CARD_NUM_CACHE = new HashSet<>();

    @Autowired
    public void setUserBankCardBlackService(UserBankCardBlackService userBankCardBlackService) {
        this.userBankCardBlackService = userBankCardBlackService;
    }

    @Override
    public void reloadLocal() {
        loadAndCache();
    }

    /**
     * 是否是黑名单卡号
     *
     * @param cardNum
     * @return
     */
    public RestResponse checkUserBankCardBlack(String cardNum) {
        String realCardNum = StrUtil.cleanBlank(cardNum);
        if (LOCAL_CARD_NUM_CACHE.contains(realCardNum)) {
            return RestResponse.failed(ResponseCode.USER_BANK_CARD_NUM_IN_BLACKLIST, realCardNum);
        }
        return RestResponse.OK;
    }

    /**
     * 加载并缓存数据
     */
    private void loadAndCache() {
        List<UserBankCardBlack> list = userBankCardBlackService.list();

        Optional.ofNullable(list).ifPresent(this::resolveList);
    }

    /**
     * 解析并缓存数据
     *
     * @param list List
     */
    private void resolveList(List<UserBankCardBlack> list) {
        Set<String> cardNumMap = new HashSet<>();

        // 去除所有空格
        for (UserBankCardBlack userBankCardBlack : list) {
            String realCardNum = StrUtil.cleanBlank(userBankCardBlack.getCardNum());
            if (StrUtil.isNotBlank(realCardNum)) {
                cardNumMap.add(realCardNum);
            }
        }

        this.LOCAL_CARD_NUM_CACHE = cardNumMap;
    }
}
