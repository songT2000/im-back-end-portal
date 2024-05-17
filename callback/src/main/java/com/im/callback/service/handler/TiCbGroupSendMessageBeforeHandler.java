package com.im.callback.service.handler;

import com.im.callback.entity.TiCbGroupMessageSendRequest;
import com.im.callback.service.TiCallbackHandler;
import com.im.common.util.api.im.tencent.entity.result.TiBaseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 *  群组消息发送前回调处理
 */
@Slf4j
@Component
public class TiCbGroupSendMessageBeforeHandler implements TiCallbackHandler<TiCbGroupMessageSendRequest> {

    @Override
    public TiBaseResult handle(TiCbGroupMessageSendRequest request, Map<String, Object> context) {
        log.info("接收到一个群组消息发送前的事件:[{}]", request.toString());
        //如果用于敏感词拦截，请将方法改成同步执行，参考：https://cloud.tencent.com/document/product/269/1619
        return TiBaseResult.success();
    }
}
