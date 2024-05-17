package com.im.common.util.api.im.tencent.service.rest;

import com.im.common.response.RestResponse;
import com.im.common.util.api.im.tencent.entity.param.message.*;
import com.im.common.util.api.im.tencent.entity.result.message.TiSingleChatUnreadMsgNumResult;
import com.im.common.util.api.im.tencent.entity.result.message.TiSingleMessageResult;
import com.im.common.util.api.im.tencent.entity.result.message.TiSingleMessageSendResult;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 单聊消息接口
 */
public interface TiSingleChatService {

    /**
     * 发送单聊消息
     *
     * @param tiMessage 单聊消息内容
     */
    RestResponse<TiSingleMessageSendResult> sendMessage(TiMessage tiMessage);

    /**
     * 批量发送单聊消息
     *
     * @param tiBatchMessageParam 单聊消息内容
     */
    RestResponse<TiSingleMessageSendResult> batchSendMessage(TiBatchMessageParam tiBatchMessageParam);

    /**
     * 导入历史消息
     *
     * @param tiMessage 消息内容
     */
    RestResponse importMessage(TiMessageImportParam tiMessage);

    /**
     * 查询单聊消息
     *
     * @param fromAccount 发消息的账号
     * @param toAccount   接收消息的账号
     * @param startTime   开始时间
     * @param endTime     结束时间
     * @return 该时段所有的消息
     */
    RestResponse<List<TiSingleMessageResult>> queryHistory(String fromAccount, String toAccount,
                                                           LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 撤回单聊消息
     *
     * @param tiWithdrawMessageParam 撤回参数
     *                               接口地址：https://cloud.tencent.com/document/product/269/38980
     */
    RestResponse withdraw(TiWithdrawMessageParam tiWithdrawMessageParam);

    /**
     * 设置消息已读
     *
     * @param tiSetMessageReadParam 请求内容
     */
    RestResponse setMessageRead(TiSetMessageReadParam tiSetMessageReadParam);

    /**
     * 查询单聊未读消息计数
     *
     * @param account 待查询的账号
     */
    RestResponse<TiSingleChatUnreadMsgNumResult> getUnreadMsgNum(String account);

    /**
     * 修改单聊消息
     *
     * @param param 修改消息的参数
     */
    RestResponse modifyC2cMsg(TiModifyC2cMessageParam param);
}
