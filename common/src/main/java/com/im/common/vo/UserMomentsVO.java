package com.im.common.vo;

import com.im.common.entity.UserMoments;
import com.im.common.entity.UserMomentsReview;
import com.im.common.entity.enums.PortalTypeEnum;
import com.im.common.entity.tim.TimFriend;
import com.im.common.util.user.UserUtil;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.beans.BeanCopier;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 朋友圈VO
 * @author max.stark
 */
@Data
@NoArgsConstructor
@ApiModel
public class UserMomentsVO {
    public static final BeanCopier BEAN_COPIER = BeanCopier.create(UserMoments.class, UserMomentsVO.class, false);

    public UserMomentsVO(UserMoments e, List<UserMomentsReviewVO> likeUsers, List<UserMomentsReviewVO> reviewUsers) {
        BEAN_COPIER.copy(e, this, null);
        this.nickname = UserUtil.getUserNicknameByIdFromLocal(e.getUserId());
        this.avatar = UserUtil.getPortalUserCache().getAvatarByIdFromLocal(e.getUserId());
        this.likeUsers = likeUsers;
        this.reviewUsers = reviewUsers;
    }

    private Long id;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 文字内容
     */
    private String content;

    /**
     * 图片链接地址，英文逗号分割，最多9个
     */
    private List<String> imgUrls;

    /**
     * 视频链接地址，只允许一个
     */
    private String videoUrls;

    /**
     * 发布位置
     */
    private String address;

    /**
     * 发布时间
     */
    private LocalDateTime createTime;

    /**
     * 点赞用户列表
     */
    private List<UserMomentsReviewVO> likeUsers;

    /**
     * 评论用户列表
     */
    private List<UserMomentsReviewVO> reviewUsers;

}
