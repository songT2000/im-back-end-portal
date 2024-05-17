package com.im.common.util.api.im.tencent.service.rest;

import cn.hutool.core.collection.ListUtil;
import com.im.common.param.TimGroupCustomMessageSendParam;
import com.im.common.response.RestResponse;
import com.im.common.util.api.im.tencent.entity.param.group.*;
import com.im.common.util.api.im.tencent.entity.param.message.TiModifyGroupMessageParam;
import com.im.common.util.api.im.tencent.entity.result.TiBaseResult;
import com.im.common.util.api.im.tencent.entity.result.group.*;
import com.im.common.util.api.im.tencent.entity.result.message.TiGroupMessageSendResult;

import java.util.List;

/**
 * 群组管理
 */
public interface TiGroupService {
    /**
     * 创建群组
     */
    RestResponse createGroup(TiGroupCreateParam param);

    /**
     * 修改群组信息
     */
    RestResponse<TiBaseResult> updateGroup(TiGroupUpdateParam param);

    /**
     * 添加群成员
     *
     * @param param 请求内容
     */
    RestResponse addMember(TiGroupAddMemberParam param);

    /**
     * 删除群成员
     *
     * @param param 请求内容
     */
    RestResponse deleteMember(TiGroupDeleteMemberParam param);

    /**
     * 解散群组
     *
     * @param groupId 群组ID
     */
    RestResponse<TiBaseResult> destroy(String groupId);

    /**
     * 查询群组信息
     *
     * @param groupId 群组ID
     */
    default RestResponse<TiGroupResult> getGroupInfo(String groupId) {
        return getGroupInfo(ListUtil.of(groupId));
    }

    /**
     * 查询群组信息
     *
     * @param groupIds 群组ID集合
     */
    default RestResponse<TiGroupResult> getGroupInfo(List<String> groupIds) {
        TiGroupQueryParam query = new TiGroupQueryParam();
        query.setGroupIdList(groupIds);
        return getGroupInfo(query);
    }

    /**
     * 查询群组信息
     *
     * @param groupQuery 详细查询参数
     */
    RestResponse<TiGroupResult> getGroupInfo(TiGroupQueryParam groupQuery);


    /**
     * 群组禁言
     * <br>禁止指定群组中某些用户在一段时间内发言。
     * <br>取消对某些用户的禁言。
     * <br>被禁言用户退出群组之后再进入同一群组，禁言仍然有效。
     */
    RestResponse shutUp(TiGroupShutUpParam param);

    /**
     * 获取被禁言群成员列表
     *
     * @param groupId 群组ID
     */
    RestResponse<TiGroupShutUpResult> getShutUpUni(String groupId);

    /**
     * 导入群组
     */
    RestResponse importGroup(TiGroupImportParam param);

    /**
     * 导入群成员
     * <br>该 API 接口的作用是导入群组成员，不会触发回调、不会下发通知。
     * <br>当 App 需要从其他即时通信系统迁移到即时通信 IM 时，使用该协议导入存量群成员数据。
     */
    RestResponse<TiGroupMemberImportResult> importGroupMember(TiGroupMemberImportParam param);

    /**
     * 导入群组消息
     * <br>批量导入群消息，一次最多导入7条。
     */
    RestResponse<TiGroupMessageImportResult> importGroupMessage(TiGroupMessageImportParam param);

    /**
     * 发送群聊系统通知
     */
    RestResponse sendGroupSystemNotification(TiGroupSystemNotificationParam param);

    /**
     * 撤回指定用户发送的消息
     * <br>该 API 接口的作用是撤回最近1000条消息中指定用户发送的消息。
     *
     * @param groupId 群组ID
     * @param account 指定用户的ID
     */
    RestResponse deleteGroupMsgBySender(String groupId, String account);

    /**
     * 获取 App 中的所有群组
     *
     * @param query 查询参数
     */
    RestResponse<TiGroupIdResult> getAllGroupIds(TiGroupIdQueryParam query);

    /**
     * 拉取群历史消息
     *
     * @param param 查询参数
     * @return 历史消息
     */
    RestResponse<TiGroupMessageHistoryResult> getGroupMessageHistory(TiGroupMessageQueryParam param);

    /**
     * 撤回群组消息
     */
    RestResponse<TiGroupMessageWithdrawResult> withdraw(TiGroupMessageWithdrawParam param);

    /**
     * 设置群成员角色
     */
    RestResponse<TiBaseResult> setMemberRole(TiGroupMemberSetRoleParam param);

    /**
     * 获取成员在群内的身份
     *
     * @param groupId 群组ID
     * @param account 账号
     */
    RestResponse getMemberRole(String groupId, String account);

    /**
     * 发送群聊自定义消息
     *
     * @param param 自定义消息的内容
     */
    RestResponse<TiGroupMessageSendResult> sendCustomMessage(TimGroupCustomMessageSendParam param);

    /**
     * 修改群聊历史消息
     *
     * @param param 修改群聊消息参数
     */
    RestResponse modifyGroupMsg(TiModifyGroupMessageParam param);
}
