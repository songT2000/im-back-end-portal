package com.im.common.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.im.common.entity.GroupRedEnvelopeReceive;
import com.im.common.entity.enums.PortalTypeEnum;
import com.im.common.entity.tim.TimGroupMember;
import com.im.common.util.CollectionUtil;
import com.im.common.util.StrUtil;
import com.im.common.util.user.UserUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * 群红包领取记录
 *
 * @author Barry
 * @date 2021-12-20
 */
@Data
@NoArgsConstructor
@ApiModel
public class GroupRedEnvelopeReceivePortalVO {
    @ApiModelProperty(value = "ID", position = 1)
    private Long id;
    @ApiModelProperty(value = "群成员昵称（注意，为了兼容版本，此处返回的是昵称）", position = 2)
    private String username;
    @ApiModelProperty(value = "群内昵称", position = 3)
    private String nameCard;
    @ApiModelProperty(value = "群成员头像", position = 4)
    private String avatar;
    @JSONField(format = "#0.00")
    @ApiModelProperty(value = "金额，最多只会有2位小数", position = 5)
    private BigDecimal amount;
    @ApiModelProperty(value = "领取人用户名", position = 6)
    private String receiveUsername;
    @ApiModelProperty(value = "领取人头像", position = 7)
    private String receiveUserAvatar;
    @ApiModelProperty(value = "领取人用户昵称", position = 8)
    private String receiveUserNickname;

    public GroupRedEnvelopeReceivePortalVO(GroupRedEnvelopeReceive receive, List<TimGroupMember> memberList) {
        this.id = receive.getId();
        this.username = UserUtil.getUserNicknameByIdFromLocal(receive.getReceiveUserId());

        TimGroupMember member = CollectionUtil.findFirst(memberList, e -> receive.getReceiveUserId().equals(e.getUserId()));

        if (member == null || StrUtil.isEmpty(member.getNameCard())) {
            this.nameCard = this.username;
        } else {
            this.nameCard = member.getNameCard();
        }
        this.avatar = UserUtil.getUserAvatarByIdFromLocal(receive.getReceiveUserId());
        this.amount = receive.getAmount();
        this.receiveUsername = UserUtil.getUsernameByIdFromLocal(receive.getReceiveUserId(), PortalTypeEnum.PORTAL);
        this.receiveUserAvatar = UserUtil.getUserAvatarByIdFromLocal(receive.getReceiveUserId());
        this.receiveUserNickname = UserUtil.getUserNicknameByIdFromLocal(receive.getReceiveUserId());
    }
}
