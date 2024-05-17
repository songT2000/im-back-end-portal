package com.im.common.service.impl;

import com.im.common.cache.base.CacheProxy;
import com.im.common.entity.UserBankCardBlack;
import com.im.common.entity.enums.SysCacheRefreshTypeEnum;
import com.im.common.mapper.UserBankCardBlackMapper;
import com.im.common.param.IdParam;
import com.im.common.param.UserBankCardBlackAddAdminParam;
import com.im.common.param.UserBankCardBlackEditAdminParam;
import com.im.common.response.ResponseCode;
import com.im.common.response.RestResponse;
import com.im.common.service.UserBankCardBlackService;
import com.im.common.util.StrUtil;
import com.im.common.util.mybatis.service.MyBatisPlusServiceImpl;
import com.im.common.vo.AdminSessionUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户卡黑名单 服务实现类
 *
 * @author Barry
 * @date 2018/5/18
 */
@Service
public class UserBankCardBlackServiceImpl
        extends MyBatisPlusServiceImpl<UserBankCardBlackMapper, UserBankCardBlack>
        implements UserBankCardBlackService {
    private CacheProxy cacheProxy;

    @Autowired
    public void setCacheProxy(CacheProxy cacheProxy) {
        this.cacheProxy = cacheProxy;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResponse addForAdmin(AdminSessionUser sessionUser, UserBankCardBlackAddAdminParam param) {
        String cardName = StrUtil.trim(param.getCardName());
        String cardNum = StrUtil.cleanBlank(param.getCardNum());
        if (StrUtil.isBlank(cardNum)) {
            return RestResponse.failed(ResponseCode.SYS_REQUEST_PARAM_ERROR);
        }

        // 检查银行卡是否存在
        UserBankCardBlack one = lambdaQuery().eq(UserBankCardBlack::getCardNum, cardNum).one();
        if (one != null) {
            return RestResponse.failed(ResponseCode.SYS_DATA_EXISTED);
        }

        UserBankCardBlack card = new UserBankCardBlack();
        card.setCardName(cardName);
        card.setCardNum(cardNum);
        card.setCreateAdminId(sessionUser.getId());
        card.setRemark(StrUtil.trim(param.getRemark()));

        boolean saved = save(card);

        cacheProxy.signalRefreshCache(SysCacheRefreshTypeEnum.USER_BANK_CARD_BLACK);

        return saved ? RestResponse.OK : RestResponse.SYS_DATA_STATUS_ERROR;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResponse editForAdmin(AdminSessionUser sessionUser, UserBankCardBlackEditAdminParam param) {
        String cardName = StrUtil.trim(param.getCardName());
        String cardNum = StrUtil.cleanBlank(param.getCardNum());
        if (StrUtil.isBlank(cardNum)) {
            return RestResponse.failed(ResponseCode.SYS_REQUEST_PARAM_ERROR);
        }

        // 检查银行卡是否存在
        UserBankCardBlack one = lambdaQuery().eq(UserBankCardBlack::getCardNum, cardNum).ne(UserBankCardBlack::getId, param.getId()).one();
        if (one != null) {
            return RestResponse.failed(ResponseCode.SYS_DATA_EXISTED);
        }

        boolean updated = lambdaUpdate()
                .eq(UserBankCardBlack::getId, param.getId())
                .set(UserBankCardBlack::getCardName, cardName)
                .set(UserBankCardBlack::getCardNum, cardNum)
                .set(UserBankCardBlack::getUpdateAdminId, sessionUser.getId())
                .set(UserBankCardBlack::getRemark, StrUtil.trim(param.getRemark()))
                .update();

        cacheProxy.signalRefreshCache(SysCacheRefreshTypeEnum.USER_BANK_CARD_BLACK);

        return updated ? RestResponse.OK : RestResponse.SYS_DATA_STATUS_ERROR;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResponse deleteForAdmin(IdParam param) {
        boolean removed = lambdaUpdate().eq(UserBankCardBlack::getId, param.getId()).remove();

        cacheProxy.signalRefreshCache(SysCacheRefreshTypeEnum.USER_BANK_CARD_BLACK);

        return removed ? RestResponse.OK : RestResponse.SYS_DATA_STATUS_ERROR;
    }
}
