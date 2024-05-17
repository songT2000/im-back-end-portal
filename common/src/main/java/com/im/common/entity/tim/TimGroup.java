package com.im.common.entity.tim;

import cn.hutool.core.date.LocalDateTimeUtil;
import com.im.common.cache.impl.PortalUserCache;
import com.im.common.constant.CommonConstant;
import com.im.common.entity.base.BaseEntity;
import com.im.common.entity.enums.GroupApplyJoinOptionEnum;
import com.im.common.entity.enums.GroupTypeEnum;
import com.im.common.entity.enums.OnOrOffEnum;
import com.im.common.util.api.im.tencent.entity.result.group.TiGroup;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 群组
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TimGroup extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 7652856150377722674L;

    /**
     * 群组的ID
     */
    private String groupId;
    /**
     * 群主的 UserId
     */
    private Long ownerUserId;
    /**
     * 群组类型,默认是Public
     */
    private GroupTypeEnum groupType = GroupTypeEnum.Public;
    /**
     * 群名称
     */
    private String groupName;
    /**
     * 群简介
     */
    private String introduction;
    /**
     * 群公告
     */
    private String notification;
    /**
     * 群头像 URL
     */
    private String faceUrl;
    /**
     * 申请加群处理方式默认是需要审核
     */
    private GroupApplyJoinOptionEnum applyJoinOption = GroupApplyJoinOptionEnum.NeedPermission;

    /**
     * 最大群成员数量
     * <pre>
     *     创建时不用填写，使用系统默认值
     * </pre>
     */
    private Integer maxMemberCount;

    /**
     * 全员禁言状态,on or off
     */
    private OnOrOffEnum shutUpState = OnOrOffEnum.OFF;
    /**
     * 备注信息，不予公开
     */
    private String remark;

    /**
     * 显示群内成员权限，默认显示
     **/
    private Boolean showMemberEnabled = true;

    /**
     * 上传权限，默认打开
     **/
    private Boolean uploadEnabled = true;

    /**
     * 是否允许普通成员拉人进群，默认关闭
     **/
    private Boolean addMemberEnabled = false;

    /**
     * 群内成员私聊权限，默认关闭
     **/
    private Boolean anonymousChatEnabled = false;

    /**
     * 群内私加好友权限，默认关闭
     **/
    private Boolean addFriendEnabled = false;

    /**
     * 退出群组权限，默认打开
     **/
    private Boolean exitEnabled = true;

    /**
     * 普通成员发消息间隔时间，单位秒,0代表不限制
     */
    private Integer msgIntervalTime = CommonConstant.INT_0;

    public TimGroup(TiGroup group, PortalUserCache portalUserCache) {
        this.setGroupName(group.getName());
        this.setOwnerUserId(portalUserCache.getIdByUsernameFromLocal(group.getOwnerAccount()));
        this.setGroupId(group.getGroupId());
        this.setFaceUrl(group.getFaceUrl());
        this.setApplyJoinOption(group.getApplyJoinOption());
        this.setMaxMemberCount(group.getMaxMemberNum());
        this.setShutUpState(group.getShutUpAllMember());
        this.setCreateTime(LocalDateTimeUtil.of(group.getCreateTime() * 1000));
    }
}
