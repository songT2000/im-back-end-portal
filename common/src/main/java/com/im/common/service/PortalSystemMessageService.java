package com.im.common.service;

/**
 * 前台系统消息服务
 */
public interface PortalSystemMessageService {

    /**
     * 发送提现申请系统消息
     */
    void sendWithdrawMessage();

    /**
     * 发送充值申请系统消息
     */
    void sendRechargeMessage();

}
