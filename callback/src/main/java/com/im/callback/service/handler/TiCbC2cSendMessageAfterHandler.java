package com.im.callback.service.handler;

import cn.hutool.core.date.LocalDateTimeUtil;
import com.im.callback.entity.TiCbMessageSendRequest;
import com.im.callback.service.TiCallbackHandler;
import com.im.common.cache.impl.PortalUserCache;
import com.im.common.entity.tim.TimMessageC2c;
import com.im.common.service.AppAutoReplyService;
import com.im.common.service.TimMessageC2cService;
import com.im.common.util.api.im.tencent.entity.result.TiBaseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 发送单聊消息后的回掉处理
 */
@Slf4j
@Component
public class TiCbC2cSendMessageAfterHandler implements TiCallbackHandler<TiCbMessageSendRequest> {

    private TimMessageC2cService timMessageC2cService;
    private PortalUserCache portalUserCache;
    private AppAutoReplyService appAutoReplyService;

    @Autowired
    public void setTimMessageC2cService(TimMessageC2cService timMessageC2cService) {
        this.timMessageC2cService = timMessageC2cService;
    }

    @Autowired
    public void setPortalUserCache(PortalUserCache portalUserCache) {
        this.portalUserCache = portalUserCache;
    }

    @Autowired
    public void setAppAutoReplyService(AppAutoReplyService appAutoReplyService) {
        this.appAutoReplyService = appAutoReplyService;
    }

    @Override
    public TiBaseResult handle(TiCbMessageSendRequest request, Map<String, Object> context) {
        log.info("接收到一个单聊发送消息后的事件:[{}]", request.toString());

        TimMessageC2c messageC2c = new TimMessageC2c();
        messageC2c.setFromUserId(portalUserCache.getIdByUsernameFromLocal(request.getFromAccount()));
        messageC2c.setToUserId(portalUserCache.getIdByUsernameFromLocal(request.getToAccount()));
        messageC2c.setClientIp(request.getClientIp());
        messageC2c.setSendTime(LocalDateTimeUtil.of(request.getMsgTime() * 1000));
        messageC2c.setMsgKey(request.getMsgKey());
        messageC2c.setMsgSeq(request.getMsgSeq());
        messageC2c.setMsgRandom(request.getMsgRandom());
        messageC2c.setMsgFromPlatform(request.getOptPlatform());

        timMessageC2cService.save(messageC2c, request.getMsgBody());

        //自动回复
        appAutoReplyService.autoReply(request.getToAccount(), request.getFromAccount());
        return TiBaseResult.success();
    }


}
