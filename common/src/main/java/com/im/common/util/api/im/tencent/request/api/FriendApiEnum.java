package com.im.common.util.api.im.tencent.request.api;

import lombok.Getter;

/**
 * 好友关系链管理
 */
@Getter
public enum FriendApiEnum {

    friend_add("v4/sns/friend_add","添加好友"),
    friend_import("v4/sns/friend_import","导入好友"),
    friend_update("v4/sns/friend_update","更新好友"),
    friend_delete("v4/sns/friend_delete","删除好友"),
    friend_delete_all("v4/sns/friend_delete_all","删除所有好友"),
    friend_check("v4/sns/friend_check","校验好友"),
    friend_get("v4/sns/friend_get","拉取好友"),
    friend_get_list("v4/sns/friend_get_list","拉取指定好友"),
    black_list_add("v4/sns/black_list_add","添加黑名单"),
    black_list_delete("v4/sns/black_list_delete","删除黑名单"),
    black_list_get("v4/sns/black_list_get","拉取黑名单"),
    black_list_check("v4/sns/black_list_check","校验黑名单"),
    group_add("v4/sns/group_add","添加分组"),
    group_delete("v4/sns/group_delete","删除分组"),
    group_get("v4/sns/group_get","拉取分组"),
    ;
    /**
     * 接口地址
     */
    private String url;
    /**
     * 备注
     */
    private String remark;

    FriendApiEnum(String url, String remark) {
        this.url = url;
        this.remark = remark;
    }
}
