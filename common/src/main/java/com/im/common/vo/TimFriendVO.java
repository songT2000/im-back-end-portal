package com.im.common.vo;

import com.im.common.entity.enums.PortalTypeEnum;
import com.im.common.entity.tim.TimFriend;
import com.im.common.util.user.UserUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.beans.BeanCopier;

import java.time.LocalDateTime;

/**
 * 好友信息
 * @author max.stark
 */
@Data
@NoArgsConstructor
@ApiModel
public class TimFriendVO {
    public static final BeanCopier BEAN_COPIER = BeanCopier.create(TimFriend.class, TimFriendVO.class, false);

    public TimFriendVO(TimFriend e) {
        BEAN_COPIER.copy(e, this, null);
        this.friendUsername = UserUtil.getUsernameByIdFromLocal(e.getFriendUserId(), PortalTypeEnum.PORTAL);
        this.friendNickname = UserUtil.getUserNicknameByIdFromLocal(e.getFriendUserId());
    }

    @ApiModelProperty("用户ID")
    private Long userId;

    @ApiModelProperty("好友用户ID")
    private Long friendUserId;

    @ApiModelProperty("好友用户的账号")
    private String friendUsername;

    @ApiModelProperty("好友用户的昵称")
    private String friendNickname;

    @ApiModelProperty("备注名")
    private String aliasName;

    @ApiModelProperty("分组名称")
    private String groupName;

    @ApiModelProperty("加好友来源")
    private String addSource;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;
}
