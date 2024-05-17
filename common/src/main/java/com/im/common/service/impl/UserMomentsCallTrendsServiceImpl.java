package com.im.common.service.impl;

import com.im.common.entity.UserMomentsCallTrends;
import com.im.common.entity.UserMomentsReview;
import com.im.common.mapper.UserMomentsCallTrendsMapper;
import com.im.common.mapper.UserMomentsReviewMapper;
import com.im.common.param.IdParam;
import com.im.common.param.IdReadUnreadParam;
import com.im.common.param.MomentsIdParam;
import com.im.common.response.ResponseCode;
import com.im.common.response.RestResponse;
import com.im.common.service.UserMomentsCallTrendsService;
import com.im.common.service.UserMomentsReviewService;
import com.im.common.util.mybatis.service.MyBatisPlusServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 朋友圈提醒谁看
 * @author max.stark
 */
@Service
public class UserMomentsCallTrendsServiceImpl extends MyBatisPlusServiceImpl<UserMomentsCallTrendsMapper, UserMomentsCallTrends>
        implements UserMomentsCallTrendsService {

    /**
     * 根据用户查询提醒数据
     *
     * @param userId
     * @return
     */
    @Override
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public List<UserMomentsCallTrends> listByUser(Long userId, Boolean read) {
        return lambdaQuery().eq(UserMomentsCallTrends::getUserId, userId).eq(UserMomentsCallTrends::getRead, read).list();
    }

    /**
     * 标记为已读
     *
     * @param param
     * @return 返回OK表示成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResponse readForPortal(Long userId, MomentsIdParam param) {
        boolean updated = lambdaUpdate()
                .set(UserMomentsCallTrends::getRead, true)
                .eq(UserMomentsCallTrends::getUserId, userId)
                .eq(UserMomentsCallTrends::getMomentsId, param.getMomentsId())
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
        boolean removed = lambdaUpdate().eq(UserMomentsCallTrends::getId, param.getId()).remove();
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
                .set(UserMomentsCallTrends::getRead, param.getRead())
                .eq(UserMomentsCallTrends::getId, param.getId())
                .update();
        if (!updated) {
            return RestResponse.failed(ResponseCode.SYS_SERVER_ERROR);
        }
        return RestResponse.OK;
    }
}
