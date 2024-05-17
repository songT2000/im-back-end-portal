package com.im.callback.service.handler;

import com.im.callback.entity.TiCbPairListRequest;
import com.im.callback.service.TiCallbackHandler;
import com.im.callback.util.PortalUserAppOperationLogConvert;
import com.im.common.cache.impl.PortalUserCache;
import com.im.common.entity.tim.TimBlacklist;
import com.im.common.service.PortalUserAppOperationLogService;
import com.im.common.service.TimBlacklistService;
import com.im.common.util.api.im.tencent.entity.result.TiBaseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 删除黑名单后的回掉处理
 */
@Slf4j
@Component
public class TiCbBlackListDeleteHandler implements TiCallbackHandler<TiCbPairListRequest> {

    private PortalUserCache portalUserCache;
    private TimBlacklistService timBlacklistService;
    private PortalUserAppOperationLogService portalUserAppOperationLogService;

    @Autowired
    public void setPortalUserAppOperationLogService(PortalUserAppOperationLogService portalUserAppOperationLogService) {
        this.portalUserAppOperationLogService = portalUserAppOperationLogService;
    }

    @Autowired
    public void setPortalUserCache(PortalUserCache portalUserCache) {
        this.portalUserCache = portalUserCache;
    }

    @Autowired
    public void setTimBlacklistService(TimBlacklistService timBlacklistService) {
        this.timBlacklistService = timBlacklistService;
    }

    @Override
    public TiBaseResult handle(TiCbPairListRequest request, Map<String, Object> context) {
        log.info("接收到一个删除黑名单事件:[{}]", request.toString());

        List<TimBlacklist> list = new ArrayList<>();
        for (TiCbPairListRequest.PairListDTO dto : request.getPairList()) {
            Long userId = portalUserCache.getIdByUsernameFromLocal(dto.getFromAccount());
            Long blacklistUserId = portalUserCache.getIdByUsernameFromLocal(dto.getToAccount());
            list.add(new TimBlacklist(userId, blacklistUserId));
        }
        timBlacklistService.dismiss(list);

        //记录日志
        portalUserAppOperationLogService.saveBatch(PortalUserAppOperationLogConvert.convert(request));
        return TiBaseResult.success();
    }
}
