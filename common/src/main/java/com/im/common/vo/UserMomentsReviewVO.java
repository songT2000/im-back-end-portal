package com.im.common.vo;

import com.im.common.entity.UserMoments;
import com.im.common.entity.UserMomentsReview;
import com.im.common.entity.enums.PortalTypeEnum;
import com.im.common.entity.enums.TrendsReviewTypeEnum;
import com.im.common.util.user.UserUtil;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.beans.BeanCopier;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 评论VO
 * @author max.stark
 */
@Data
@NoArgsConstructor
@ApiModel
public class UserMomentsReviewVO {
    public static final BeanCopier BEAN_COPIER = BeanCopier.create(UserMomentsReview.class, UserMomentsReviewVO.class, false);

    public UserMomentsReviewVO(UserMomentsReview e) {
        BEAN_COPIER.copy(e, this, null);
        this.username = UserUtil.getUsernameByIdFromLocal(e.getUserId(), PortalTypeEnum.PORTAL);
        this.nickname = UserUtil.getUserNicknameByIdFromLocal(e.getUserId());
        this.avatar = UserUtil.getPortalUserCache().getAvatarByIdFromLocal(e.getUserId());

        this.replyUserUsername = UserUtil.getUsernameByIdFromLocal(e.getReplyUserId(), PortalTypeEnum.PORTAL);
        this.replyUserNickname = UserUtil.getUserNicknameByIdFromLocal(e.getReplyUserId());
        this.replyUserAvatar = UserUtil.getPortalUserCache().getAvatarByIdFromLocal(e.getReplyUserId());
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
     * 回复用户名
     */
    private String replyUserUsername;

    /**
     * 回复用户 昵称
     */
    private String replyUserNickname;

    /**
     * 回复用户 头像
     */
    private String replyUserAvatar;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 评论时间
     */
    private LocalDateTime createTime;

    /**
     * 提示了谁看
     */
    private List<UserMomentsReviewTrendsAdminVO> reviewTrends;
}
