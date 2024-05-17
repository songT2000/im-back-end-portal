package com.im.common.vo;

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
import java.time.LocalDateTime;
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
public class GroupRedEnvelopeReceiveAdminVO {
    public GroupRedEnvelopeReceiveAdminVO(GroupRedEnvelopeReceive receive, List<TimGroupMember> memberList) {
        this.id = receive.getId();
        this.username = UserUtil.getUsernameByIdFromLocal(receive.getReceiveUserId(), PortalTypeEnum.PORTAL);

        TimGroupMember member = CollectionUtil.findFirst(memberList, e -> receive.getReceiveUserId().equals(e.getUserId()));

        if (member == null || StrUtil.isEmpty(member.getNameCard())) {
            this.nameCard = this.username;
        } else {
            this.nameCard = member.getNameCard();
        }
        this.amount = receive.getAmount();
        this.createTime = receive.getCreateTime();
    }

    @ApiModelProperty(value = "ID", position = 1)
    private Long id;

    @ApiModelProperty(value = "用户名", position = 2)
    private String username;

    @ApiModelProperty(value = "群名片，群内昵称", position = 3)
    private String nameCard;

    @ApiModelProperty(value = "金额，最多只会有2位小数", position = 4)
    private BigDecimal amount;

    @ApiModelProperty(value = "领取时间", position = 5)
    private LocalDateTime createTime;
}
