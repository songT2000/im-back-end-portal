package com.im.common.vo;

import com.im.common.entity.enums.PortalTypeEnum;
import com.im.common.entity.tim.TimMessageBody;
import com.im.common.entity.tim.TimMessageC2c;
import com.im.common.entity.tim.TimUserDeviceState;
import com.im.common.util.user.UserUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.beans.BeanCopier;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 在线用户信息
 */
@Data
@NoArgsConstructor
@ApiModel
public class TimUserOnlineVO {
    public static final BeanCopier BEAN_COPIER = BeanCopier.create(TimUserDeviceState.class, TimUserOnlineVO.class, false);

    public TimUserOnlineVO(TimUserDeviceState e) {
        BEAN_COPIER.copy(e, this, null);
        this.username = UserUtil.getUsernameByIdFromLocal(e.getUserId(), PortalTypeEnum.PORTAL);
        this.nickname = UserUtil.getUserNicknameByIdFromLocal(e.getUserId());
        this.avatar = UserUtil.getUserAvatarByIdFromLocal(e.getUserId());
    }

    @ApiModelProperty("用户ID")
    private Long userId;

    @ApiModelProperty("用户账号")
    private String username;

    @ApiModelProperty("用户昵称")
    private String nickname;

    @ApiModelProperty("用户头像")
    private String avatar;

    @ApiModelProperty("客户端")
    private String deviceType;

    @ApiModelProperty("最后登陆IP")
    private String clientIp;

}
