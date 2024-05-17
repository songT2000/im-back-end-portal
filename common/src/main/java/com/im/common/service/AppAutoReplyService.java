package com.im.common.service;

/**
 * 自动回复消息 服务类
 */
public interface AppAutoReplyService {

    /**
     * 自动回复
     *
     * @param fromAccount 自动回复的发送人
     * @param toAccount   自动回复的接收人
     */
    void autoReply(String fromAccount, String toAccount);
}
