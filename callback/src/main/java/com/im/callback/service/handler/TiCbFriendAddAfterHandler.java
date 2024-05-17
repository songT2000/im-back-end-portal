package com.im.callback.service.handler;

import com.im.callback.entity.TiCbFriendAddAfterRequest;
import com.im.callback.service.TiCallbackHandler;
import com.im.callback.util.PortalUserAppOperationLogConvert;
import com.im.common.cache.impl.PortalUserCache;
import com.im.common.entity.tim.TimFriend;
import com.im.common.service.PortalUserAppOperationLogService;
import com.im.common.service.TimFriendService;
import com.im.common.util.api.im.tencent.entity.result.TiBaseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 添加好友后的回掉处理
 */
@Slf4j
@Component
public class TiCbFriendAddAfterHandler implements TiCallbackHandler<TiCbFriendAddAfterRequest> {

    private TimFriendService timFriendService;
    private PortalUserCache portalUserCache;
    private PortalUserAppOperationLogService portalUserAppOperationLogService;

    @Autowired
    public void setPortalUserAppOperationLogService(PortalUserAppOperationLogService portalUserAppOperationLogService) {
        this.portalUserAppOperationLogService = portalUserAppOperationLogService;
    }

    @Autowired
    public void setTimFriendService(TimFriendService timFriendService) {
        this.timFriendService = timFriendService;
    }

    @Autowired
    public void setPortalUserCache(PortalUserCache portalUserCache) {
        this.portalUserCache = portalUserCache;
    }

    @Override
    public TiBaseResult handle(TiCbFriendAddAfterRequest request, Map<String, Object> context) {
        log.info("接收到一个添加好友之后的事件:[{}]", request.toString());
        List<TimFriend> list = new ArrayList<>();
        for (TiCbFriendAddAfterRequest.PairListDTO dto : request.getPairList()) {
            Long userId = portalUserCache.deepGetIdByUsernameFromLocal(dto.getFromAccount());
            Long initiatorUserId = portalUserCache.deepGetIdByUsernameFromLocal(dto.getInitiatorAccount());
            Long friendUserId = portalUserCache.deepGetIdByUsernameFromLocal(dto.getToAccount());
            TimFriend timFriend = new TimFriend(userId, friendUserId);
            timFriend.setInitiatorUserId(initiatorUserId);
            list.add(timFriend);
        }
        timFriendService.batchHandler(list);

        //记录日志
        portalUserAppOperationLogService.saveBatch(PortalUserAppOperationLogConvert.convert(request));
        return TiBaseResult.success();
    }
}
