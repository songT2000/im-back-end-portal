package com.im.common.vo;

import com.im.common.entity.UserMomentsCallTrends;
import com.im.common.entity.UserMomentsTrends;
import com.im.common.entity.enums.PortalTypeEnum;
import com.im.common.util.user.UserUtil;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.beans.BeanCopier;

import java.time.LocalDateTime;

/**
 * 朋友圈提醒
 * @author max.stark
 */
@Data
@NoArgsConstructor
@ApiModel
public class UserMomentsTrendsAdminVO {

    public static final BeanCopier BEAN_COPIER = BeanCopier.create(UserMomentsTrends.class, UserMomentsTrendsAdminVO.class, false);

    public UserMomentsTrendsAdminVO(UserMomentsTrends e) {
        BEAN_COPIER.copy(e, this, null);
        this.nickname = UserUtil.getUserNicknameByIdFromLocal(e.getUserId());
        this.avatar = UserUtil.getPortalUserCache().getAvatarByIdFromLocal(e.getUserId());
        this.username = UserUtil.getUsernameByIdFromLocal(e.getUserId(), PortalTypeEnum.PORTAL);
    }

    /**
     * ID
     */
    private Long id;

    /**
     * 朋友圈ID
     */
    private Long momentsId;

    /**
     * 用户名
     */
    private Long userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 是否允许
     */
    private Boolean allow;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;
}
