package com.im.common.service.impl;

import com.im.common.cache.impl.BankCache;
import com.im.common.cache.impl.PortalUserCache;
import com.im.common.cache.impl.SysConfigCache;
import com.im.common.cache.impl.UserBankCardBlackCache;
import com.im.common.cache.sysconfig.bo.WithdrawConfigBO;
import com.im.common.constant.CommonConstant;
import com.im.common.entity.Bank;
import com.im.common.entity.PortalUser;
import com.im.common.entity.UserBankCard;
import com.im.common.entity.enums.PortalTypeEnum;
import com.im.common.mapper.PortalUserMapper;
import com.im.common.mapper.UserBankCardMapper;
import com.im.common.param.IdEnableDisableParam;
import com.im.common.param.IdParam;
import com.im.common.param.PortalUserBankCardAddParam;
import com.im.common.param.UserBankCardAddAdminParam;
import com.im.common.response.ResponseCode;
import com.im.common.response.RestResponse;
import com.im.common.service.BankService;
import com.im.common.service.UserBankCardService;
import com.im.common.util.CollectionUtil;
import com.im.common.util.NumberUtil;
import com.im.common.util.PasswordUtil;
import com.im.common.util.StrUtil;
import com.im.common.util.mybatis.service.MyBatisPlusServiceImpl;
import com.im.common.util.user.UserUtil;
import com.im.common.vo.PortalSessionUser;
import com.im.common.vo.UserBankCardPortalVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 用户银行卡 服务实现类
 *
 * @author Barry
 * @date 2018/5/18
 */
