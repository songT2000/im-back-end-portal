package com.im.common.mapper;

import com.im.common.entity.UserMoments;
import com.im.common.entity.UserMomentsTrends;
import com.im.common.util.mybatis.mapper.MyBatisPlusMapper;
import org.springframework.stereotype.Repository;


/**
 * 朋友圈记录 动态
 *
 * @author max.stark
 */
@Repository
public interface UserMomentsTrendsMapper extends MyBatisPlusMapper<UserMomentsTrends> {
}
