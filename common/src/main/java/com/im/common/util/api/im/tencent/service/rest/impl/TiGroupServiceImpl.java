package com.im.common.util.api.im.tencent.service.rest.impl;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.map.MapUtil;
import com.im.common.param.TimGroupCustomMessageSendParam;
import com.im.common.response.RestResponse;
import com.im.common.util.api.im.tencent.entity.param.group.*;
import com.im.common.util.api.im.tencent.entity.param.message.TiModifyGroupMessageParam;
import com.im.common.util.api.im.tencent.entity.result.TiBaseResult;
import com.im.common.util.api.im.tencent.entity.result.group.*;
import com.im.common.util.api.im.tencent.entity.result.message.TiGroupMessageSendResult;
import com.im.common.util.api.im.tencent.request.RequestExecutor;
import com.im.common.util.api.im.tencent.request.api.GroupApiEnum;
import com.im.common.util.api.im.tencent.service.rest.TiGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class TiGroupServiceImpl implements TiGroupService {

    private RequestExecutor requestExecutor;

    @Autowired
    public void setRequestExecutor(RequestExecutor requestExecutor) {
        this.requestExecutor = requestExecutor;
    }

    @Override
    public RestResponse createGroup(TiGroupCreateParam param) {
        return requestExecutor.execute(GroupApiEnum.create_group.getUrl(), param);
    }

    @Override
    public RestResponse<TiBaseResult> updateGroup(TiGroupUpdateParam param) {
        return requestExecutor.execute(GroupApiEnum.modify_group_base_info.getUrl(), param, TiBaseResult.class);
    }

    @Override
    public RestResponse addMember(TiGroupAddMemberParam param) {
        return requestExecutor.execute(GroupApiEnum.add_group_member.getUrl(), param);
    }

    @Override
    public RestResponse deleteMember(TiGroupDeleteMemberParam param) {
        return requestExecutor.execute(GroupApiEnum.delete_group_member.getUrl(), param);
    }

    @Override
    public RestResponse<TiBaseResult> destroy(String groupId) {
        Map<String, String> param = MapUtil.of("GroupId", groupId);
        return requestExecutor.execute(GroupApiEnum.destroy_group.getUrl(), param, TiBaseResult.class);
    }

    @Override
    public RestResponse<TiGroupResult> getGroupInfo(TiGroupQueryParam groupQuery) {
        return requestExecutor.execute(GroupApiEnum.get_group_info.getUrl(), groupQuery, TiGroupResult.class);
    }

    @Override
    public RestResponse shutUp(TiGroupShutUpParam param) {
        return requestExecutor.execute(GroupApiEnum.forbid_send_msg.getUrl(), param);
    }

    @Override
    public RestResponse<TiGroupShutUpResult> getShutUpUni(String groupId) {
        Map<String, String> param = MapUtil.of("GroupId", groupId);
        return requestExecutor.execute(GroupApiEnum.get_group_shutted_uin.getUrl(), param, TiGroupShutUpResult.class);
    }

    @Override
    public RestResponse importGroup(TiGroupImportParam param) {
        return requestExecutor.execute(GroupApiEnum.import_group.getUrl(), param);
    }

    @Override
    public RestResponse<TiGroupMemberImportResult> importGroupMember(TiGroupMemberImportParam param) {
        return requestExecutor.execute(GroupApiEnum.import_group_member.getUrl(), param, TiGroupMemberImportResult.class);
    }

    @Override
    public RestResponse<TiGroupMessageImportResult> importGroupMessage(TiGroupMessageImportParam param) {
        return requestExecutor.execute(GroupApiEnum.import_group_msg.getUrl(), param, TiGroupMessageImportResult.class);
    }

    @Override
    public RestResponse sendGroupSystemNotification(TiGroupSystemNotificationParam param) {
        return requestExecutor.execute(GroupApiEnum.send_group_system_notification.getUrl(), param);
    }

    @Override
    public RestResponse deleteGroupMsgBySender(String groupId, String account) {
        Map<String, String> param = MapUtil.of("GroupId", groupId);
        param.put("Sender_Account", account);
        return requestExecutor.execute(GroupApiEnum.delete_group_msg_by_sender.getUrl(), param);
    }

    @Override
    public RestResponse<TiGroupIdResult> getAllGroupIds(TiGroupIdQueryParam query) {
        return requestExecutor.execute(GroupApiEnum.get_appid_group_list.getUrl(), query, TiGroupIdResult.class);
    }

    @Override
    public RestResponse<TiGroupMessageHistoryResult> getGroupMessageHistory(TiGroupMessageQueryParam param) {
        return requestExecutor.execute(GroupApiEnum.group_msg_get_simple.getUrl(), param, TiGroupMessageHistoryResult.class);
    }

    @Override
    public RestResponse<TiGroupMessageWithdrawResult> withdraw(TiGroupMessageWithdrawParam param) {
        return requestExecutor.execute(GroupApiEnum.group_msg_recall.getUrl(), param, TiGroupMessageWithdrawResult.class);
    }

    @Override
    public RestResponse<TiBaseResult> setMemberRole(TiGroupMemberSetRoleParam param) {
        return requestExecutor.execute(GroupApiEnum.modify_group_member_info.getUrl(), param, TiBaseResult.class);
    }

    @Override
    public RestResponse getMemberRole(String groupId, String account) {
        Map<String, Object> param = MapUtil.of("GroupId", groupId);
        param.put("User_Account", ListUtil.of(account));
        param.put("GroupId", groupId);
        RestResponse<TiGroupMemberRoleResult> restResponse = requestExecutor.execute(GroupApiEnum.get_role_in_group.getUrl(), param, TiGroupMemberRoleResult.class);
        if (restResponse.isOkRsp()) {
            return RestResponse.ok(restResponse.getData().getList().get(0).getRole());
        }
        return restResponse;
    }

    @Override
    public RestResponse<TiGroupMessageSendResult> sendCustomMessage(TimGroupCustomMessageSendParam param) {
        return requestExecutor.execute(GroupApiEnum.send_group_msg.getUrl(), param, TiGroupMessageSendResult.class);
    }

    @Override
    public RestResponse modifyGroupMsg(TiModifyGroupMessageParam param) {
        return requestExecutor.execute(GroupApiEnum.modify_group_msg.getUrl(), param, TiBaseResult.class);
    }
}
