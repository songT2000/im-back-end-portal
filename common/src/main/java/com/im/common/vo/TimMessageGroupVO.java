package com.im.common.vo;

import com.im.common.entity.enums.PortalTypeEnum;
import com.im.common.entity.tim.TimMessageBody;
import com.im.common.entity.tim.TimMessageGroup;
import com.im.common.util.StrUtil;
import com.im.common.util.user.UserUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.beans.BeanCopier;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 群组信息
 */
@Data
@NoArgsConstructor
@ApiModel
public class TimMessageGroupVO {
    public static final BeanCopier BEAN_COPIER = BeanCopier.create(TimMessageGroup.class, TimMessageGroupVO.class, false);
    @ApiModelProperty("ID")
    private Long id;
    @ApiModelProperty("群组的ID")
    private String groupId;
    @ApiModelProperty("群组的名称")
    private String groupName;
    @ApiModelProperty("发言人ID")
    private Long fromUserId;
    @ApiModelProperty("发言人账号")
    private String fromUsername;
    @ApiModelProperty("发言人昵称")
    private String fromUserNickname;
    @ApiModelProperty("发言人头像")
    private String fromUserAvatar;
    @ApiModelProperty("消息序列号")
    private Long msgSeq;
    @ApiModelProperty("消息来自客户端")
    private String msgFromPlatform;
    @ApiModelProperty("消息发送时间")
    private LocalDateTime sendTime;
    @ApiModelProperty("消息来源IP")
    private String clientIp;
    @ApiModelProperty("消息内容")
    private List<TimMessageBody> msgBody;

    public TimMessageGroupVO(TimMessageGroup e) {
        BEAN_COPIER.copy(e, this, null);
        this.fromUsername = UserUtil.getUsernameByIdFromLocal(e.getFromUserId(), PortalTypeEnum.PORTAL);
        this.fromUserNickname = UserUtil.getUserNicknameByIdFromLocal(e.getFromUserId());
        this.fromUserAvatar = UserUtil.getUserAvatarByIdFromLocal(e.getFromUserId());
        if (StrUtil.isBlank(groupName)) {
            groupName = "[群组已解散]";
        }
    }
}
