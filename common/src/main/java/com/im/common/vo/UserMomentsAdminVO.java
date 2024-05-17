package com.im.common.vo;

import com.im.common.constant.CommonConstant;
import com.im.common.entity.UserMoments;
import com.im.common.entity.enums.PortalTypeEnum;
import com.im.common.entity.enums.TrendsTypeEnum;
import com.im.common.util.CollectionUtil;
import com.im.common.util.StrUtil;
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
public class UserMomentsAdminVO {
    public static final BeanCopier BEAN_COPIER = BeanCopier.create(UserMoments.class, UserMomentsAdminVO.class, false);

    public UserMomentsAdminVO(UserMoments e) {
        BEAN_COPIER.copy(e, this, null);
        this.nickname = UserUtil.getUserNicknameByIdFromLocal(e.getUserId());
        this.avatar = UserUtil.getPortalUserCache().getAvatarByIdFromLocal(e.getUserId());
        this.username = UserUtil.getUsernameByIdFromLocal(e.getUserId(), PortalTypeEnum.PORTAL);

        if (StrUtil.isNotBlank(e.getImgUrls())) {
            this.imgUrlsArr = CollectionUtil.splitStrToList(e.getImgUrls(), CommonConstant.DOT_EN);
        }
    }

    private Long id;

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
     * 文字内容
     */
    private String content;

    /**
     * 朋友圈类型
     */
    private TrendsTypeEnum trendsType;

    /**
     * 图片链接地址
     */
    private List<String> imgUrlsArr;

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
     * ip
     */
    private String ip;

    private Integer likeCount;

    private Integer reviewCount;

    private Integer callCount;

    private Integer allowCount;

    private Integer shieldCount;
    /**
     * 点赞列表
     */
    private List<UserMomentsReviewVO> likeUsers;

    /**
     * 评论列表
     */
    private List<UserMomentsReviewVO> reviewUsers;

    /**
     * 提醒谁看
     */
    private List<UserMomentsCallTrendsAdminVO> callTrends;

    /**
     * 允许谁看
     */
    private List<UserMomentsTrendsAdminVO> momentsByAllow;

    /**
     * 拒绝谁看
     */
    private List<UserMomentsTrendsAdminVO> momentsByShield;
}