@Service
public class UserBankCardServiceImpl
        extends MyBatisPlusServiceImpl<UserBankCardMapper, UserBankCard>
        implements UserBankCardService {
    private PortalUserMapper portalUserMapper;
    private BankService bankService;
    private SysConfigCache sysConfigCache;
    private UserBankCardBlackCache userBankCardBlackCache;
    private BankCache bankCache;
    private PortalUserCache portalUserCache;

    @Autowired
    public void setPortalUserMapper(PortalUserMapper portalUserMapper) {
        this.portalUserMapper = portalUserMapper;
    }

    @Autowired
    public void setUserBankCardBlackCache(UserBankCardBlackCache userBankCardBlackCache) {
        this.userBankCardBlackCache = userBankCardBlackCache;
    }

    @Autowired
    public void setBankService(BankService bankService) {
        this.bankService = bankService;
    }

    @Autowired
    public void setSysConfigCache(SysConfigCache sysConfigCache) {
        this.sysConfigCache = sysConfigCache;
    }

    @Autowired
    public void setBankCache(BankCache bankCache) {
        this.bankCache = bankCache;
    }

    @Autowired
    public void setPortalUserCache(PortalUserCache portalUserCache) {
        this.portalUserCache = portalUserCache;
    }

    @Override
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public UserBankCard getByUserIdAndCardId(long userId, long cardId) {
        return lambdaQuery()
                .eq(UserBankCard::getUserId, userId)
                .eq(UserBankCard::getId, cardId)
                .one();
    }

    @Override
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public List<UserBankCardPortalVO> listVOForPortal(PortalSessionUser sessionUser) {
        List<UserBankCard> list = lambdaQuery().eq(UserBankCard::getUserId, sessionUser.getId()).list();

        return CollectionUtil.toList(list, e -> new UserBankCardPortalVO(e, bankCache, portalUserCache));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResponse addForPortal(PortalSessionUser sessionUser, PortalUserBankCardAddParam param) {
        // 卡黑名单
        RestResponse cardBlackRsp = userBankCardBlackCache.checkUserBankCardBlack(param.getCardNum());
        if (!cardBlackRsp.isOkRsp()) {
            return cardBlackRsp;
        }


        PortalUser user = portalUserMapper.selectById(sessionUser.getId());

        // 必须先绑定提现姓名
        if (StrUtil.isBlank(user.getWithdrawName())) {
            return RestResponse.failed(ResponseCode.USER_WITHDRAW_NAME_NOT_YET_BIND);
        }

        // 必须绑定资金密码
        if (StrUtil.isBlank(user.getFundPwd())) {
            return RestResponse.failed(ResponseCode.USER_FUND_PASSWORD_NOT_YET_BIND);
        }

        // 验证资金密码
        if (!PasswordUtil.validatePwd(user.getFundPwd(), param.getFundPwd())) {
            return RestResponse.failed(ResponseCode.USER_FUND_PASSWORD_INCORRECT);
        }

        String cardNum = StrUtil.cleanBlank(param.getCardNum());
        if (StrUtil.isBlank(cardNum) || !NumberUtil.isNumber(cardNum)) {
            return RestResponse.failed(ResponseCode.SYS_REQUEST_PARAM_ERROR);
        }

        // 检查卡号是否存在
        Integer cardNumCount = lambdaQuery().eq(UserBankCard::getCardNum, cardNum).count();
        if (NumberUtil.isGreatThenZero(cardNumCount)) {
            return RestResponse.failed(ResponseCode.USER_BANK_CARD_CARD_NUM_EXISTED);
        }

        // 检查银行是否存在，银行提现是否启用
        Bank bank = bankService.getById(param.getBankId());
        if (bank == null) {
            return RestResponse.failed(ResponseCode.BANK_NOT_FOUND);
        }
        if (!Boolean.TRUE.equals(bank.getWithdrawEnabled())) {
            return RestResponse.failed(ResponseCode.BANK_WITHDRAW_DISABLED);
        }

        // 最多绑定银行数量，忽略状态
        WithdrawConfigBO withdrawConfig = sysConfigCache.getWithdrawConfigFromRedis();
        if (NumberUtil.isGreatThenZero(withdrawConfig.getMaxBindBankCardCount())) {
            Integer alreadyBindCount = lambdaQuery()
                    .eq(UserBankCard::getUserId, sessionUser.getId())
                    .count();
            alreadyBindCount = Optional.ofNullable(alreadyBindCount).orElse(CommonConstant.INT_0);
            if (NumberUtil.isGreaterOrEqual(alreadyBindCount, withdrawConfig.getMaxBindBankCardCount())) {
                return RestResponse.failed(ResponseCode.USER_BANK_CARD_MAX_BIND_COUNT_EXCEEDED,
                        withdrawConfig.getMaxBindBankCardCount(), alreadyBindCount);
            }
        }

        UserBankCard card = new UserBankCard();
        card.setUserId(sessionUser.getId());
        card.setBankId(bank.getId());
        card.setCardNum(cardNum);
        card.setBranch(StrUtil.trim(param.getBranch()));
        // 默认启用
        card.setEnabled(true);

        boolean saved = save(card);

        return saved ? RestResponse.OK : RestResponse.SYS_DATA_STATUS_ERROR;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResponse deleteForPortal(PortalSessionUser sessionUser, IdParam param) {
        boolean removed = lambdaUpdate()
                .eq(UserBankCard::getId, param.getId())
                .eq(UserBankCard::getUserId, sessionUser.getId())
                .remove();

        return removed ? RestResponse.OK : RestResponse.failed(ResponseCode.SYS_DATA_NOT_EXIST);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResponse addForAdmin(UserBankCardAddAdminParam param) {
        // 卡黑名单
        RestResponse cardBlackRsp = userBankCardBlackCache.checkUserBankCardBlack(param.getCardNum());
        if (!cardBlackRsp.isOkRsp()) {
            return cardBlackRsp;
        }
        // 用户是否存在
        Long userId = UserUtil.getUserIdByUsernameFromLocal(param.getUsername(), PortalTypeEnum.PORTAL);
        if (userId == null) {
            return RestResponse.failed(ResponseCode.USER_NOT_FOUND);
        }

        PortalUser user = portalUserMapper.selectById(userId);

        // 必须先绑定提现姓名
        if (StrUtil.isBlank(user.getWithdrawName())) {
            return RestResponse.failed(ResponseCode.USER_WITHDRAW_NAME_NOT_YET_BIND);
        }

        // 检查银行卡
        String cardNum = StrUtil.cleanBlank(param.getCardNum());
        if (StrUtil.isBlank(cardNum) || !NumberUtil.isNumber(cardNum)) {
            return RestResponse.failed(ResponseCode.SYS_REQUEST_PARAM_ERROR);
        }

        // 检查卡号是否存在
        Integer cardNumCount = lambdaQuery().eq(UserBankCard::getCardNum, cardNum).count();
        if (NumberUtil.isGreatThenZero(cardNumCount)) {
            return RestResponse.failed(ResponseCode.USER_BANK_CARD_CARD_NUM_EXISTED);
        }

        Bank bank = bankService.getById(param.getBankId());
        if (bank == null) {
            return RestResponse.failed(ResponseCode.BANK_NOT_FOUND);
        }

        UserBankCard userBankCard = new UserBankCard();
        userBankCard.setUserId(userId);
        userBankCard.setBankId(param.getBankId());
        userBankCard.setCardNum(cardNum);
        userBankCard.setBranch(StrUtil.trim(param.getBranch()));
        userBankCard.setEnabled(param.getEnabled());

        // 保存
        boolean saved = save(userBankCard);
        return saved ? RestResponse.OK : RestResponse.SYS_DATA_STATUS_ERROR;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResponse enableDisableForAdmin(IdEnableDisableParam param) {
        boolean saved = lambdaUpdate()
                .eq(UserBankCard::getId, param.getId())
                .set(UserBankCard::getEnabled, param.getEnable())
                .update();

        return saved ? RestResponse.OK : RestResponse.SYS_DATA_STATUS_ERROR;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResponse deleteForAdmin(IdParam param) {
        boolean removed = lambdaUpdate()
                .eq(UserBankCard::getId, param.getId())
                .remove();
        return removed ? RestResponse.OK : RestResponse.failed(ResponseCode.SYS_DATA_NOT_EXIST);
    }

    @Override
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public int countUserEnabledBankCard(long userId) {
        Integer count = lambdaQuery()
                .eq(UserBankCard::getUserId, userId)
                .eq(UserBankCard::getEnabled, true)
                .count();
        return NumberUtil.nullOrZero(count);
    }
}
