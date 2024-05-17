package com.im.common.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.im.common.entity.PersonalRedEnvelope;
import com.im.common.entity.enums.PersonalRedEnvelopeStatusEnum;
import com.im.common.entity.enums.PortalTypeEnum;
import com.im.common.util.StrUtil;
import com.im.common.util.user.UserUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author Barry
 * @date 2022-03-22
 */
@Data
@NoArgsConstructor
@ApiModel
public class PersonalRedEnvelopePortalVO {
    @ApiModelProperty(value = "红包ID，系统自己的，不是IM的，该值一旦确定后续不会发生变化", position = 1)
    private Long id;
    @ApiModelProperty(value = "发送人昵称（注意，为了兼容版本，此处返回的是昵称，后续将废弃）", position = 2)
    private String username;
    @JSONField(format = "#0.00")
    @ApiModelProperty(value = "金额，该值一旦确定后续不会发生变化", position = 3)
    private BigDecimal amount;
    @ApiModelProperty(value = "状态，字符串，1=未领取，2=已领取，3=已退回，4=已过期，该值只要发生变化服务器就会发送IM消息", position = 4)
    private PersonalRedEnvelopeStatusEnum status;
    @ApiModelProperty(value = "备注，用来显示在红包上的文字，有可能为空，为空则显示[恭喜发财，大吉大利]，该值一旦确定后续不会发生变化", position = 5)
    private String remark;
    @ApiModelProperty(value = "发送人头像", position = 6)
    private String avatar;
    @ApiModelProperty(value = "接收人用户名", position = 7)
    private String receiveUsername;
    @ApiModelProperty(value = "接收人头像", position = 8)
    private String receiveUserAvatar;
    @ApiModelProperty(value = "接收人用户昵称", position = 9)
    private String receiveUserNickname;
    @ApiModelProperty(value = "发送人用户名", position = 10)
    private String sendUsername;
    @ApiModelProperty(value = "发送人头像", position = 11)
    private String sendUserAvatar;
    @ApiModelProperty(value = "发送人用户昵称", position = 12)
    private String sendUserNickname;

    public PersonalRedEnvelopePortalVO(PersonalRedEnvelope envelope) {
        this.id = envelope.getId();
        this.username = UserUtil.getUserNicknameByIdFromLocal(envelope.getUserId());
        this.amount = envelope.getAmount();
        this.status = envelope.getStatus();
        this.remark = envelope.getRemark();
        this.avatar = UserUtil.getUserAvatarByIdFromLocal(envelope.getUserId());
        this.receiveUsername = UserUtil.getUsernameByIdFromLocal(envelope.getReceiveUserId(), PortalTypeEnum.PORTAL);
        this.receiveUserAvatar = UserUtil.getUserAvatarByIdFromLocal(envelope.getReceiveUserId());
        this.receiveUserNickname = UserUtil.getUserNicknameByIdFromLocal(envelope.getReceiveUserId());
        this.sendUsername = UserUtil.getUsernameByIdFromLocal(envelope.getUserId(), PortalTypeEnum.PORTAL);
        this.sendUserAvatar = UserUtil.getUserAvatarByIdFromLocal(envelope.getUserId());
        this.sendUserNickname = UserUtil.getUserNicknameByIdFromLocal(envelope.getUserId());
        if (StrUtil.isEmpty(this.avatar)) {
            this.avatar = StrUtil.EMPTY;
        }
        if (StrUtil.isEmpty(this.receiveUserAvatar)) {
            this.receiveUserAvatar = StrUtil.EMPTY;
        }
        if (StrUtil.isEmpty(this.sendUserAvatar)) {
            this.sendUserAvatar = StrUtil.EMPTY;
        }
    }

    public void setAvatar(String avatar) {
        this.avatar = StrUtil.isEmpty(avatar) ? StrUtil.EMPTY : avatar;
    }
}
