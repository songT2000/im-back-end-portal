package com.im.common.vo;

import com.im.common.entity.enums.PortalTypeEnum;
import com.im.common.entity.tim.TimMessageBody;
import com.im.common.entity.tim.TimMessageC2c;
import com.im.common.entity.tim.TimMessageGroup;
import com.im.common.util.user.UserUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.beans.BeanCopier;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 单聊信息
 */
@Data
@NoArgsConstructor
@ApiModel
public class TimMessageC2cVO {
    public static final BeanCopier BEAN_COPIER = BeanCopier.create(TimMessageC2c.class, TimMessageC2cVO.class, false);

    public TimMessageC2cVO(TimMessageC2c e) {
        BEAN_COPIER.copy(e, this, null);
        this.fromUsername = UserUtil.getUsernameByIdFromLocal(e.getFromUserId(), PortalTypeEnum.PORTAL);
        this.fromUserNickname = UserUtil.getUserNicknameByIdFromLocal(e.getFromUserId());
        this.fromUserAvatar = UserUtil.getUserAvatarByIdFromLocal(e.getFromUserId());
        this.toUsername = UserUtil.getUsernameByIdFromLocal(e.getToUserId(), PortalTypeEnum.PORTAL);
        this.toUserNickname = UserUtil.getUserNicknameByIdFromLocal(e.getToUserId());
        this.toUserAvatar = UserUtil.getUserAvatarByIdFromLocal(e.getToUserId());
    }
    @ApiModelProperty("ID")
    private Long id;

    @ApiModelProperty("发言人ID")
    private Long fromUserId;

    @ApiModelProperty("发言人账号")
    private String fromUsername;

    @ApiModelProperty("发言人昵称")
    private String fromUserNickname;

    @ApiModelProperty("发言人头像")
    private String fromUserAvatar;

    @ApiModelProperty("接收人ID")
    private Long toUserId;

    @ApiModelProperty("接收人账号")
    private String toUsername;

    @ApiModelProperty("接收人昵称")
    private String toUserNickname;

    @ApiModelProperty("接收人头像")
    private String toUserAvatar;

    @ApiModelProperty("消息唯一标识符，用于撤回")
    private String msgKey;

    @ApiModelProperty("消息来自客户端")
    private String msgFromPlatform;

    @ApiModelProperty("消息发送时间")
    private LocalDateTime sendTime;

    @ApiModelProperty("消息来源IP")
    private String clientIp;

    @ApiModelProperty("消息内容")
    private List<TimMessageBody> msgBody;
}
