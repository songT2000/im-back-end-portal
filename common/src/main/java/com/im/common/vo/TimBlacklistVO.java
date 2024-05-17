package com.im.common.vo;

import com.im.common.entity.enums.PortalTypeEnum;
import com.im.common.entity.tim.TimBlacklist;
import com.im.common.entity.tim.TimFriend;
import com.im.common.util.user.UserUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.beans.BeanCopier;

import java.time.LocalDateTime;

/**
 * 黑名单信息
 */
@Data
@NoArgsConstructor
@ApiModel
public class TimBlacklistVO {
    public static final BeanCopier BEAN_COPIER = BeanCopier.create(TimBlacklist.class, TimBlacklistVO.class, false);

    public TimBlacklistVO(TimBlacklist e) {
        BEAN_COPIER.copy(e, this, null);
        this.blacklistUsername = UserUtil.getUsernameByIdFromLocal(e.getBlacklistUserId(), PortalTypeEnum.PORTAL);
        this.blacklistNickname = UserUtil.getUserNicknameByIdFromLocal(e.getBlacklistUserId());
    }

    @ApiModelProperty("黑名单用户ID")
    private Long blacklistUserId;

    @ApiModelProperty("黑名单用户的账号")
    private String blacklistUsername;

    @ApiModelProperty("黑名单用户的昵称")
    private String blacklistNickname;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;
}
