package com.im.common.vo;

import com.im.common.entity.enums.GroupMemberRoleEnum;
import com.im.common.entity.enums.PortalTypeEnum;
import com.im.common.entity.tim.TimGroupMember;
import com.im.common.util.user.UserUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.beans.BeanCopier;

import java.time.LocalDateTime;

/**
 * 群组成员信息
 */
@Data
@NoArgsConstructor
@ApiModel
public class TimGroupMemberVO {
    public static final BeanCopier BEAN_COPIER = BeanCopier.create(TimGroupMember.class, TimGroupMemberVO.class, false);

    public TimGroupMemberVO(TimGroupMember e) {
        BEAN_COPIER.copy(e, this, null);
        this.username = UserUtil.getUsernameByIdFromLocal(e.getUserId(), PortalTypeEnum.PORTAL);
        this.nickname = UserUtil.getUserNicknameByIdFromLocal(e.getUserId());
        this.avatar = UserUtil.getUserAvatarByIdFromLocal(e.getUserId());
    }

    @ApiModelProperty("成员ID")
    private Long userId;

    @ApiModelProperty("成员账号")
    private String username;

    @ApiModelProperty("成员昵称")
    private String nickname;

    @ApiModelProperty("成员头像")
    private String avatar;

    @ApiModelProperty("群组ID")
    private String groupId;

    @ApiModelProperty("成员身份")
    private GroupMemberRoleEnum role;

    @ApiModelProperty("禁言时间")
    private LocalDateTime shutUpEndTime;

    @ApiModelProperty("入群时间")
    private LocalDateTime joinTime;

    @ApiModelProperty("群名片")
    private String nameCard;
}
