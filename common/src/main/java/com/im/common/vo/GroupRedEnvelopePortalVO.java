package com.im.common.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.im.common.entity.GroupRedEnvelope;
import com.im.common.entity.enums.GroupRedEnvelopeStatusEnum;
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
public class GroupRedEnvelopePortalVO {
    @ApiModelProperty(value = "红包ID，系统自己的，不是IM的，该值一旦确定后续不会发生变化", position = 1)
    private Long id;
    @ApiModelProperty(value = "发送人昵称（注意，为了兼容版本，此处返回的是昵称）", position = 2)
    private String username;
    @JSONField(format = "#0.00")
    @ApiModelProperty(value = "金额，该值一旦确定后续不会发生变化", position = 3)
    private BigDecimal amount;
    @ApiModelProperty(value = "拆分个数，该值一旦确定后续不会发生变化", position = 4)
    private Integer num;
    @ApiModelProperty(value = "状态，字符串，1=未领取，2=部分领取，3=全部领取，4=已过期，该值只要发生变化服务器就会发送IM消息", position = 5)
    private GroupRedEnvelopeStatusEnum status;
    @JSONField(format = "#0.00")
    @ApiModelProperty(value = "已领取总金额", position = 6)
    private BigDecimal receivedAmount;
    @ApiModelProperty(value = "已领取总个数", position = 7)
    private Integer receivedNum;
    @ApiModelProperty(value = "备注，用来显示在红包上的文字，有可能为空，为空则显示[恭喜发财，大吉大利]，该值一旦确定后续不会发生变化", position = 8)
    private String remark;
    @ApiModelProperty(value = "当前用户是否已领取", position = 9)
    private Boolean currentUserReceived;
    @ApiModelProperty(value = "发送人头像", position = 10)
    private String avatar;
    @ApiModelProperty(value = "发送人用户名", position = 11)
    private String sendUsername;
    @ApiModelProperty(value = "发送人头像", position = 12)
    private String sendUserAvatar;
    @ApiModelProperty(value = "发送人用户昵称", position = 13)
    private String sendUserNickname;
    @ApiModelProperty(value = "当前用户（领取人）昵称", position = 14)
    private String receivedUserNickname;

    public GroupRedEnvelopePortalVO(GroupRedEnvelope envelope, Boolean currentUserReceived) {
        this.id = envelope.getId();
        this.username = UserUtil.getUserNicknameByIdFromLocal(envelope.getUserId());
        this.amount = envelope.getAmount();
        this.num = envelope.getNum();
        this.status = envelope.getStatus();
        this.receivedAmount = envelope.getReceivedAmount();
        this.receivedNum = envelope.getReceivedNum();
        this.remark = envelope.getRemark();
        this.avatar = UserUtil.getUserAvatarByIdFromLocal(envelope.getUserId());
        if (StrUtil.isEmpty(this.avatar)) {
            this.avatar = StrUtil.EMPTY;
        }
        this.sendUsername = UserUtil.getUsernameByIdFromLocal(envelope.getUserId(), PortalTypeEnum.PORTAL);
        this.sendUserAvatar = UserUtil.getUserAvatarByIdFromLocal(envelope.getUserId());
        this.sendUserNickname = UserUtil.getUserNicknameByIdFromLocal(envelope.getUserId());

        this.currentUserReceived = currentUserReceived;
    }
}
