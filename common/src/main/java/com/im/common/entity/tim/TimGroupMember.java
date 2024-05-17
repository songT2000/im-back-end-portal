package com.im.common.entity.tim;

import cn.hutool.core.date.LocalDateTimeUtil;
import com.im.common.cache.impl.PortalUserCache;
import com.im.common.entity.base.BaseEntity;
import com.im.common.entity.enums.GroupMemberRoleEnum;
import com.im.common.util.api.im.tencent.entity.result.group.TiGroupMember;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TimGroupMember extends BaseEntity implements Serializable {
    private static final long serialVersionUID = -2806747754396375216L;


    /**
     * 群成员 ID
     */
    private Long userId;
    /**
     * 群组 ID
     */
    private String groupId;
    /**
     * 群内身份，包括 Owner 群主、Admin 群管理员以及 Member 群成员
     */
    private GroupMemberRoleEnum role;
    /**
     * 消息接收选项，包括如下几种：
     * AcceptAndNotify 表示接收并提示
     * AcceptNotNotify 表示接收不提示（不会触发 APNs 远程推送）
     * Discard 表示屏蔽群消息（不会向客户端推送消息）
     */
    private String msgFlag = "AcceptAndNotify";
    /**
     *  null表示未被禁言，否则为禁言的截止时间
     */
    private LocalDateTime shutUpEndTime;
    /**
     * 入群时间
     */
    private LocalDateTime joinTime;
    /**
     * 群名片(在群内的昵称)
     */
    private String nameCard;

    public TimGroupMember(Long userId, String groupId) {
        this.userId = userId;
        this.groupId = groupId;
        this.role = GroupMemberRoleEnum.Member;
        this.joinTime = LocalDateTime.now();
    }

    public TimGroupMember(TiGroupMember groupMember, String groupId, PortalUserCache portalUserCache) {
        this.setGroupId(groupId);
        this.setMsgFlag(groupMember.getMsgFlag());
        this.setNameCard(groupMember.getNameCard());
        this.setRole(GroupMemberRoleEnum.getByVal(groupMember.getRole()));
        if(groupMember.getShutUpUntil()!=null && groupMember.getShutUpUntil()>0){
            this.setShutUpEndTime(LocalDateTimeUtil.of(groupMember.getShutUpUntil()*1000));
        }
        this.setJoinTime(LocalDateTimeUtil.of(groupMember.getJoinTime()*1000));
        this.setUserId(portalUserCache.getIdByUsernameFromLocal(groupMember.getMemberAccount()));
    }
}
