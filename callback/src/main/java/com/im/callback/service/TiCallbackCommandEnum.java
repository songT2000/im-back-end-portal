package com.im.callback.service;

import com.im.callback.entity.*;
import lombok.Getter;

/**
 * 回掉命令枚举
 */
@Getter
public enum TiCallbackCommandEnum {
    state_change("State.StateChange","状态变更回调", TiCbStateChangeRequest.class),
    after_friend_add("Sns.CallbackFriendAdd","添加好友之后回调", TiCbFriendAddAfterRequest.class),
    before_friend_add("Sns.CallbackPrevFriendAdd","添加好友之前回调", TiCbFriendAddBeforeRequest.class),
    before_friend_response("Sns.CallbackPrevFriendResponse","添加好友回应之前回调", TiCbFriendBeforeResponseRequest.class),
    after_friend_delete("Sns.CallbackFriendDelete","删除好友之后回调", TiCbPairListRequest.class),
    black_list_add("Sns.CallbackBlackListAdd","添加黑名单之后回调", TiCbPairListRequest.class),
    black_list_delete("Sns.CallbackBlackListDelete","删除黑名单之后回调", TiCbPairListRequest.class),
    before_send_msg("C2C.CallbackBeforeSendMsg","发单聊消息之前回调", TiCbMessageSendRequest.class),
    after_send_msg("C2C.CallbackAfterSendMsg","发单聊消息之后回调", TiCbMessageSendRequest.class),
    after_withdraw_msg("C2C.CallbackAfterMsgWithDraw","单聊消息撤回后回调", TiCbMessageWithdrawRequest.class),
    after_create_group("Group.CallbackAfterCreateGroup","创建群组之后回调", TiCbGroupCreateRequest.class),
    after_member_join("Group.CallbackAfterNewMemberJoin","新成员入群之后回调", TiCbMemberJoinRequest.class),
    after_member_exit("Group.CallbackAfterMemberExit","群成员离开之后回调", TiCbMemberExitRequest.class),
    before_group_send_msg("Group.CallbackBeforeSendMsg","群内发言之前回调", TiCbGroupMessageSendRequest.class),
    after_group_send_msg("Group.CallbackAfterSendMsg","群内发言之后回调", TiCbGroupMessageSendRequest.class),
    after_group_destroyed("Group.CallbackAfterGroupDestroyed","群组解散之后回调", TiCbGroupDestroyedRequest.class),
    after_group_info_changed("Group.CallbackAfterGroupInfoChanged","群组资料修改之后回调", TiCbGroupInfoChangedAfterRequest.class),
    after_group_withdraw_msg("Group.CallbackAfterRecallMsg","群聊消息撤回之后回调", TiCbGroupMessageWithdrawRequest.class),
    ;

    TiCallbackCommandEnum(String command, String note, Class<? extends TiCallbackRequest> request) {
        this.command = command;
        this.note = note;
        this.request = request;
    }

    private String command;
    private String note;
    private Class<? extends TiCallbackRequest> request;

    public static TiCallbackCommandEnum getByCommand(String command){
        TiCallbackCommandEnum commandEnum = null;
        for (TiCallbackCommandEnum anEnum : TiCallbackCommandEnum.values()) {
            if(anEnum.getCommand().equals(command)){
                commandEnum = anEnum;
                break;
            }
        }
        return commandEnum;
    }

}
