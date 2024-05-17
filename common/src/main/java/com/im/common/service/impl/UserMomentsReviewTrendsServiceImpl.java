package com.im.common.service.impl;

import com.im.common.entity.UserMomentsReview;
import com.im.common.entity.UserMomentsReviewTrends;
import com.im.common.mapper.UserMomentsReviewMapper;
import com.im.common.mapper.UserMomentsReviewTrendsMapper;
import com.im.common.param.IdParam;
import com.im.common.param.IdReadUnreadParam;
import com.im.common.param.MomentsIdParam;
import com.im.common.response.ResponseCode;
import com.im.common.response.RestResponse;
import com.im.common.service.UserMomentsReviewService;
import com.im.common.service.UserMomentsReviewTrendsService;
import com.im.common.util.mybatis.service.MyBatisPlusServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 朋友圈评论动态
 * @author max.stark
 */
@Service
public class UserMomentsReviewTrendsServiceImpl extends MyBatisPlusServiceImpl<UserMomentsReviewTrendsMapper, UserMomentsReviewTrends>
        implements UserMomentsReviewTrendsService {

    /**
     * 根据用户查询
     *
     * @param userId
     * @param read
     * @return
     */
    @Override
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public List<UserMomentsReviewTrends> listByUser(Long userId, Boolean read) {
        return lambdaQuery().eq(UserMomentsReviewTrends::getUserId, userId).eq(UserMomentsReviewTrends::getRead, read).list();
    }

    /**
     * 标记为已读
     * @param userId
     * @param param
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResponse readForPortal(Long userId, MomentsIdParam param) {
        boolean updated = lambdaUpdate()
                .set(UserMomentsReviewTrends::getRead, true)
                .eq(UserMomentsReviewTrends::getUserId, userId)
                .eq(UserMomentsReviewTrends::getMomentsId, param.getMomentsId())
                .update();
        if (!updated) {
            return RestResponse.failed(ResponseCode.SYS_SERVER_ERROR);
        }
        return RestResponse.OK;
    }

    /**
     * 删除提醒数据
     *
     * @param param 主键
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResponse deleteForAdmin(IdParam param) {
        boolean removed = lambdaUpdate().eq(UserMomentsReviewTrends::getId, param.getId()).remove();
        if (!removed) {
            return RestResponse.failed(ResponseCode.SYS_SERVER_ERROR);
        }
        return RestResponse.OK;
    }

    /**
     * 编辑已读状态
     *
     * @param param
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResponse readForAdmin(IdReadUnreadParam param) {
        boolean updated = lambdaUpdate()
                .set(UserMomentsReviewTrends::getRead, param.getRead())
                .eq(UserMomentsReviewTrends::getId, param.getId())
                .update();
        if (!updated) {
            return RestResponse.failed(ResponseCode.SYS_SERVER_ERROR);
        }
        return RestResponse.OK;
    }
}
