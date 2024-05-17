package com.im.callback.service.handler;

import com.im.callback.entity.TiCbMessageSendRequest;
import com.im.callback.service.TiCallbackHandler;
import com.im.common.util.api.im.tencent.entity.result.TiBaseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 发送单聊消息前的回掉处理
 */
@Slf4j
@Component
public class TiCbC2cSendMessageBeforeHandler implements TiCallbackHandler<TiCbMessageSendRequest> {

    @Override
    public TiBaseResult handle(TiCbMessageSendRequest request, Map<String, Object> context) {
        log.info("接收到一个单聊发送消息前的事件:[{}]", request.toString());
        //用于敏感词拦截等，参考：https://cloud.tencent.com/document/product/269/1632
        return TiBaseResult.success();
    }
}
