package com.im.common.mapper;

import com.im.common.entity.UserMoments;
import com.im.common.util.mybatis.mapper.MyBatisPlusMapper;
import org.springframework.stereotype.Repository;


/**
 * 朋友圈记录
 *
 * @author max.stark
 */
@Repository
public interface UserMomentsMapper extends MyBatisPlusMapper<UserMoments> {
}
