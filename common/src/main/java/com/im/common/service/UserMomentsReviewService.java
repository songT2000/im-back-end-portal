package com.im.common.service;

import com.im.common.entity.UserMoments;
import com.im.common.entity.UserMomentsReview;
import com.im.common.param.IdParam;
import com.im.common.param.UserMomentsAddParam;
import com.im.common.param.UserMomentsReviewAddParam;
import com.im.common.param.UserMomentsReviewLikeAddParam;
import com.im.common.response.RestResponse;
import com.im.common.util.mybatis.service.MyBatisPlusService;
import com.im.common.vo.PortalSessionUser;
import com.im.common.vo.UserMomentsReviewAdminVO;

import java.util.List;


/**
 * 朋友圈评论信息 接口
 *
 * @author max.stark
 */
public interface UserMomentsReviewService extends MyBatisPlusService<UserMomentsReview> {

    /**
     * 新增点赞
     *
     * @param sessionUser
     * @param param
     * @return 返回OK表示成功
     */
    RestResponse addLikeForPortal(PortalSessionUser sessionUser, UserMomentsReviewLikeAddParam param);

    /**
     * 新增评论
     * @param sessionUser
     * @param param
     * @return
     */
    RestResponse addReviewForPortal(PortalSessionUser sessionUser, UserMomentsReviewAddParam param);

    /**
     * 删除评论或点赞
     *
     * @param sessionUser
     * @param param
     * @return 返回OK表示成功
     */
    RestResponse deleteForPortal(PortalSessionUser sessionUser, IdParam param);

    /**
     * 删除评论或点赞
     * @param param
     * @return
     */
    RestResponse deleteForAdmin(IdParam param);
}
