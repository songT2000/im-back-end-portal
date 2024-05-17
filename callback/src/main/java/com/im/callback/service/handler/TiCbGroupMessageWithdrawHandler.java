package com.im.callback.service.handler;

import com.im.callback.entity.TiCbGroupMessageWithdrawRequest;
import com.im.callback.entity.TiCbMessageWithdrawRequest;
import com.im.callback.service.TiCallbackHandler;
import com.im.common.cache.impl.PortalUserCache;
import com.im.common.param.TimMessageGroupWithdrawParam;
import com.im.common.service.TimMessageC2cService;
import com.im.common.service.TimMessageGroupService;
import com.im.common.util.api.im.tencent.entity.result.TiBaseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.stream.Collectors;

/**
 *  群聊消息撤回之后的回掉处理
 */
@Slf4j
@Component
public class TiCbGroupMessageWithdrawHandler implements TiCallbackHandler<TiCbGroupMessageWithdrawRequest> {

    private TimMessageGroupService timMessageGroupService;

    @Autowired
    public void setTimMessageGroupService(TimMessageGroupService timMessageGroupService) {
        this.timMessageGroupService = timMessageGroupService;
    }

    @Override
    public TiBaseResult handle(TiCbGroupMessageWithdrawRequest request, Map<String, Object> context) {
        log.info("接收到一个群聊消息撤回之后的事件:[{}]", request.toString());

        TimMessageGroupWithdrawParam param = new TimMessageGroupWithdrawParam();
        param.setGroupId(request.getGroupId());
        param.setMsgSeqList(request.getMsgSeqList().stream().map(TiCbGroupMessageWithdrawRequest.MsgSeqListDTO::getMsgSeq).collect(Collectors.toList()));
        timMessageGroupService.deleteLocalMessage(param);

        return TiBaseResult.success();
    }
}
