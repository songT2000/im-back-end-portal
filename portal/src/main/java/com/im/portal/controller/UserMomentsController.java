package com.im.portal.controller;

import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.im.common.entity.enums.UserOperationLogTypeEnum;
import com.im.common.param.*;
import com.im.common.response.RestResponse;
import com.im.common.service.UserMomentsCallTrendsService;
import com.im.common.service.UserMomentsReviewService;
import com.im.common.service.UserMomentsReviewTrendsService;
import com.im.common.service.UserMomentsService;
import com.im.common.util.aop.log.UserOperationLog;
import com.im.common.util.ip.RequestIp;
import com.im.common.util.mybatis.page.PageVO;
import com.im.common.vo.PortalSessionUser;
import com.im.common.vo.UserMomentsTrendsVO;
import com.im.common.vo.UserMomentsVO;
import com.im.portal.controller.url.ApiUrl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * 朋友圈接口
 * @author max.stark
 */
@RestController
@Api(tags = "朋友圈接口")
@ApiSupport(order = 11)
public class UserMomentsController extends BaseController {
    private UserMomentsService userMomentsService;
    private UserMomentsReviewService userMomentsReviewService;
    private UserMomentsCallTrendsService userMomentsCallTrendsService;
    private UserMomentsReviewTrendsService userMomentsReviewTrendsService;


    @RequestMapping(value = ApiUrl.USER_MOMENTS_LIKE_ADD, method = RequestMethod.POST)
    @UserOperationLog(type = UserOperationLogTypeEnum.USER_MOMENTS_LIKE_ADD)
    @ApiOperation("朋友圈点赞-新增")
    public RestResponse userMomentsLikeAdd(HttpServletRequest request,
                                           @RequestIp String requestIp,
                                           @RequestBody @Valid UserMomentsReviewLikeAddParam param) {
        PortalSessionUser sessionUser = getSessionUser(request);
        param.setIp(requestIp);
        return userMomentsReviewService.addLikeForPortal(sessionUser, param);
    }

    @RequestMapping(value = ApiUrl.USER_MOMENTS_LIKE_DELETE, method = RequestMethod.POST)
    @UserOperationLog(type = UserOperationLogTypeEnum.USER_MOMENTS_LIKE_DELETE)
    @ApiOperation("朋友圈点赞-删除")
    public RestResponse userMomentsLikeDelete(HttpServletRequest request, @RequestBody @Valid IdParam param) {
        PortalSessionUser sessionUser = getSessionUser(request);
        return userMomentsReviewService.deleteForPortal(sessionUser, param);
    }

    @RequestMapping(value = ApiUrl.USER_MOMENTS_REVIEW_ADD, method = RequestMethod.POST)
    @UserOperationLog(type = UserOperationLogTypeEnum.USER_MOMENTS_REVIEW_ADD)
    @ApiOperation("朋友圈评论-新增")
    public RestResponse userMomentsReviewAdd(HttpServletRequest request,
                                             @RequestIp String requestIp,
                                             @RequestBody @Valid UserMomentsReviewAddParam param) {
        PortalSessionUser sessionUser = getSessionUser(request);
        param.setIp(requestIp);
        return userMomentsReviewService.addReviewForPortal(sessionUser, param);
    }

    @RequestMapping(value = ApiUrl.USER_MOMENTS_REVIEW_DELETE, method = RequestMethod.POST)
    @UserOperationLog(type = UserOperationLogTypeEnum.USER_MOMENTS_REVIEW_DELETE)
    @ApiOperation("朋友圈评论-删除")
    public RestResponse userMomentsReviewDelete(HttpServletRequest request, @RequestBody @Valid IdParam param) {
        PortalSessionUser sessionUser = getSessionUser(request);
        return userMomentsReviewService.deleteForPortal(sessionUser, param);
    }

    @RequestMapping(value = ApiUrl.USER_MOMENTS_DELETE, method = RequestMethod.POST)
    @UserOperationLog(type = UserOperationLogTypeEnum.USER_MOMENTS_DELETE)
    @ApiOperation("朋友圈-删除")
    public RestResponse userMomentsDelete(HttpServletRequest request, @RequestBody @Valid IdParam param) {
        PortalSessionUser sessionUser = getSessionUser(request);
        return userMomentsService.deleteForPortal(sessionUser, param);
    }

