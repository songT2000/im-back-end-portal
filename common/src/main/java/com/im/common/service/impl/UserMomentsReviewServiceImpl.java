package com.im.common.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.im.common.cache.impl.TimFriendCache;
import com.im.common.entity.UserMoments;
import com.im.common.entity.UserMomentsReview;
import com.im.common.entity.UserMomentsReviewTrends;
import com.im.common.entity.UserMomentsTrends;
import com.im.common.entity.enums.RegisterTypeEnum;
import com.im.common.entity.enums.TrendsReviewTypeEnum;
import com.im.common.mapper.UserMomentsMapper;
import com.im.common.mapper.UserMomentsReviewMapper;
import com.im.common.param.IdParam;
import com.im.common.param.UserMomentsReviewAddParam;
import com.im.common.param.UserMomentsReviewLikeAddParam;
import com.im.common.response.ResponseCode;
import com.im.common.response.RestResponse;
import com.im.common.service.UserMomentsReviewService;
import com.im.common.service.UserMomentsReviewTrendsService;
import com.im.common.service.UserMomentsService;
import com.im.common.service.UserMomentsTrendsService;
import com.im.common.util.XssUtil;
import com.im.common.util.mybatis.service.MyBatisPlusServiceImpl;
import com.im.common.vo.PortalSessionUser;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 朋友圈评论记录
 * @author max.stark
 */
