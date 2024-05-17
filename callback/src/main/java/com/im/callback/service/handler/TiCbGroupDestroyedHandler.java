package com.im.callback.service.handler;

import com.im.callback.entity.TiCbGroupDestroyedRequest;
import com.im.callback.service.TiCallbackHandler;
import com.im.callback.util.PortalUserAppOperationLogConvert;
import com.im.common.service.PortalUserAppOperationLogService;
import com.im.common.service.TimGroupService;
import com.im.common.util.api.im.tencent.entity.result.TiBaseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 群组解散之后回调处理
 */
@Slf4j
@Component
public class TiCbGroupDestroyedHandler implements TiCallbackHandler<TiCbGroupDestroyedRequest> {

    private TimGroupService timGroupService;
    private PortalUserAppOperationLogService portalUserAppOperationLogService;

    @Autowired
    public void setPortalUserAppOperationLogService(PortalUserAppOperationLogService portalUserAppOperationLogService) {
        this.portalUserAppOperationLogService = portalUserAppOperationLogService;
    }

    @Autowired
    public void setTimGroupService(TimGroupService timGroupService) {
        this.timGroupService = timGroupService;
    }

    @Override
    public TiBaseResult handle(TiCbGroupDestroyedRequest request, Map<String, Object> context) {
        log.info("接收到一个群组解散之后的事件:[{}]", request.toString());
        timGroupService.destroyLocal(request.getGroupId());

        //记录日志
        portalUserAppOperationLogService.saveBatch(PortalUserAppOperationLogConvert.convert(request));
        return TiBaseResult.success();
    }
}
