package com.im.admin.controller;

import com.im.admin.config.permission.CheckPermission;
import com.im.admin.controller.url.ApiUrl;
import com.im.common.entity.enums.UserOperationLogTypeEnum;
import com.im.common.param.IdParam;
import com.im.common.param.UserBillPageAdminParam;
import com.im.common.param.UserMomentsPageAdminParam;
import com.im.common.response.RestResponse;
import com.im.common.service.UserMomentsReviewService;
import com.im.common.service.UserMomentsService;
import com.im.common.util.aop.log.UserOperationLog;
import com.im.common.util.mybatis.page.PageVO;
import com.im.common.vo.UserBillAdminVO;
import com.im.common.vo.UserMomentsAdminVO;
import com.im.common.vo.UserMomentsVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 朋友圈相关接口
 *
 * @author max.stark
 */
@RestController
@Api(tags = "朋友圈相关接口")
public class UserMomentsController extends BaseController {

    private UserMomentsService userMomentsService;
    private UserMomentsReviewService userMomentsReviewService;

    @RequestMapping(value = ApiUrl.USER_MOMENTS_PAGE, method = RequestMethod.POST)
    @CheckPermission
    @ApiOperation("分页")
    public RestResponse<PageVO<UserMomentsAdminVO>> userMomentsPage(@RequestBody @Valid UserMomentsPageAdminParam param) {
        return userMomentsService.pageForAdmin(param);
    }

    @RequestMapping(value = ApiUrl.USER_MOMENTS_DELETE, method = RequestMethod.POST)
    @UserOperationLog(type = UserOperationLogTypeEnum.USER_MOMENTS_DELETE)
    @CheckPermission
    @ApiOperation("删除")
    public RestResponse userMomentsDelete(@RequestBody @Valid IdParam param) {
        return userMomentsService.deleteForAdmin(param);
    }

    @RequestMapping(value = ApiUrl.USER_MOMENTS_REVIEW_DELETE, method = RequestMethod.POST)
    @UserOperationLog(type = UserOperationLogTypeEnum.USER_MOMENTS_REVIEW_DELETE)
    @CheckPermission
    @ApiOperation("删除评论")
    public RestResponse userMomentsReviewDelete(@RequestBody @Valid IdParam param) {
        return userMomentsReviewService.deleteForAdmin(param);
    }

    @RequestMapping(value = ApiUrl.USER_MOMENTS_LIKE_DELETE, method = RequestMethod.POST)
    @UserOperationLog(type = UserOperationLogTypeEnum.USER_MOMENTS_LIKE_DELETE)
    @CheckPermission
    @ApiOperation("删除点赞")
    public RestResponse userMomentsLikeDelete(@RequestBody @Valid IdParam param) {
        return userMomentsReviewService.deleteForAdmin(param);
    }


    @Autowired
    public void setUserMomentsService(UserMomentsService userMomentsService) {
        this.userMomentsService = userMomentsService;
    }

    @Autowired
    public void setUserMomentsReviewService(UserMomentsReviewService userMomentsReviewService) {
        this.userMomentsReviewService = userMomentsReviewService;
    }
}
