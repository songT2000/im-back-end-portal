package com.im.common.util.api.im.tencent.request.api;

import lombok.Getter;

/**
 * 群组管理
 */
@Getter
public enum GroupApiEnum {

    get_appid_group_list("v4/group_open_http_svc/get_appid_group_list", "获取 App 中的所有群组ID"),
    create_group("v4/group_open_http_svc/create_group", "创建群组"),
    modify_group_base_info("v4/group_open_http_svc/modify_group_base_info", "修改群组信息"),
    add_group_member("v4/group_open_http_svc/add_group_member", "添加群成员"),
    delete_group_member("v4/group_open_http_svc/delete_group_member", "删除群成员"),
    destroy_group("v4/group_open_http_svc/destroy_group", "解散群组"),
    get_group_info("v4/group_open_http_svc/get_group_info", "获取群详细资料"),
    forbid_send_msg("v4/group_open_http_svc/forbid_send_msg", "批量禁言和取消禁言"),
    get_group_shutted_uin("v4/group_open_http_svc/get_group_shutted_uin", "获取被禁言群成员列表"),
    send_group_msg("v4/group_open_http_svc/send_group_msg", "在群组中发送普通消息"),
    send_group_system_notification("v4/group_open_http_svc/send_group_system_notification", "在群组中发送系统通知"),
    group_msg_recall("v4/group_open_http_svc/group_msg_recall", "撤回群消息"),
    import_group("v4/group_open_http_svc/import_group", "导入群基础资料"),
    import_group_msg("v4/group_open_http_svc/import_group_msg", "导入群消息"),
    import_group_member("v4/group_open_http_svc/import_group_member", "导入群成员"),
    delete_group_msg_by_sender("v4/group_open_http_svc/delete_group_msg_by_sender", "撤回指定用户发送的消息"),
    group_msg_get_simple("v4/group_open_http_svc/group_msg_get_simple", "拉取群历史消息"),
    modify_group_member_info("v4/group_open_http_svc/modify_group_member_info", "修改群成员资料"),
    get_role_in_group("v4/group_open_http_svc/get_role_in_group", "查询用户在群组中的身份"),
    modify_group_msg("v4/openim/modify_group_msg", "修改群聊历史消息"),
    ;

    /**
     * 接口地址
     */
    private String url;
    /**
     * 备注
     */
    private String remark;

    GroupApiEnum(String url, String remark) {
        this.url = url;
        this.remark = remark;
    }
}
