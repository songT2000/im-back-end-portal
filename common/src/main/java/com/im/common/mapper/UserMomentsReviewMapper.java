package com.im.common.mapper;

import com.im.common.entity.UserMoments;
import com.im.common.entity.UserMomentsReview;
import com.im.common.util.mybatis.mapper.MyBatisPlusMapper;
import org.springframework.stereotype.Repository;


/**
 * 朋友圈评论
 *
 * @author max.stark
 */
@Repository
public interface UserMomentsReviewMapper extends MyBatisPlusMapper<UserMomentsReview> {
}
