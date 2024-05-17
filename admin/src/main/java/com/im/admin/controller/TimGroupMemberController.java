package com.im.admin.controller;

import com.im.admin.config.permission.CheckPermission;
import com.im.admin.controller.url.ApiUrl;
import com.im.common.cache.impl.PortalUserCache;
import com.im.common.entity.enums.UserOperationLogTypeEnum;
import com.im.common.param.*;
import com.im.common.response.RestResponse;
import com.im.common.service.TimGroupMemberService;
import com.im.common.util.aop.log.UserOperationLog;
import com.im.common.util.mybatis.page.PageVO;
import com.im.common.vo.TimGroupMemberVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 群组成员相关接口
 */
@RestController
@Api(tags = "群组成员相关接口")
public class TimGroupMemberController extends BaseController {

    private TimGroupMemberService timGroupMemberService;
    private PortalUserCache portalUserCache;

    @Autowired
    public void setTimGroupMemberService(TimGroupMemberService timGroupMemberService) {
        this.timGroupMemberService = timGroupMemberService;
    }

    @Autowired
    public void setPortalUserCache(PortalUserCache portalUserCache) {
        this.portalUserCache = portalUserCache;
    }

    @RequestMapping(value = ApiUrl.TIM_GROUP_MEMBER_PAGE, method = RequestMethod.POST)
    @CheckPermission
    @ApiOperation("成员分页")
    public RestResponse<PageVO<TimGroupMemberVO>> groupMemberPage(@RequestBody @Valid TimGroupMemberPageParam param) {
        PageVO<TimGroupMemberVO> pageVO = timGroupMemberService.pageVOForAdmin(param);
        return ok(pageVO);
    }

    @RequestMapping(value = ApiUrl.TIM_GROUP_MEMBER_SHUT_UP, method = RequestMethod.POST)
    @CheckPermission
    @UserOperationLog(type = UserOperationLogTypeEnum.TIM_GROUP_MEMBER_SHUT_UP)
    @ApiOperation("群组成员设置禁言")
    public RestResponse shutUpMember(@RequestBody @Valid TimGroupMemberShutUpParam param) {
        return timGroupMemberService.shutUp(param);
    }

    @RequestMapping(value = ApiUrl.TIM_GROUP_MEMBER_DELETE, method = RequestMethod.POST)
    @CheckPermission
    @UserOperationLog(type = UserOperationLogTypeEnum.TIM_GROUP_MEMBER_DELETE)
    @ApiOperation("删除群组成员")
    public RestResponse deleteMember(@RequestBody @Valid TimGroupMemberDeleteParam param) {
        return timGroupMemberService.deleteMember(param);
    }

    @RequestMapping(value = ApiUrl.TIM_GROUP_MEMBER_ADD, method = RequestMethod.POST)
    @CheckPermission
    @UserOperationLog(type = UserOperationLogTypeEnum.TIM_GROUP_MEMBER_ADD)
    @ApiOperation("添加群组成员")
    public RestResponse addMember(@RequestBody @Valid TimGroupMemberAddParam param) {
        return timGroupMemberService.addMemberForAdmin(param);
    }

    @RequestMapping(value = ApiUrl.TIM_GROUP_SET_MANAGER, method = RequestMethod.POST)
    @CheckPermission
    @UserOperationLog(type = UserOperationLogTypeEnum.TIM_GROUP_SET_MANAGER)
    @ApiOperation("设置/取消群组管理员")
    public RestResponse setManager(@RequestBody @Valid TimGroupManagerParam param) {
        return timGroupMemberService.setManager(param);
    }


}
