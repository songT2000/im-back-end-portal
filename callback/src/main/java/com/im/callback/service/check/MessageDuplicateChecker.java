package com.im.callback.service.check;

import com.im.callback.entity.TiCallbackRequest;

/**
 * 消息重复检查器
 * <br>实测发现回掉事件会有重复调用的现象
 */
public interface MessageDuplicateChecker {

    /**
     * 判断消息是否重复
     * @param request       单聊消息根据msgKey来判断，群聊消息，根据群组ID和msgSeq来判断
     * @return              是否重复
     */
    boolean isDuplicate(TiCallbackRequest request);

}
