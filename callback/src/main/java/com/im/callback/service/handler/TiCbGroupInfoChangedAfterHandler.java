package com.im.callback.service.handler;

import com.im.callback.entity.TiCbGroupInfoChangedAfterRequest;
import com.im.callback.service.TiCallbackHandler;
import com.im.common.entity.tim.TimGroup;
import com.im.common.service.TimGroupService;
import com.im.common.util.api.im.tencent.entity.result.TiBaseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.Map;

/**
 *  群组资料修改之后回调处理
 */
@Slf4j
@Component
public class TiCbGroupInfoChangedAfterHandler implements TiCallbackHandler<TiCbGroupInfoChangedAfterRequest> {

    private TimGroupService timGroupService;

    @Autowired
    public void setTimGroupService(TimGroupService timGroupService) {
        this.timGroupService = timGroupService;
    }

    @Override
    public TiBaseResult handle(TiCbGroupInfoChangedAfterRequest request, Map<String, Object> context) {
        log.info("接收到一个更新群资料后的事件:[{}]", request.toString());

        TimGroup timGroup = timGroupService.getByGroupId(request.getGroupId());
        if(timGroup!=null){
            timGroup.setNotification(request.getNotification());
            timGroup.setIntroduction(request.getIntroduction());
            timGroup.setGroupName(request.getName());
            timGroup.setFaceUrl(request.getFaceUrl());
            timGroupService.updateById(timGroup);
        }

        return TiBaseResult.success();
    }
}
