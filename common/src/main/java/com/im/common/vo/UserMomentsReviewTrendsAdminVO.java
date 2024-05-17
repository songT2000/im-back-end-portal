package com.im.common.vo;

import com.im.common.entity.UserMomentsCallTrends;
import com.im.common.entity.UserMomentsReviewTrends;
import com.im.common.entity.UserMomentsTrends;
import com.im.common.entity.enums.PortalTypeEnum;
import com.im.common.util.user.UserUtil;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.beans.BeanCopier;

import java.time.LocalDateTime;

/**
 * 评论动态
 * @author max.stark
 */
@Data
@NoArgsConstructor
@ApiModel
public class UserMomentsReviewTrendsAdminVO {

    public static final BeanCopier BEAN_COPIER = BeanCopier.create(UserMomentsReviewTrends.class, UserMomentsReviewTrendsAdminVO.class, false);

    public UserMomentsReviewTrendsAdminVO(UserMomentsReviewTrends e) {
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
     * 评论ID
     */
    private Long momentsReviewId;

    /**
     * 用户id
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
