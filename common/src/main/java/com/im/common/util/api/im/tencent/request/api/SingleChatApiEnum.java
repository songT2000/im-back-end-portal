package com.im.common.util.api.im.tencent.request.api;

import lombok.Getter;

/**
 * 单聊消息接口
 */
@Getter
public enum SingleChatApiEnum {

    send_msg("v4/openim/sendmsg", "单发单聊消息"),
    batch_send_msg("v4/openim/batchsendmsg", "批量发单聊消息"),
    import_msg("v4/openim/importmsg", "导入单聊消息"),
    admin_get_roam_msg("v4/openim/admin_getroammsg", "查询单聊消息"),
    admin_msg_withdraw("v4/openim/admin_msgwithdraw", "撤回单聊消息"),
    admin_set_msg_read("v4/openim/admin_set_msg_read", "设置单聊消息已读"),
    modify_c2c_msg("v4/openim/modify_c2c_msg", "修改单聊历史消息"),
    get_c2c_unread_msg_num("v4/openim/get_c2c_unread_msg_num", "查询单聊未读消息计数");

    /**
     * 接口地址
     */
    private String url;
    /**
     * 备注
     */
    private String remark;

    SingleChatApiEnum(String url, String remark) {
        this.url = url;
        this.remark = remark;
    }
}
