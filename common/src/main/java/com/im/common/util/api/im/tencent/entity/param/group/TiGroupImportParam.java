package com.im.common.util.api.im.tencent.entity.param.group;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.LongCodec;
import com.im.common.cache.impl.PortalUserCache;
import com.im.common.entity.enums.GroupApplyJoinOptionEnum;
import com.im.common.entity.enums.GroupTypeEnum;
import com.im.common.entity.tim.TimGroup;
import com.im.common.util.LocalDateTimeUtil;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 导入群组参数
 */
@Data
@NoArgsConstructor
public class TiGroupImportParam implements Serializable {

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
     * 群组类型
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
     * 群头像 URL
     */
    @JSONField(name = "ApplyJoinOption")
    private GroupApplyJoinOptionEnum applyJoinOption;
    /**
     * 创建时间
     */
    @JSONField(name = "CreateTime",serializeUsing = LongCodec.class)
    private Long createTime;

    public TiGroupImportParam(TimGroup group, PortalUserCache portalUserCache) {
        this.groupId = group.getGroupId();
        this.name = group.getGroupName();
        this.ownerAccount = portalUserCache.getUsernameByIdFromLocal(group.getOwnerUserId());
        this.faceUrl = group.getFaceUrl();
        this.applyJoinOption = group.getApplyJoinOption();
        this.type = group.getGroupType();
        this.createTime = LocalDateTimeUtil.getTimestampOfDateTime(group.getCreateTime())/1000L;
    }
}
