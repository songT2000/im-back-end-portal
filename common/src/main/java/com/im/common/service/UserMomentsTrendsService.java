package com.im.common.service;

import com.im.common.entity.UserMoments;
import com.im.common.entity.UserMomentsTrends;
import com.im.common.param.IdParam;
import com.im.common.param.IdReadUnreadParam;
import com.im.common.param.UserMomentsAddParam;
import com.im.common.response.RestResponse;
import com.im.common.util.mybatis.service.MyBatisPlusService;
import com.im.common.vo.PortalSessionUser;


/**
 * 朋友圈动态 接口
 *
 * @author max.stark
 */
public interface UserMomentsTrendsService extends MyBatisPlusService<UserMomentsTrends> {

}
