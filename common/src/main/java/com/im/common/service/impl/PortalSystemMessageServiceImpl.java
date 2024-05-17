package com.im.common.service.impl;

import cn.hutool.core.collection.ListUtil;
import com.im.common.cache.impl.SysConfigCache;
import com.im.common.cache.sysconfig.bo.GlobalConfigBO;
import com.im.common.entity.enums.TiMsgTypeEnum;
import com.im.common.service.PortalSystemMessageService;
import com.im.common.util.CollectionUtil;
import com.im.common.util.RandomUtil;
import com.im.common.util.StrUtil;
import com.im.common.util.api.im.tencent.entity.param.message.TIMTextElem;
import com.im.common.util.api.im.tencent.entity.param.message.TiMessage;
import com.im.common.util.api.im.tencent.entity.param.message.TiMsgBody;
import com.im.common.util.api.im.tencent.service.rest.TiSingleChatService;
import com.im.common.util.i18n.I18nTranslateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PortalSystemMessageServiceImpl implements PortalSystemMessageService {

    private SysConfigCache sysConfigCache;
    private TiSingleChatService tiSingleChatService;

    @Autowired
    public void setSysConfigCache(SysConfigCache sysConfigCache) {
        this.sysConfigCache = sysConfigCache;
    }

    @Autowired
    public void setTiSingleChatService(TiSingleChatService tiSingleChatService) {
        this.tiSingleChatService = tiSingleChatService;
    }

    @Async
    @Override
    public void sendWithdrawMessage() {
        try {
            List<String> accountList = getPortalAdminAccountList();
            if (CollectionUtil.isNotEmpty(accountList)) {
                for (String account : accountList) {
                    String content = I18nTranslateUtil.translate("RSP_MSG.WITHDRAW_SYSTEM_MESSAGE#I18N");
                    TiMsgBody msgBody = new TiMsgBody(TiMsgTypeEnum.TIMTextElem, new TIMTextElem(content));
                    TiMessage message = new TiMessage(account, RandomUtil.randomInt(100000), ListUtil.of(msgBody));
                    tiSingleChatService.sendMessage(message);
                }
            }
        } catch (Exception e) {
            //非必要操作，不抛异常
        }

    }

    @Async
    @Override
    public void sendRechargeMessage() {
        try {
            List<String> accountList = getPortalAdminAccountList();
            if (CollectionUtil.isNotEmpty(accountList)) {
                for (String account : accountList) {
                    String content = I18nTranslateUtil.translate("RSP_MSG.RECHARGE_SYSTEM_MESSAGE#I18N");
                    TiMsgBody msgBody = new TiMsgBody(TiMsgTypeEnum.TIMTextElem, new TIMTextElem(content));
                    TiMessage message = new TiMessage(account, RandomUtil.randomInt(100000), ListUtil.of(msgBody));
                    tiSingleChatService.sendMessage(message);
                }
            }
        } catch (Exception e) {
            //非必要操作，不抛任何异常
        }
    }

    private List<String> getPortalAdminAccountList() {
        GlobalConfigBO globalConfigBO = sysConfigCache.getGlobalConfigFromLocal();
        String portalAdminAccount = globalConfigBO.getPortalAdminAccount();
        if (StrUtil.isNotBlank(portalAdminAccount)) {
            return CollectionUtil.splitStrToList(portalAdminAccount.replaceAll("，", ","), ",");//替换全角逗号后再分割账号
        }
        return new ArrayList<>();
    }
}