    @RequestMapping(value = ApiUrl.USER_MOMENTS_ADD, method = RequestMethod.POST)
    @UserOperationLog(type = UserOperationLogTypeEnum.USER_MOMENTS_ADD)
    @ApiOperation("朋友圈-发布")
    public RestResponse userMomentsAdd(HttpServletRequest request,
                                                              @RequestIp String requestIp,
                                                              @RequestBody @Valid UserMomentsAddParam param) {
        PortalSessionUser sessionUser = getSessionUser(request);
        param.setIp(requestIp);
        return userMomentsService.addForPortal(sessionUser, param);
    }

    @RequestMapping(value = ApiUrl.USER_MOMENTS_PAGE, method = RequestMethod.POST)
    @ApiOperation("朋友圈分页")
    public RestResponse<PageVO<UserMomentsVO>> userMomentsPage(HttpServletRequest request, @RequestBody @Valid UserMomentsPortalParam param) {
        PortalSessionUser sessionUser = getSessionUser(request);
        return userMomentsService.pageForPortal(sessionUser, param);
    }

    @RequestMapping(value = ApiUrl.USER_MOMENTS_BY_USER, method = RequestMethod.POST)
    @ApiOperation("某个人的朋友圈")
    public RestResponse<PageVO<UserMomentsVO>> userMomentsByUserPage(HttpServletRequest request, @RequestBody @Valid UserMomentsByUserPortalParam param) {
        PortalSessionUser sessionUser = getSessionUser(request);
        return userMomentsService.pageByUserForPortal(sessionUser, param);
    }

    @RequestMapping(value = ApiUrl.USER_MOMENTS_DETAIL, method = RequestMethod.POST)
    @ApiOperation("单条朋友圈详情")
    public RestResponse<UserMomentsVO> userMomentsDetail(HttpServletRequest request, @RequestBody @Valid IdParam param) {
        PortalSessionUser sessionUser = getSessionUser(request);
        return userMomentsService.detailForPortal(sessionUser, param);
    }

    @RequestMapping(value = ApiUrl.USER_MOMENTS_TRENDS, method = RequestMethod.POST)
    @ApiOperation("朋友圈动态提醒查询")
    public RestResponse<UserMomentsTrendsVO> userMomentsTrends(HttpServletRequest request) {
        PortalSessionUser sessionUser = getSessionUser(request);
        return userMomentsService.trendsForPortal(sessionUser);
    }

    @RequestMapping(value = ApiUrl.USER_MOMENTS_TRENDS_CALL_READ, method = RequestMethod.POST)
    @ApiOperation("朋友圈提醒我看，动态提醒已读")
    public RestResponse<UserMomentsTrendsVO> userMomentsTrendsCallRead(HttpServletRequest request, @RequestBody @Valid MomentsIdParam param) {
        PortalSessionUser sessionUser = getSessionUser(request);
        return userMomentsCallTrendsService.readForPortal(sessionUser.getId(), param);
    }

    @RequestMapping(value = ApiUrl.USER_MOMENTS_TRENDS_REVIEW_READ, method = RequestMethod.POST)
    @ApiOperation("朋友圈评论，动态提醒已读")
    public RestResponse<UserMomentsTrendsVO> userMomentsTrendsReviewRead(HttpServletRequest request, @RequestBody @Valid MomentsIdParam param) {
        PortalSessionUser sessionUser = getSessionUser(request);
        return userMomentsReviewTrendsService.readForPortal(sessionUser.getId(), param);
    }


    @Autowired
    public void setUserMomentsService(UserMomentsService userMomentsService) {
        this.userMomentsService = userMomentsService;
    }

    @Autowired
    public void setUserMomentsReviewService(UserMomentsReviewService userMomentsReviewService) {
        this.userMomentsReviewService = userMomentsReviewService;
    }

    @Autowired
    public void setUserMomentsCallTrendsService(UserMomentsCallTrendsService userMomentsCallTrendsService) {
        this.userMomentsCallTrendsService = userMomentsCallTrendsService;
    }

    @Autowired
    public void setUserMomentsReviewTrendsService(UserMomentsReviewTrendsService userMomentsReviewTrendsService) {
        this.userMomentsReviewTrendsService = userMomentsReviewTrendsService;
    }
}
