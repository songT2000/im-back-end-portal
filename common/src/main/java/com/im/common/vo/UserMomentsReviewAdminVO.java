package com.im.common.vo;

import com.im.common.entity.UserMomentsReview;
import com.im.common.entity.enums.PortalTypeEnum;
import com.im.common.util.user.UserUtil;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.beans.BeanCopier;

import java.time.LocalDateTime;

/**
 * 评论VO
 * @author max.stark
 */
@Data
@NoArgsConstructor
@ApiModel
public class UserMomentsReviewAdminVO {
    public static final BeanCopier BEAN_COPIER = BeanCopier.create(UserMomentsReview.class, UserMomentsReviewAdminVO.class, false);

    public UserMomentsReviewAdminVO(UserMomentsReview e) {
        BEAN_COPIER.copy(e, this, null);
        this.nickname = UserUtil.getUserNicknameByIdFromLocal(e.getUserId());
        this.avatar = UserUtil.getPortalUserCache().getAvatarByIdFromLocal(e.getUserId());
        this.username = UserUtil.getUsernameByIdFromLocal(e.getUserId(), PortalTypeEnum.PORTAL);

        this.replyUserNickname = UserUtil.getUserNicknameByIdFromLocal(e.getReplyUserId());
        this.replyUserAvatar = UserUtil.getPortalUserCache().getAvatarByIdFromLocal(e.getReplyUserId());
        this.replyUsername = UserUtil.getUsernameByIdFromLocal(e.getReplyUserId(), PortalTypeEnum.PORTAL);

    }

    private Long id;
    /**
     * 用户ID
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
     * 朋友圈ID
     */
    private Long momentsId;

    /**
     * 回复用户ID，表示回复谁的评论
     */
    private Long replyUserId;

    /**
     * 回复用户 昵称
     */
    private String replyUserNickname;

    /**
     * 回复用户 头像
     */
    private String replyUserAvatar;

    /**
     * 回复的用户名
     */
    private String replyUsername;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 评论时间
     */
    private LocalDateTime createTime;

}
