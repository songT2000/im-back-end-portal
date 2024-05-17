package com.im.common.service.impl;

import com.im.common.entity.UserMoments;
import com.im.common.entity.UserMomentsTrends;
import com.im.common.mapper.UserMomentsMapper;
import com.im.common.mapper.UserMomentsTrendsMapper;
import com.im.common.param.IdParam;
import com.im.common.param.IdReadUnreadParam;
import com.im.common.response.RestResponse;
import com.im.common.service.UserMomentsService;
import com.im.common.service.UserMomentsTrendsService;
import com.im.common.util.mybatis.service.MyBatisPlusServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 朋友圈动态
 * @author max.stark
 */
@Service
public class UserMomentsTrendsServiceImpl
        extends MyBatisPlusServiceImpl<UserMomentsTrendsMapper, UserMomentsTrends>
        implements UserMomentsTrendsService {

}
