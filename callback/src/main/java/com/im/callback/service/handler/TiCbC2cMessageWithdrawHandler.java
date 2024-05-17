package com.im.callback.service.handler;

import com.im.callback.entity.TiCbMessageWithdrawRequest;
import com.im.callback.service.TiCallbackHandler;
import com.im.common.cache.impl.PortalUserCache;
import com.im.common.service.TimMessageC2cService;
import com.im.common.util.api.im.tencent.entity.result.TiBaseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 单聊消息撤回之后的回掉处理
 */
@Slf4j
@Component
public class TiCbC2cMessageWithdrawHandler implements TiCallbackHandler<TiCbMessageWithdrawRequest> {

    private TimMessageC2cService timMessageC2cService;
    private PortalUserCache portalUserCache;

    @Autowired
    public void setTimMessageC2cService(TimMessageC2cService timMessageC2cService) {
        this.timMessageC2cService = timMessageC2cService;
    }

    @Autowired
    public void setPortalUserCache(PortalUserCache portalUserCache) {
        this.portalUserCache = portalUserCache;
    }

    @Override
    public TiBaseResult handle(TiCbMessageWithdrawRequest request, Map<String, Object> context) {
        log.info("接收到一个单聊消息撤回之后的事件:[{}]", request.toString());
        Long fromUserId = portalUserCache.getIdByUsernameFromLocal(request.getFromAccount());
        Long toUserId = portalUserCache.getIdByUsernameFromLocal(request.getToAccount());
        timMessageC2cService.withdraw(fromUserId, toUserId, request.getMsgKey());
        return TiBaseResult.success();
    }
}