@Service
public class UserMomentsReviewServiceImpl extends MyBatisPlusServiceImpl<UserMomentsReviewMapper, UserMomentsReview>
        implements UserMomentsReviewService {

    private UserMomentsService userMomentsService;
    private UserMomentsTrendsService userMomentsTrendsService;
    private UserMomentsReviewTrendsService userMomentsReviewTrendsService;
    private TimFriendCache timFriendCache;

    /**
     * 新增点赞
     *
     * @param sessionUser
     * @param param
     * @return 返回OK表示成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResponse addLikeForPortal(PortalSessionUser sessionUser, UserMomentsReviewLikeAddParam param) {
        Long userId = sessionUser.getId();
        UserMoments userMoments = userMomentsService.getById(param.getMomentsId());
        if (null == userMoments) {
            return RestResponse.failed(ResponseCode.USER_MOMENTS_NOT_READ);
        }

        // 验证权限
        switch (userMoments.getTrendsType()) {
            case ONLY_ME:
                return RestResponse.failed(ResponseCode.USER_MOMENTS_NOT_READ);
            case REJECT_FRIEND:
                UserMomentsTrends rejectOne = userMomentsTrendsService.lambdaQuery()
                        .eq(UserMomentsTrends::getMomentsId, param.getMomentsId())
                        .eq(UserMomentsTrends::getAllow, Boolean.FALSE)
                        .eq(UserMomentsTrends::getUserId, userId).one();
                if (null != rejectOne) {
                    return RestResponse.failed(ResponseCode.USER_MOMENTS_NOT_READ);
                }
                break;
            case ALLOW_FRIEND:
                UserMomentsTrends allowOne = userMomentsTrendsService.lambdaQuery()
                        .eq(UserMomentsTrends::getMomentsId, param.getMomentsId())
                        .eq(UserMomentsTrends::getAllow, Boolean.TRUE)
                        .eq(UserMomentsTrends::getUserId, userId).one();
                if (null != allowOne) {
                    return RestResponse.failed(ResponseCode.USER_MOMENTS_NOT_READ);
                }
                break;
        }

        UserMomentsReview userMomentsReview = new UserMomentsReview();
        Long id = IdWorker.getId();
        userMomentsReview.setId(id);
        userMomentsReview.setMomentsId(param.getMomentsId());
        userMomentsReview.setIp(param.getIp());
        userMomentsReview.setUserId(userId);
        userMomentsReview.setReviewType(TrendsReviewTypeEnum.LIKE);
        boolean saved = save(userMomentsReview);
        if (!saved) {
            return RestResponse.failed(ResponseCode.SYS_SERVER_ERROR);
        }

        // 录入点赞提醒，提醒是自己好友并且在自己之前为本朋友圈点赞的用户
        List<UserMomentsReview> list = lambdaQuery().eq(UserMomentsReview::getMomentsId, param.getMomentsId())
                .eq(UserMomentsReview::getReviewType, TrendsReviewTypeEnum.LIKE).list();

        List<Long> friend = timFriendCache.getFriendByIdFromLocal(userId);

        // 筛选出需要提醒的用户
        Set<Long> trendsUsers = new HashSet<>();
        trendsUsers.add(userMoments.getUserId());
        for (Long uid : friend) {
            for (UserMomentsReview u : list) {
                if (uid.equals(u.getUserId())) {
                    trendsUsers.add(uid);
                    break;
                }
            }
        }

        // 添加动态提醒
        List<UserMomentsReviewTrends> userMomentsReviewTrendsList = new ArrayList<>();
        for (Long uid : trendsUsers) {
            UserMomentsReviewTrends userMomentsReviewTrends = new UserMomentsReviewTrends();
            userMomentsReviewTrends.setId(IdWorker.getId());
            userMomentsReviewTrends.setUserId(uid);
            userMomentsReviewTrends.setMomentsReviewId(id);
            userMomentsReviewTrends.setRead(Boolean.FALSE);
            userMomentsReviewTrends.setMomentsId(param.getMomentsId());
            userMomentsReviewTrendsList.add(userMomentsReviewTrends);
        }
        boolean saveBatch = userMomentsReviewTrendsService.saveBatch(userMomentsReviewTrendsList);
        if (!saveBatch) {
            return RestResponse.failed(ResponseCode.SYS_SERVER_ERROR);
        }
        return RestResponse.OK;
    }

    /**
     * 新增评论
     *
     * @param sessionUser
     * @param param
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResponse addReviewForPortal(PortalSessionUser sessionUser, UserMomentsReviewAddParam param) {

        if (!XssUtil.isValid(param.getContent())) {
            return RestResponse.failed(ResponseCode.SYS_REQUEST_ILLEGAL);
        }

        Long userId = sessionUser.getId();

        UserMoments userMoments = userMomentsService.getById(param.getMomentsId());
        if (null == userMoments) {
            return RestResponse.failed(ResponseCode.USER_MOMENTS_NOT_READ);
        }

        // 验证权限
        switch (userMoments.getTrendsType()) {
            case ONLY_ME:
                return RestResponse.failed(ResponseCode.USER_MOMENTS_NOT_READ);
            case REJECT_FRIEND:
                UserMomentsTrends rejectOne = userMomentsTrendsService.lambdaQuery()
                        .eq(UserMomentsTrends::getMomentsId, param.getMomentsId())
                        .eq(UserMomentsTrends::getAllow, Boolean.FALSE)
                        .eq(UserMomentsTrends::getUserId, userId).one();
                if (null != rejectOne) {
                    return RestResponse.failed(ResponseCode.USER_MOMENTS_NOT_READ);
                }
                break;
            case ALLOW_FRIEND:
                UserMomentsTrends allowOne = userMomentsTrendsService.lambdaQuery()
                        .eq(UserMomentsTrends::getMomentsId, param.getMomentsId())
                        .eq(UserMomentsTrends::getAllow, Boolean.TRUE)
                        .eq(UserMomentsTrends::getUserId, userId).one();
                if (null != allowOne) {
                    return RestResponse.failed(ResponseCode.USER_MOMENTS_NOT_READ);
                }
                break;
        }

        UserMomentsReview userMomentsReview = new UserMomentsReview();
        Long id = IdWorker.getId();
        userMomentsReview.setId(id);
        userMomentsReview.setMomentsId(param.getMomentsId());
        userMomentsReview.setIp(param.getIp());
        userMomentsReview.setUserId(userId);
        userMomentsReview.setReviewType(TrendsReviewTypeEnum.LIKE);
        userMomentsReview.setReplyUserId(param.getReplyUserId());
        userMomentsReview.setContent(param.getContent());
        boolean saved = save(userMomentsReview);
        if (!saved) {
            return RestResponse.failed(ResponseCode.SYS_SERVER_ERROR);
        }

        // 录入评论提醒
        Set<Long> trendsUsers = new HashSet<>();
        trendsUsers.add(userMoments.getUserId());
        if (userMomentsReview.getReplyUserId() != null) {
            trendsUsers.add(userMomentsReview.getReplyUserId());
        }
        // 添加评论动态提醒
        List<UserMomentsReviewTrends> userMomentsReviewTrendsList = new ArrayList<>();
        for (Long uid : trendsUsers) {
            UserMomentsReviewTrends userMomentsReviewTrends = new UserMomentsReviewTrends();
            userMomentsReviewTrends.setId(IdWorker.getId());
            userMomentsReviewTrends.setUserId(uid);
            userMomentsReviewTrends.setMomentsReviewId(id);
            userMomentsReviewTrends.setRead(Boolean.FALSE);
            userMomentsReviewTrends.setMomentsId(param.getMomentsId());
            userMomentsReviewTrendsList.add(userMomentsReviewTrends);
        }
        boolean saveBatch = userMomentsReviewTrendsService.saveBatch(userMomentsReviewTrendsList);
        if (!saveBatch) {
            return RestResponse.failed(ResponseCode.SYS_SERVER_ERROR);
        }
        return RestResponse.OK;
    }


    /**
     * 删除评论或点赞
     *
     * @param sessionUser
     * @param param
     * @return 返回OK表示成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResponse deleteForPortal(PortalSessionUser sessionUser, IdParam param) {
        boolean removed = lambdaUpdate()
                .eq(UserMomentsReview::getUserId, sessionUser.getId())
                .eq(UserMomentsReview::getId, param.getId()).remove();
        if (!removed) {
            return RestResponse.failed(ResponseCode.SYS_SERVER_ERROR);
        }

        //删除评论提醒
        boolean removeTrends = userMomentsReviewTrendsService.lambdaUpdate()
                .eq(UserMomentsReviewTrends::getMomentsReviewId, param.getId())
                .remove();

        if (!removeTrends) {
            return RestResponse.failed(ResponseCode.SYS_SERVER_ERROR);
        }
        return RestResponse.OK;
    }

    /**
     * 删除评论或点赞
     *
     * @param param
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResponse deleteForAdmin(IdParam param) {
        boolean removed = lambdaUpdate()
                .eq(UserMomentsReview::getId, param.getId()).remove();
        if (!removed) {
            return RestResponse.failed(ResponseCode.SYS_SERVER_ERROR);
        }

        //删除评论提醒
        boolean removeTrends = userMomentsReviewTrendsService.lambdaUpdate()
                .eq(UserMomentsReviewTrends::getMomentsReviewId, param.getId())
                .remove();

        if (!removeTrends) {
            return RestResponse.failed(ResponseCode.SYS_SERVER_ERROR);
        }
        return RestResponse.OK;
    }

    @Autowired
    public void setUserMomentsService(UserMomentsService userMomentsService) {
        this.userMomentsService = userMomentsService;
    }

    @Autowired
    public void setUserMomentsTrendsService(UserMomentsTrendsService userMomentsTrendsService) {
        this.userMomentsTrendsService = userMomentsTrendsService;
    }

    @Autowired
    public void setUserMomentsReviewTrendsService(UserMomentsReviewTrendsService userMomentsReviewTrendsService) {
        this.userMomentsReviewTrendsService = userMomentsReviewTrendsService;
    }

    @Autowired
    public void setTimFriendCache(TimFriendCache timFriendCache) {
        this.timFriendCache = timFriendCache;
    }
}
