package com.im.common.util.api.im.tencent.entity.result.group;

import com.alibaba.fastjson.annotation.JSONField;
import com.google.gson.annotations.SerializedName;
import com.im.common.entity.enums.GroupApplyJoinOptionEnum;
import com.im.common.entity.enums.GroupTypeEnum;
import com.im.common.entity.enums.OnOrOffEnum;
import com.im.common.util.api.im.tencent.entity.result.TiBaseResult;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 群组结构
 */
@Data
@NoArgsConstructor
public class TiGroup extends TiBaseResult {

    /**
     * 群组ID
     */
    @JSONField(name = "GroupId")
    private String groupId;
    /**
     * 群主的 UserId
     */
    @JSONField(name = "Owner_Account")
    private String ownerAccount;
    /**
     * 群组类型,默认是public
     */
    @JSONField(name = "Type")
    private GroupTypeEnum type;
    /**
     * 群名称
     */
    @JSONField(name = "Name")
    private String name;
    /**
     * 群简介
     */
    @JSONField(name = "Introduction")
    private String introduction;
    /**
     * 群公告
     */
    @JSONField(name = "Notification")
    private String notification;
    /**
     * 群头像 URL
     */
    @JSONField(name = "FaceUrl")
    private String faceUrl;
    /**
     * 申请加群处理方式默认是需要审核
     */
    @JSONField(name = "ApplyJoinOption")
    private GroupApplyJoinOptionEnum applyJoinOption;

    /**
     * 最大群成员数量
     */
    @JSONField(name = "MaxMemberNum")
    private Integer maxMemberNum;

    /**
     * 群组创建时间
     */
    @JSONField(name = "CreateTime")
    private Long createTime;

    /**
     * 全员禁言状态,on or off
     */
    @JSONField(name = "ShutUpAllMember")
    private OnOrOffEnum shutUpAllMember;

    /**
     * 群成员数组
     */
    @JSONField(name = "MemberList")
    private List<TiGroupMember> members;
}
