package com.im.callback.service.handler;

import com.im.callback.entity.TiCbGroupCreateRequest;
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
 * 创建群组后的回掉处理
 */
@Slf4j
@Component
public class TiCbGroupCreateHandler implements TiCallbackHandler<TiCbGroupCreateRequest> {

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
    public TiBaseResult handle(TiCbGroupCreateRequest request, Map<String, Object> context) {
        log.info("接收到一个创建群组后的事件:[{}]", request.toString());
        timGroupService.sync(request.getGroupId());

        //记录日志
        portalUserAppOperationLogService.saveBatch(PortalUserAppOperationLogConvert.convert(request));
        return TiBaseResult.success();
    }
}
