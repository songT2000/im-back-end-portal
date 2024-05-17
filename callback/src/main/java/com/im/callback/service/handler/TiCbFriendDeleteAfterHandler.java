package com.im.callback.service.handler;

import com.im.callback.entity.TiCbPairListRequest;
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
 * 删除好友后的回掉处理
 */
@Slf4j
@Component
public class TiCbFriendDeleteAfterHandler implements TiCallbackHandler<TiCbPairListRequest> {

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
    public TiBaseResult handle(TiCbPairListRequest request, Map<String, Object> context) {
        log.info("接收到一个删除好友事件:[{}]", request.toString());

        List<TimFriend> list = new ArrayList<>();
        for (TiCbPairListRequest.PairListDTO dto : request.getPairList()) {
            Long userId = portalUserCache.getIdByUsernameFromLocal(dto.getFromAccount());
            Long friendUserId = portalUserCache.getIdByUsernameFromLocal(dto.getToAccount());

            list.add(new TimFriend(userId, friendUserId));
        }
        timFriendService.batchDelete(list);

        //记录日志
        portalUserAppOperationLogService.saveBatch(PortalUserAppOperationLogConvert.convert(request));
        return TiBaseResult.success();
    }
}
