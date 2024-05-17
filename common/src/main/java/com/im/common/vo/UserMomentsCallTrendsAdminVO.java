package com.im.common.vo;

import com.im.common.entity.UserMoments;
import com.im.common.entity.UserMomentsCallTrends;
import com.im.common.entity.enums.PortalTypeEnum;
import com.im.common.util.user.UserUtil;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.beans.BeanCopier;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 提醒谁看
 * @author max.stark
 */
@Data
@NoArgsConstructor
@ApiModel
public class UserMomentsCallTrendsAdminVO {

    public static final BeanCopier BEAN_COPIER = BeanCopier.create(UserMomentsCallTrends.class, UserMomentsCallTrendsAdminVO.class, false);

    public UserMomentsCallTrendsAdminVO(UserMomentsCallTrends e) {
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
     * 是否已读
     */
    private Boolean read;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;
}
