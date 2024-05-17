package com.im.callback.service.handler;

import cn.hutool.core.date.LocalDateTimeUtil;
import com.im.callback.entity.TiCbGroupMessageSendRequest;
import com.im.common.cache.impl.PortalUserCache;
import com.im.common.entity.tim.TimMessageGroup;
import com.im.common.service.TimMessageGroupService;
import com.im.common.util.api.im.tencent.entity.result.TiBaseResult;
import com.im.callback.service.TiCallbackHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 *  群组消息发送之后回调处理
 */
@Slf4j
@Component
public class TiCbGroupSendMessageAfterHandler implements TiCallbackHandler<TiCbGroupMessageSendRequest> {

    private TimMessageGroupService timMessageGroupService;
    private PortalUserCache portalUserCache;

    @Autowired
    public void setTimMessageGroupService(TimMessageGroupService timMessageGroupService) {
        this.timMessageGroupService = timMessageGroupService;
    }

    @Autowired
    public void setPortalUserCache(PortalUserCache portalUserCache) {
        this.portalUserCache = portalUserCache;
    }



    @Override
    public TiBaseResult handle(TiCbGroupMessageSendRequest request, Map<String, Object> context) {
        log.info("接收到一个群组消息发送之后的事件:[{}]", request.toString());
        TimMessageGroup messageGroup = new TimMessageGroup();
        messageGroup.setFromUserId(portalUserCache.getIdByUsernameFromLocal(request.getFromAccount()));
        messageGroup.setGroupId(request.getGroupId());
        messageGroup.setClientIp(request.getClientIp());
        messageGroup.setSendTime(LocalDateTimeUtil.of(request.getMsgTime()*1000));
        messageGroup.setMsgSeq(request.getMsgSeq());
        messageGroup.setMsgRandom(request.getRandom());
        messageGroup.setMsgFromPlatform(request.getOptPlatform());

        timMessageGroupService.save(messageGroup,request.getMsgBody());
        return TiBaseResult.success();
    }
}
