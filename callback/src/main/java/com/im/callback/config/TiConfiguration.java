package com.im.callback.config;

import com.im.callback.service.TiCallbackCommandEnum;
import com.im.callback.service.TiCallbackRouter;
import com.im.callback.service.check.MessageC2cDuplicateChecker;
import com.im.callback.service.check.MessageGroupDuplicateChecker;
import com.im.callback.service.handler.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TiConfiguration {

    private TiCbStateChangeHandler tiCbStateChangeHandler;
    private TiCbFriendAddAfterHandler tiCbFriendAddAfterHandler;
    private TiCbFriendAddBeforeHandler tiCbFriendAddBeforeHandler;
    private TiCbFriendResponseBeforeHandler tiCbFriendResponseBeforeHandler;
    private TiCbFriendDeleteAfterHandler tiCbFriendDeleteAfterHandler;
    private TiCbBlackListAddHandler tiCbBlackListAddHandler;
    private TiCbBlackListDeleteHandler tiCbBlackListDeleteHandler;
    private TiCbGroupCreateHandler tiCbGroupCreateHandler;
    private TiCbGroupDestroyedHandler tiCbGroupDestroyedHandler;
    private TiCbGroupMemberJoinHandler tiCbGroupMemberJoinHandler;
    private TiCbGroupMemberExitHandler tiCbGroupMemberExitHandler;
    private TiCbGroupSendMessageAfterHandler tiCbGroupSendMessageAfterHandler;
    private TiCbGroupSendMessageBeforeHandler tiCbGroupSendMessageBeforeHandler;
    private TiCbC2cSendMessageBeforeHandler tiCbC2cSendMessageBeforeHandler;
    private TiCbC2cSendMessageAfterHandler tiCbC2CSendMessageAfterHandler;
    private TiCbC2cMessageWithdrawHandler tiCbC2cMessageWithdrawHandler;
    private TiCbGroupMessageWithdrawHandler tiCbGroupMessageWithdrawHandler;
    private TiCbGroupInfoChangedAfterHandler tiCbGroupInfoChangedAfterHandler;

    @Autowired
    public void setTiCbStateChangeHandler(TiCbStateChangeHandler tiCbStateChangeHandler) {
        this.tiCbStateChangeHandler = tiCbStateChangeHandler;
    }

    @Autowired
    public void setTiCbFriendAddAfterHandler(TiCbFriendAddAfterHandler tiCbFriendAddAfterHandler) {
        this.tiCbFriendAddAfterHandler = tiCbFriendAddAfterHandler;
    }

    @Autowired
    public void setTiCbFriendAddBeforeHandler(TiCbFriendAddBeforeHandler tiCbFriendAddBeforeHandler) {
        this.tiCbFriendAddBeforeHandler = tiCbFriendAddBeforeHandler;
    }

    @Autowired
    public void setTiCbFriendResponseBeforeHandler(TiCbFriendResponseBeforeHandler tiCbFriendResponseBeforeHandler) {
        this.tiCbFriendResponseBeforeHandler = tiCbFriendResponseBeforeHandler;
    }

    @Autowired
    public void setTiCbFriendDeleteAfterHandler(TiCbFriendDeleteAfterHandler tiCbFriendDeleteAfterHandler) {
        this.tiCbFriendDeleteAfterHandler = tiCbFriendDeleteAfterHandler;
    }

    @Autowired
    public void setTiCbBlackListAddHandler(TiCbBlackListAddHandler tiCbBlackListAddHandler) {
        this.tiCbBlackListAddHandler = tiCbBlackListAddHandler;
    }

    @Autowired
    public void setTiCbBlackListDeleteHandler(TiCbBlackListDeleteHandler tiCbBlackListDeleteHandler) {
        this.tiCbBlackListDeleteHandler = tiCbBlackListDeleteHandler;
    }

    @Autowired
    public void setTiCbGroupCreateHandler(TiCbGroupCreateHandler tiCbGroupCreateHandler) {
        this.tiCbGroupCreateHandler = tiCbGroupCreateHandler;
    }

    @Autowired
    public void setTiCbGroupDestroyedHandler(TiCbGroupDestroyedHandler tiCbGroupDestroyedHandler) {
        this.tiCbGroupDestroyedHandler = tiCbGroupDestroyedHandler;
    }

    @Autowired
    public void setTiCbGroupMemberJoinHandler(TiCbGroupMemberJoinHandler tiCbGroupMemberJoinHandler) {
        this.tiCbGroupMemberJoinHandler = tiCbGroupMemberJoinHandler;
    }

    @Autowired
    public void setTiCbGroupMemberExitHandler(TiCbGroupMemberExitHandler tiCbGroupMemberExitHandler) {
        this.tiCbGroupMemberExitHandler = tiCbGroupMemberExitHandler;
    }

    @Autowired
    public void setTiCbGroupSendMessageAfterHandler(TiCbGroupSendMessageAfterHandler tiCbGroupSendMessageAfterHandler) {
        this.tiCbGroupSendMessageAfterHandler = tiCbGroupSendMessageAfterHandler;
    }

    @Autowired
    public void setTiCbGroupSendMessageBeforeHandler(TiCbGroupSendMessageBeforeHandler tiCbGroupSendMessageBeforeHandler) {
        this.tiCbGroupSendMessageBeforeHandler = tiCbGroupSendMessageBeforeHandler;
    }

    @Autowired
    public void setTiCbMessageSendBeforeHandler(TiCbC2cSendMessageBeforeHandler tiCbC2cSendMessageBeforeHandler) {
        this.tiCbC2cSendMessageBeforeHandler = tiCbC2cSendMessageBeforeHandler;
    }

    @Autowired
    public void setTiCbMessageSendAfterHandler(TiCbC2cSendMessageAfterHandler tiCbC2CSendMessageAfterHandler) {
        this.tiCbC2CSendMessageAfterHandler = tiCbC2CSendMessageAfterHandler;
    }

    @Autowired
    public void setTiCbMessageWithdrawHandler(TiCbC2cMessageWithdrawHandler tiCbC2cMessageWithdrawHandler) {
        this.tiCbC2cMessageWithdrawHandler = tiCbC2cMessageWithdrawHandler;
    }

    @Autowired
    public void setTiCbGroupInfoChangedAfterHandler(TiCbGroupInfoChangedAfterHandler tiCbGroupInfoChangedAfterHandler) {
        this.tiCbGroupInfoChangedAfterHandler = tiCbGroupInfoChangedAfterHandler;
    }

    @Autowired
    public void setTiCbGroupMessageWithdrawHandler(TiCbGroupMessageWithdrawHandler tiCbGroupMessageWithdrawHandler) {
        this.tiCbGroupMessageWithdrawHandler = tiCbGroupMessageWithdrawHandler;
    }

    @Bean
    public TiCallbackRouter callbackRouter() {
        /**
         * 回掉函数处理路由，支持一个事件多个handler处理，支持异步或者同步处理，支持interceptor拦截器
         */
        TiCallbackRouter router = new TiCallbackRouter();
        // 用户状态变更事件
        router.rule().async(false).event(TiCallbackCommandEnum.state_change)
                .handler(this.tiCbStateChangeHandler)
                .end();
        // 用户添加好友之前事件
        router.rule().async(true).event(TiCallbackCommandEnum.before_friend_add)
                .handler(this.tiCbFriendAddBeforeHandler)
                .end();
        // 用户添加好友回应之前事件
        router.rule().async(true).event(TiCallbackCommandEnum.before_friend_response)
                .handler(this.tiCbFriendResponseBeforeHandler)
                .end();
        // 用户添加好友之后事件
        router.rule().async(false).event(TiCallbackCommandEnum.after_friend_add)
                .handler(this.tiCbFriendAddAfterHandler)
                .end();
        // 用户删除好友之后事件
        router.rule().async(false).event(TiCallbackCommandEnum.after_friend_delete)
                .handler(this.tiCbFriendDeleteAfterHandler)
                .end();

        // 添加黑名单事件
        router.rule().async(false).event(TiCallbackCommandEnum.black_list_add)
                .handler(this.tiCbBlackListAddHandler)
                .end();

        // 删除黑名单事件
        router.rule().async(false).event(TiCallbackCommandEnum.black_list_delete)
                .handler(this.tiCbBlackListDeleteHandler)
                .end();

        // 创建群组事件
        router.rule().async(false).event(TiCallbackCommandEnum.after_create_group)
                .handler(this.tiCbGroupCreateHandler)
                .end();

        // 解散群组事件
        router.rule().async(false).event(TiCallbackCommandEnum.after_group_destroyed)
                .handler(this.tiCbGroupDestroyedHandler)
                .end();

        // 群成员退出群事件
        router.rule().async(false).event(TiCallbackCommandEnum.after_member_exit)
                .handler(this.tiCbGroupMemberExitHandler)
                .end();

        // 添加群成员事件
        router.rule().async(false).event(TiCallbackCommandEnum.after_member_join)
                .handler(this.tiCbGroupMemberJoinHandler)
                .end();

        // 群组发消息之后事件
        router.rule().async(false).event(TiCallbackCommandEnum.after_group_send_msg)
                .handler(this.tiCbGroupSendMessageAfterHandler)
                .duplicateChecker(new MessageGroupDuplicateChecker())
                .end();
        // 群组发消息之前事件
        router.rule().async(false).event(TiCallbackCommandEnum.before_group_send_msg)
                .handler(this.tiCbGroupSendMessageBeforeHandler)
                .end();

        // 发送单聊消息之前事件
        router.rule().async(true).event(TiCallbackCommandEnum.before_send_msg)
                .handler(this.tiCbC2cSendMessageBeforeHandler)
                .end();

        // 发送单聊之后消息事件
        router.rule().async(false).event(TiCallbackCommandEnum.after_send_msg)
                .handler(this.tiCbC2CSendMessageAfterHandler)
                .duplicateChecker(new MessageC2cDuplicateChecker())
                .end();

        // 撤回消息事件
        router.rule().async(false).event(TiCallbackCommandEnum.after_withdraw_msg)
                .handler(this.tiCbC2cMessageWithdrawHandler)
                .end();

        // 群组资料修改之后事件
        router.rule().async(false).event(TiCallbackCommandEnum.after_group_info_changed)
                .handler(this.tiCbGroupInfoChangedAfterHandler)
                .end();

        // 群组消息撤回之后事件
        router.rule().async(false).event(TiCallbackCommandEnum.after_group_withdraw_msg)
                .handler(this.tiCbGroupMessageWithdrawHandler)
                .end();
        return router;
    }

}
