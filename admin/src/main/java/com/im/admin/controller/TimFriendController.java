package com.im.admin.controller;

import com.im.admin.config.permission.CheckPermission;
import com.im.admin.controller.url.ApiUrl;
import com.im.common.entity.enums.UserOperationLogTypeEnum;
import com.im.common.param.*;
import com.im.common.response.RestResponse;
import com.im.common.service.TimFriendService;
import com.im.common.util.aop.log.UserOperationLog;
import com.im.common.util.mybatis.page.PageVO;
import com.im.common.vo.TimFriendVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@Api(tags = "好友相关接口")
public class TimFriendController extends BaseController{

    private TimFriendService timFriendService;

    @Autowired
    public void setTimFriendService(TimFriendService timFriendService) {
        this.timFriendService = timFriendService;
    }

    @RequestMapping(value = ApiUrl.TIM_FRIEND_PAGE, method = RequestMethod.POST)
    @CheckPermission
    @ApiOperation("好友分页")
    public RestResponse<PageVO<TimFriendVO>> pageVO(@RequestBody @Valid TimFriendPageParam param) {
        PageVO<TimFriendVO> pageVO = timFriendService.pageVO(param, TimFriendVO::new);
        return ok(pageVO);
    }

    @RequestMapping(value = ApiUrl.TIM_FRIEND_ADD, method = RequestMethod.POST)
    @CheckPermission
    @UserOperationLog(type= UserOperationLogTypeEnum.TIM_FRIEND_ADD)
    @ApiOperation("添加好友")
    public RestResponse add(@RequestBody @Valid TimFriendAddParam param) {
        return timFriendService.addForAdmin(param.getUserId(),param.getFriendUserId());
    }

    @RequestMapping(value = ApiUrl.TIM_FRIEND_DELETE, method = RequestMethod.POST)
    @CheckPermission
    @UserOperationLog(type= UserOperationLogTypeEnum.TIM_FRIEND_DELETE)
    @ApiOperation("删除好友")
    public RestResponse delete(@RequestBody @Valid TimFriendDeleteParam param) {
        return timFriendService.deleteForAdmin(param.getUserId(),param.getFriendUserId());
    }

    @RequestMapping(value = ApiUrl.TIM_FRIEND_DELETE_ALL, method = RequestMethod.POST)
    @CheckPermission
    @UserOperationLog(type= UserOperationLogTypeEnum.TIM_FRIEND_DELETE_ALL)
    @ApiOperation("删除用户所有的好友")
    public RestResponse deleteAll(@RequestBody @Valid TimFriendDeleteAllParam param) {
        return timFriendService.deleteAllForAdmin(param.getUserId());
    }

}
