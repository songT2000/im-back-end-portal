package com.im.common.util.api.im.tencent.entity.param.group;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 腾讯IM群聊系统通知
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TiGroupSystemNotificationParam implements Serializable {

    public TiGroupSystemNotificationParam(String groupId, String content) {
        this.groupId = groupId;
        this.content = content;
    }

    /**
     * 向哪个群组发送系统通知（必填）
     */
    @JSONField(name = "GroupId")
    private String groupId;
    /**
     * 系统通知的内容（必填）
     */
    @JSONField(name = "Content")
    private String content;

    /**
     * 接收者群成员列表，请填写接收者 UserID，不填或为空表示全员下发(选填)
     */
    @JSONField(name = "ToMembers_Account")
    private List<String> toMemberAccounts;


}
