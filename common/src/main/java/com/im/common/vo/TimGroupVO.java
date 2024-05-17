package com.im.common.vo;

import com.im.common.constant.CommonConstant;
import com.im.common.entity.enums.OnOrOffEnum;
import com.im.common.entity.enums.PortalTypeEnum;
import com.im.common.entity.tim.TimGroup;
import com.im.common.util.user.UserUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.beans.BeanCopier;

import java.time.LocalDateTime;

/**
 * 群组信息
 */
@Data
@NoArgsConstructor
@ApiModel
public class TimGroupVO {
    public static final BeanCopier BEAN_COPIER = BeanCopier.create(TimGroup.class, TimGroupVO.class, false);
    @ApiModelProperty(value = "群组的ID", position = 1)
    private String groupId;
    @ApiModelProperty(value = "群名称", position = 2)
    private String groupName;
    @ApiModelProperty(value = "群主Id", position = 3)
    private Long ownerUserId;
    @ApiModelProperty(value = "群主账号", position = 4)
    private String ownerUsername;
    @ApiModelProperty(value = "群主昵称", position = 5)
    private String ownerUserNickname;
    @ApiModelProperty(value = "群简介", position = 6)
    private String introduction;
    @ApiModelProperty(value = "群公告", position = 7)
    private String notification;
    @ApiModelProperty(value = "群头像URL", position = 8)
    private String faceUrl;
    @ApiModelProperty(value = "全员禁言状态", position = 9)
    private OnOrOffEnum shutUpState = OnOrOffEnum.OFF;
    @ApiModelProperty(value = "显示群内成员权限，默认显示", position = 10)
    private Boolean showMemberEnabled = true;
    @ApiModelProperty(value = "上传权限，默认打开", position = 11)
    private Boolean uploadEnabled = true;
    @ApiModelProperty(value = "是否允许普通成员拉人进群，默认关闭", position = 12)
    private Boolean addMemberEnabled = false;
    @ApiModelProperty(value = "群内成员私聊权限，默认关闭", position = 13)
    private Boolean anonymousChatEnabled = false;
    @ApiModelProperty(value = "群内私加好友权限，默认关闭", position = 14)
    private Boolean addFriendEnabled = false;
    @ApiModelProperty(value = "退出群组权限，默认打开", position = 15)
    private Boolean exitEnabled = true;
    @ApiModelProperty(value = "普通成员发消息间隔时间，单位秒,0代表不限制", position = 16)
    private Integer msgIntervalTime = CommonConstant.INT_0;
    @ApiModelProperty(value = "创建时间", position = 17)
    private LocalDateTime createTime;

    public TimGroupVO(TimGroup e) {
        BEAN_COPIER.copy(e, this, null);
        this.ownerUsername = UserUtil.getUsernameByIdFromLocal(e.getOwnerUserId(), PortalTypeEnum.PORTAL);
        this.ownerUserNickname = UserUtil.getUserNicknameByIdFromLocal(e.getOwnerUserId());
    }
}
