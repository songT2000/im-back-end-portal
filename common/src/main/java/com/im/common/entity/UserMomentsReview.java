package com.im.common.entity;

import com.im.common.entity.base.BaseEntity;
import com.im.common.entity.enums.TrendsReviewTypeEnum;
import com.im.common.entity.enums.TrendsTypeEnum;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 朋友圈评论记录
 *
 * @author max.stark
 */
@Data
@NoArgsConstructor
public class UserMomentsReview extends BaseEntity implements Serializable {

    private static final long serialVersionUID = -1334826234034402316L;
    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 朋友圈ID
     */
    private Long momentsId;

    /**
     * 回复用户ID，表示回复谁的评论
     */
    private Long replyUserId;

    /**
     * 评论类型
     */
    private TrendsReviewTypeEnum reviewType;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 评论的IP地址
     */
    private String ip;

}
