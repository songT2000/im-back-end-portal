package com.im.callback.service.check;

import com.im.callback.entity.TiCallbackRequest;
import com.im.callback.entity.TiCbMessageSendRequest;

public class MessageC2cDuplicateChecker extends MessageInMemoryDuplicateChecker{

    @Override
    public boolean isDuplicate(TiCallbackRequest request) {
        if (request == null) {
            return false;
        }
        if (request instanceof TiCbMessageSendRequest) {
            TiCbMessageSendRequest messageSendRequest = (TiCbMessageSendRequest) request;
            checkBackgroundProcessStarted();
            Long timestamp = super.msgId2Timestamp.putIfAbsent(messageSendRequest.getMsgKey(), System.currentTimeMillis());
            return timestamp != null;
        }
        return false;
    }

}
