package com.im.callback.service.check;

import com.im.callback.entity.TiCallbackRequest;
import com.im.callback.entity.TiCbGroupMessageSendRequest;
import com.im.callback.entity.TiCbMessageSendRequest;

public class MessageGroupDuplicateChecker extends MessageInMemoryDuplicateChecker{

    @Override
    public boolean isDuplicate(TiCallbackRequest request) {
        if (request == null) {
            return false;
        }
        if(request instanceof TiCbGroupMessageSendRequest){
            TiCbGroupMessageSendRequest messageSendRequest = (TiCbGroupMessageSendRequest) request;
            String messageKey = messageSendRequest.getGroupId() + "-" + messageSendRequest.getMsgSeq();
            checkBackgroundProcessStarted();
            Long timestamp = super.msgId2Timestamp.putIfAbsent(messageKey, System.currentTimeMillis());
            return timestamp != null;
        }
        return false;
    }

}
