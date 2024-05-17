package com.im.common.service.impl;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.CharUtil;
import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSON;
import com.im.common.cache.impl.AppAutoReplyConfigCache;
import com.im.common.entity.AppAutoReplyConfig;
import com.im.common.entity.enums.CustomMessageTypeEnum;
import com.im.common.entity.enums.MsgTypeEnum;
import com.im.common.entity.enums.TiMsgTypeEnum;
import com.im.common.entity.tim.TimMessageElemImage;
import com.im.common.service.AppAutoReplyService;
import com.im.common.util.CollectionUtil;
import com.im.common.util.DateTimeUtil;
import com.im.common.util.RandomUtil;
import com.im.common.util.StrUtil;
import com.im.common.util.api.im.tencent.entity.param.message.TIMTextElem;
import com.im.common.util.api.im.tencent.entity.param.message.TiMessage;
import com.im.common.util.api.im.tencent.entity.param.message.TiMsgBody;
import com.im.common.util.api.im.tencent.entity.param.message.TiMsgCustomItem;
import com.im.common.util.api.im.tencent.service.rest.TiSingleChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 自动回复消息配置 服务实现类
 */
@Service
public class AppAutoReplyServiceImpl implements AppAutoReplyService {
    private AppAutoReplyConfigCache appAutoReplyConfigCache;
    private TiSingleChatService tiSingleChatService;

    @Autowired
    public void setAppAutoReplyConfigCache(AppAutoReplyConfigCache appAutoReplyConfigCache) {
        this.appAutoReplyConfigCache = appAutoReplyConfigCache;
    }

    @Autowired
    public void setTiSingleChatService(TiSingleChatService tiSingleChatService) {
        this.tiSingleChatService = tiSingleChatService;
    }

    /**
     * 自动回复
     *
     * @param fromAccount 自动回复的发送人
     * @param toAccount   自动回复的接收人
     */
    @Async
    @Override
    public void autoReply(String fromAccount, String toAccount) {

        List<AppAutoReplyConfig> configs = appAutoReplyConfigCache.listFromRedis();
        if (CollectionUtil.isNotEmpty(configs)) {

            List<String> allAccounts = configs.stream().flatMap(p -> StrUtil.split(p.getUsernames(), CharUtil.COMMA).stream()).collect(Collectors.toList());
            //判断接收人是否也在配置名单内，防止循环发送
            if (allAccounts.contains(toAccount)) {
                //接收人在自动回复账号名单内
                return;
            }

            for (AppAutoReplyConfig config : configs) {
                //判断是否包含fromAccount
                List<String> accounts = StrUtil.split(config.getUsernames(), CharUtil.COMMA);
                if (!accounts.contains(fromAccount)) {
                    //不在自动回复账号名单内
                    continue;
                }
                //判断是否在自动回复时间范围内
                String enableTime = config.getStartTime() + "~" + config.getEndTime();
                if (!DateTimeUtil.isDuringServiceTime(enableTime)) {
                    //不在自动回复时间范围内
                    continue;
                }
                //休眠1s再发自动消息
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (config.getMsgType().equals(MsgTypeEnum.TIMTextElem)) {
                    //文本消息
                    TiMsgBody tiMsgBody = new TiMsgBody();
                    tiMsgBody.setMsgType(TiMsgTypeEnum.TIMTextElem);
                    tiMsgBody.setMsgContent(new TIMTextElem(config.getContent()));
                    TiMessage tiMessage = new TiMessage(fromAccount, toAccount, RandomUtil.randomInt(100000), ListUtil.of(tiMsgBody));
                    tiSingleChatService.sendMessage(tiMessage);
                }
                if (config.getMsgType().equals(MsgTypeEnum.TIMImageElem)) {
                    //图片消息
                    TimMessageElemImage elemImage = JSON.parseObject(config.getNote(), TimMessageElemImage.class);
                    elemImage.setUuid(IdUtil.fastSimpleUUID());
                    TiMsgCustomItem tiMsgCustomItem = new TiMsgCustomItem(CustomMessageTypeEnum.IMAGE.getVal(), elemImage, fromAccount);
                    TiMessage tiMessage = new TiMessage(fromAccount, toAccount, tiMsgCustomItem);
                    tiSingleChatService.sendMessage(tiMessage);
                }
            }
        }
    }
}
