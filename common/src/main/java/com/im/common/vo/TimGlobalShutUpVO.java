package com.im.common.vo;

import com.im.common.entity.base.BaseEntity;
import com.im.common.entity.enums.PortalTypeEnum;
import com.im.common.entity.tim.TimGlobalShutUp;
import com.im.common.entity.tim.TimMessageGroup;
import com.im.common.util.user.UserUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.beans.BeanCopier;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TimGlobalShutUpVO extends BaseEntity {
    public static final BeanCopier BEAN_COPIER = BeanCopier.create(TimGlobalShutUp.class, TimGlobalShutUpVO.class, false);

    public TimGlobalShutUpVO(TimGlobalShutUp e) {
        BEAN_COPIER.copy(e, this, null);
        this.username = UserUtil.getUsernameByIdFromLocal(e.getUserId(), PortalTypeEnum.PORTAL);
    }

    @ApiModelProperty("用户ID")
    private Long userId;

    @ApiModelProperty("用户账号")
    private String username;

    @ApiModelProperty("单聊禁言截止时间")
    private LocalDateTime c2cShutupEndTime;

    @ApiModelProperty("群组禁言截止时间")
    private LocalDateTime groupShutupEndTime;
}
