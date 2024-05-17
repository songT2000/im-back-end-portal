package com.im.common.util.api.im.tencent.entity.result.group;

import com.alibaba.fastjson.annotation.JSONField;
import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 群成员结构
 */
@Data
@NoArgsConstructor
public class TiGroupMember implements Serializable {

    /**
     * 群成员 ID
     */
    @JSONField(name = "Member_Account")
    private String memberAccount;
    /**
     * 群内身份，包括 Owner 群主、Admin 群管理员以及 Member 群成员
     */
    @JSONField(name = "Role")
    private String role;
    /**
     * 入群时间
     */
    @JSONField(name = "JoinTime")
    private Long joinTime;
    /**
     * 消息接收选项，包括如下几种：
     * AcceptAndNotify 表示接收并提示
     * AcceptNotNotify 表示接收不提示（不会触发 APNs 远程推送）
     * Discard 表示屏蔽群消息（不会向客户端推送消息）
     */
    @JSONField(name = "MsgFlag")
    private String msgFlag;
    /**
     * 最后一次发消息的时间
     */
    @JSONField(name = "LastSendMsgTime")
    private Long lastSendMsgTime;
    /**
     *  0表示未被禁言，否则为禁言的截止时间
     */
    @JSONField(name = "ShutUpUntil")
    private Long shutUpUntil;
    /**
     * 群名片
     */
    @JSONField(name = "NameCard")
    private String nameCard;
}
