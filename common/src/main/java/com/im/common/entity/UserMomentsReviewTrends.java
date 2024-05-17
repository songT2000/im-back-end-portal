package com.im.common.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.im.common.entity.base.BaseEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 朋友圈记录
 *
 * @author max.stark
 */
@Data
@NoArgsConstructor
public class UserMomentsReviewTrends extends BaseEntity implements Serializable {

    private static final long serialVersionUID = -6276514274412839605L;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 朋友圈ID
     */
    private Long momentsId;

    /**
     * 朋友圈评论ID
     */
    private Long momentsReviewId;

    /**
     * 是否提醒
     */
    @TableField("`read`")
    private Boolean read;

}
